package com.costumemania.msusers.configuration.security.controller;

import com.costumemania.msusers.configuration.security.entity.AuthResponse;
import com.costumemania.msusers.configuration.security.service.AuthService;
import com.costumemania.msusers.model.dto.CreateUserRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> registry(@RequestBody CreateUserRequest user) {
        ResponseEntity response;
        AuthResponse authResponse = null;

        try {
            authResponse = authService.register(user);
        } catch (Exception e) {
            response = ResponseEntity.badRequest().body("Something went wrong with your request. Try again, if error persists contact Admin for support: " + e.getMessage());
        }

        response = new ResponseEntity<>(authResponse, HttpStatusCode.valueOf(201));

        return response;

    }
}
