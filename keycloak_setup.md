# Setting up Keycloak

The piggybank API only accepts requests that carry an access token issued by
Keycloak. This guide sets up the realm, its roles, two users and the client
the API checks tokens for.

| What     | Value                                    |
|----------|------------------------------------------|
| Keycloak | http://localhost:8180 (admin / admin)    |
| Realm    | `piggybank`                              |
| Issuer   | http://localhost:8180/realms/piggybank   |
| Client   | `piggybank-api` (confidential)           |

## 1. Start Keycloak

Keycloak is part of the infrastructure compose file:

```bash
(cd infrastructure && docker compose up -d)    # or: podman compose up -d
```

It runs as `idp_keycloak` in dev mode (`start-dev`, plain HTTP) on port 8180.
Its realms, clients and users are stored in the MySQL database of the
`idp_mysql` container (data in `infrastructure/idp_mysql_data`), so they
survive removing and recreating the Keycloak container. Open
http://localhost:8180 and log in with `admin` / `admin`.

> The application and the `.http` file must reach Keycloak on the **same**
> URL, `http://localhost:8180`: the issuer in every token is derived from it,
> and the application rejects tokens whose issuer differs from
> `spring.security.oauth2.resourceserver.jwt.issuer-uri`.

## 2. Create the realm

1. Open the realm dropdown at the top left (it says *Keycloak* or *master*) and
   choose **Create realm**.
2. **Realm name**: `piggybank`.
3. **Create**.

Do everything below inside the `piggybank` realm, never in `master`.

## 3. Create the realm roles

The application reads the **realm roles** from the access token
(`realm_access.roles`) and turns each one into a Spring authority
`ROLE_<role>`, see `config/KeycloakRealmRoleConverter`. So `hasRole("parent")`
matches the realm role `parent`.

| Realm role    | May use                                                          |
|---------------|------------------------------------------------------------------|
| `parent`      | `/family-members`, `/piggybanks`: register a birth, create, deposit, withdraw, list |
| `grandparent` | `/grandchildren`, and deposits into a piggybank                  |

1. **Realm roles** → **Create role**.
2. **Role name**: `parent`, **Save**.
3. Again for `grandparent`.

Keycloak puts the realm roles in the access token through the `roles` client
scope, a default scope of every client. Leave it assigned, and leave
**Always use lightweight access token** off on the client, or the roles are
missing from the token.

## 4. Create the users

| User            | Password     | Realm role    |
|-----------------|--------------|---------------|
| `t_parent`      | `t_password` | `parent`      |
| `t_grandparent` | `t_password` | `grandparent` |

For each of them:

1. **Users** → **Create new user**.
2. **Username**: `t_parent` (then `t_grandparent`). Fill in email, first and
   last name, and switch **Email verified** on. Keycloak would otherwise ask
   for this profile data at the first login.
3. **Create**.
4. Tab **Credentials** → **Set password**: `t_password`. Switch **Temporary**
   off, then **Save**.
5. Tab **Role mapping** → **Assign role** → **Filter by realm roles**, select
   `parent` for `t_parent`, `grandparent` for `t_grandparent`, **Assign**.

## 5. Create the client

The client id is the **audience** the application checks (`aud` in the token,
`spring.security.oauth2.resourceserver.jwt.audiences`). A developer logs in
with it, with the password grant, to call the API from the IntelliJ HTTP
client (`src/test/http/piggybank.http`).

1. **Clients** → **Create client**.
2. **General settings**
   - **Client type**: `OpenID Connect`
   - **Client ID**: `piggybank-api`
   - **Next**
3. **Capability config**
   - **Client authentication**: **On** (a confidential client, it has a secret)
   - **Authentication flow**: only **Direct access grants** checked (the
     password grant). Uncheck *Standard flow*.
   - **Next**
4. **Login settings**: no redirect URIs, the password grant has no redirect.
   **Save**.

> The password grant hands the user's password to the client, and OAuth 2.1
> drops it. It is here only to get a token from a `.http` file in one request.
> A real frontend uses the authorization code flow with PKCE.

### 5.1 Put the client in its own audience

Keycloak does not put a client in the `aud` of its own tokens, so add a mapper:

1. Open `piggybank-api` → tab **Client scopes** → `piggybank-api-dedicated`.
2. **Configure a new mapper** (or **Add mapper** → **By configuration**) →
   **Audience**.
3. **Name**: `audience piggybank-api`, **Included Client Audience**:
   `piggybank-api`, **Add to access token**: **On**.
4. **Save**.

### 5.2 Keep the secret out of Git

Tab **Credentials**: copy the **Client Secret** into
`src/test/http/http-client.private.env.json`:

```json
{
  "dev": {
    "client-secret": "<the client secret>",
    "password": "t_password"
  }
}
```

That file is in `.gitignore`; never commit a secret.

## 6. Try it

```bash
(cd infrastructure && docker compose up -d)    # Postgres, RabbitMQ, Keycloak :8180
./mvnw spring-boot:run                         # :8080
```

1. Without a token, the API answers `401 Unauthorized`:

   ```bash
   curl -i http://localhost:8080/piggybanks
   ```

2. In IntelliJ, open `src/test/http/piggybank.http` and select the `dev`
   environment. Run **Log in as a parent** at the top: it asks Keycloak for a
   token with the password grant and stores it in the global variable
   `access_token`, which every other request sends as its bearer. Log in again
   when the token expires (5 minutes by default). Further down, **Log in as a
   grandparent** switches the token to `t_grandparent`.

   The same login with curl:

   ```bash
   curl -s -d grant_type=password -d client_id=piggybank-api -d client_secret=<secret> \
     -d username=t_parent -d password=t_password -d scope=openid \
     http://localhost:8180/realms/piggybank/protocol/openid-connect/token
   ```

To look inside a token, paste it into https://jwt.io. Check `iss` (the realm),
`aud` (`piggybank-api`), `azp` (the client that asked for it),
`preferred_username` and `realm_access.roles`.

The Spring Shell commands and `scripts/init-and-deposit.sh` do not go through
HTTP, so they need no token.

## Troubleshooting

| Symptom                                              | Cause                                                                 |
|------------------------------------------------------|-----------------------------------------------------------------------|
| `401` with `invalid_token`, *aud claim is not valid* | The audience mapper on `piggybank-api` is missing                     |
| `401` with *iss claim is not valid*                  | The token came from a different URL than `http://localhost:8180`, or another realm |
| `unauthorized_client` on the `.http` login           | **Direct access grants** is not enabled on `piggybank-api`            |
| `invalid_client` on the `.http` login                | The `client-secret` in `http-client.private.env.json` does not match  |
| `401` in the `.http` file after it worked before     | The token expired: log in again                                       |
| `403 Forbidden`                                      | The user lacks the role: `parent` for the family endpoints, `grandparent` for `/grandchildren` |
| `hasRole(...)` never matches                         | The role is a client role instead of a realm role, it is not assigned, or lightweight access tokens are on |
