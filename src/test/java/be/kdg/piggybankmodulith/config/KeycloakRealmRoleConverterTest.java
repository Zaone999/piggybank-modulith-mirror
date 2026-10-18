package be.kdg.piggybankmodulith.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class KeycloakRealmRoleConverterTest {

    private final KeycloakRealmRoleConverter converter = new KeycloakRealmRoleConverter();

    @Test
    void eachRealmRoleBecomesARoleAuthority() {
        Jwt jwt = jwt().claim("realm_access", Map.of("roles", List.of("parent", "grandparent"))).build();

        assertThat(converter.convert(jwt))
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_parent", "ROLE_grandparent");
    }

    @Test
    void aTokenWithoutRealmAccessHasNoAuthorities() {
        Jwt jwt = jwt().claim("preferred_username", "t_parent").build();

        assertThat(converter.convert(jwt)).isEmpty();
    }

    private static Jwt.Builder jwt() {
        return Jwt.withTokenValue("token").header("alg", "none");
    }
}
