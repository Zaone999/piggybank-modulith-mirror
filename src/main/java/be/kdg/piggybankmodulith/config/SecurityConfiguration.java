package be.kdg.piggybankmodulith.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.POST, "/piggybanks/*/deposits").hasAnyRole("parent", "grandparent")
                        .requestMatchers("/family-members/**", "/piggybanks/**").hasRole("parent")
                        .requestMatchers("/grandchildren/**").hasRole("grandparent")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(server -> server.jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakAuthentication())))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .build();
    }

    private static JwtAuthenticationConverter keycloakAuthentication() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        converter.setPrincipalClaimName("preferred_username");
        return converter;
    }
}
