package com.costumemania.msgateway.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//An http object is created that will allow controlling the authentication and authorization behavior when accessing through the gateway..
//        TODO: Check deprecated methods
        http
                .authorizeExchange().anyExchange()
                .authenticated()
                .and()
                .oauth2Login(Customizer.withDefaults());
        http.csrf().disable();

        return http.build();
    }
}
