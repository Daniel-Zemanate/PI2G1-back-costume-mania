//package com.costumemania.msgateway.configuration.security;
//
//import com.costumemania.msgateway.configuration.security.jwt.JWTUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//public class AuthenticationFilter implements GatewayFilter {
//
//    @Autowired
//    private RouteValidator validator;
//    @Autowired
//    private JWTUtil jwtUtil;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//
//        ServerHttpRequest request = exchange.getRequest();
//        if (validator.isSecure.test(request)) {
//            if (authMissing(request)){
//                return onError(exchange, HttpStatus.UNAUTHORIZED);
//            }
//        }
//
//        final String token = request.getHeaders().getOrEmpty("Authorization").get(0);
//
//        if (jwtUtil.isExpired(token)){
//            return onError(exchange, HttpStatus.UNAUTHORIZED);
//        }
//
//        return chain.filter(exchange);
//    }
//
//    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
//        ServerHttpResponse response = exchange.getResponse();
//        response.setStatusCode(httpStatus);
//        return response.setComplete();
//    }
//
//    private boolean authMissing(ServerHttpRequest request) {
//        return !request.getHeaders().containsKey("Authorization");
//    }
//}
