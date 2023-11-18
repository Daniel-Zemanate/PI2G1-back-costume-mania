package com.costumemania.msusers.configuration.security.service;

import com.costumemania.msusers.configuration.security.entity.AuthRequest;
import com.costumemania.msusers.configuration.security.entity.AuthResponse;
import com.costumemania.msusers.configuration.security.jwt.JWTUtil;
import com.costumemania.msusers.model.dto.CreateUserRequest;
import com.costumemania.msusers.model.dto.UserAccountResponse;
import com.costumemania.msusers.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class AuthService {
    private final RestTemplate restTemplate;
    private final JWTUtil jwtUtil;
    private IUserService userService;

    public AuthResponse register(CreateUserRequest user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        AuthRequest request = AuthRequest.fromCreateUser(user);

        UserAccountResponse userCreated = userService.createUser(user);

        String accessToken = jwtUtil.generate(userCreated.getId().toString(), userCreated.getRole().toString(), "ACCESS");
        String refreshToken = jwtUtil.generate(userCreated.getId().toString(), userCreated.getRole().toString(), "REFRESH");

        return new AuthResponse(accessToken, refreshToken);
    }
}
