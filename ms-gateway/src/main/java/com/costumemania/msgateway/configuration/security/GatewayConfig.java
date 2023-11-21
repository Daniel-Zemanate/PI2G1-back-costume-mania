//package com.costumemania.msgateway.configuration.security;
//
//import com.costumemania.msgateway.configuration.security.jwt.JWTUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
////@EnableHystrix TODO: check dependency if required
//public class GatewayConfig {
//
//    @Autowired
//    private RouteValidator validator;
//    @Autowired
//    private JWTUtil jwtUtil;
//
//    public RouteLocator routes(RouteLocatorBuilder builder) {
//
//        AuthenticationFilter authenticationFilter = new AuthenticationFilter(validator, jwtUtil);
//
//        return builder.routes()
//                .route("ms-users", r -> r.path("/api/v1/users/**")
//                        .filters(filter -> filter.filter(authenticationFilter))
//                        .uri("lb://ms-users"))
//                .route("ms-users-auth", r -> r.path("/api/v1/auth/**")
//                        .filters(filter -> filter.filter(authenticationFilter))
//                        .uri("lb://ms-users"))
//                .build();
//
//    }
//}
