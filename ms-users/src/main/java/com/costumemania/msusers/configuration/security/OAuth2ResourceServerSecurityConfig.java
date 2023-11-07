package com.costumemania.msusers.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class OAuth2ResourceServerSecurityConfig {

    private String KEYCLOAK_HOST = System.getenv("KEYCLOAK_HOST");
    private String KEYCLOAK_PORT = System.getenv("KEYCLOAK_PORT");
    private String KEYCLOAK_REALM = System.getenv("KEYCLOAK_REALM");

    //TODO: SecurityFilterChain might be modified to accept some access to specific public endpoints
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(new KeycloakJWTAuthenticationConverter());
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .anyRequest().authenticated(); //Security configuration, authentication required for all requests

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        if (KEYCLOAK_HOST != null && KEYCLOAK_PORT != null && KEYCLOAK_REALM != null) {
            return NimbusJwtDecoder.withJwkSetUri(String.format("http://%s:%s/realms/%s/protocol/openid-connect/certs", KEYCLOAK_HOST, KEYCLOAK_PORT, KEYCLOAK_REALM)).build();
        } else {
            return NimbusJwtDecoder.withJwkSetUri("http://localhost:8180/realms/realm_costume_mania/protocol/openid-connect/certs").build();
        }

    }
}