//package com.costumemania.msusers.configuration.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import java.io.IOException;
//import java.util.Collections;
//
//
//public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//
//        AuthCredentialsRequest authCredentials = new AuthCredentialsRequest();
//
//        try {
//            authCredentials = new ObjectMapper().readValue(request.getReader(), AuthCredentialsRequest.class);
//        } catch (IOException e) {
////            throw new RuntimeException(e);
//        }
//
//        UsernamePasswordAuthenticationToken usernamePAT = new UsernamePasswordAuthenticationToken(
//                authCredentials.getEmail(),
//                authCredentials.getPassword(),
//                Collections.emptyList());
//
////        return super.attemptAuthentication(request, response);
//        return getAuthenticationManager().authenticate(usernamePAT);
//    }
//
//
////    @Override
////    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
////        super.doFilter(request, response, chain);
////    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//
//        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
//
//        String token = TokenUtils.createToken(userDetails.getNombre(), userDetails.getUsername());
//
//        response.addHeader("Authorization", "Bearer " + token);
//        response.getWriter().flush();
//
//        super.successfulAuthentication(request, response, chain, authResult);
//    }
//}
