package com.costumemania.msusers.configuration.security;

import lombok.Data;

@Data
public class AuthCredentialsRequest {
    private String email;
    private String password;
}
