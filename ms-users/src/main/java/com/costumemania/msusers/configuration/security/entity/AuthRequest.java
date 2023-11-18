package com.costumemania.msusers.configuration.security.entity;

import com.costumemania.msusers.model.dto.CreateUserRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequest {
    private String email;
    private String password;


    public static AuthRequest fromCreateUser(CreateUserRequest userRequest) {
        return AuthRequest.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();

    }
}
