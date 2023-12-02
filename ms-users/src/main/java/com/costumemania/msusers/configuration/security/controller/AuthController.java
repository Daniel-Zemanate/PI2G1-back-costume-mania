package com.costumemania.msusers.configuration.security.controller;

import com.costumemania.msusers.configuration.security.jwt.JwtUtils;
import com.costumemania.msusers.configuration.security.RegisterResponse;
import com.costumemania.msusers.model.dto.CreateUserRequest;
import com.costumemania.msusers.model.dto.UserAccountResponse;
import com.costumemania.msusers.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private IUserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest userRequest) {

        UserAccountResponse userResponse = new UserAccountResponse();
        ResponseEntity response = ResponseEntity.internalServerError().body(null);

        try {
            userResponse = userService.createUser(userRequest);
        } catch (Exception e) {
            response = ResponseEntity.badRequest().body("Something went wrong with your request. Try again, if error persists contact Admin for support: " + e.getMessage());
        }

        if (userResponse.getId() != null) {
            try {
                String token = jwtUtils.generateAccessToken(userResponse.getEmail());
//                String token = jwtUtils.generateAccessToken(userResponse.getUsername());
                RegisterResponse registerResponse = RegisterResponse.builder()
                        .token(token)
                        .userId(userResponse.getId())
                        .email(userResponse.getEmail())
                        .role(userResponse.getRole().name())
//                        .username(userResponse.getUsername())
                        .build();

                response = new ResponseEntity<>(registerResponse, HttpStatusCode.valueOf(201));
            } catch (Exception e) {
                response = ResponseEntity.badRequest().body("Something went wrong while generating token. Try Log in manually, if error persists contact Admin for support: " + e.getMessage());
            }
        }

        return response;
    }
}
