//package com.costumemania.msgateway.configuration.security;
//
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.function.Predicate;
//
//@Service
//public class RouteValidator {
//
//    public static final List<String> openEndpoints = List.of(
//            "/api/v1/auth/**",
//            "/api/v1/users/all"
//    );
//
//    public Predicate<ServerHttpRequest> isSecure =
//            request -> openEndpoints.stream()
//                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
//}
