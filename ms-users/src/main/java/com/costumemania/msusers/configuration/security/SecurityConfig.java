//package com.costumemania.msusers.configuration.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//public class SecurityConfig {
//
//    private final UserDetailsService userDetailsService;
//    private JWTAuthorizationFilter jwtAuthorizationFilter;
//
//    @Autowired
//    public SecurityConfig(UserDetailsService userDetailsService, JWTAuthorizationFilter jwtAuthorizationFilter) {
//        this.userDetailsService = userDetailsService;
//        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
//    }
//
////    public static void main(String[] args) {
////        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
////        String result = encoder.encode("PI2G1-costume-mania");
////        System.out.println(result);
////        System.out.println(encoder.matches("PI2G1-costume-mania", result));
////    }
//
////    @Bean
////    UserDetailsService userDetailsService() {
////        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
////        manager.createUser(User
////                .withUsername("admin")
////                .password(passwordEncoder().encode("admin"))
////                .roles()
////                .build());
////        return manager;
////    }
//
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
//        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();
//        jwtAuthenticationFilter.setAuthenticationManager(authManager);
//        jwtAuthenticationFilter.setFilterProcessesUrl("/login");
//
//        return http
//                .csrf().disable()
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic()
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .addFilter(jwtAuthenticationFilter)
//                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }
//
//    @Bean
//    public static PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(12);
//
//    }
//
//    @Bean
//    AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
////                .userDetailsService(userDetailsService())
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder())
//                .and().build();
//    }
//}
