package com.costumemania.msusers.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path = "/users")
public class UserController {
    @Value("${message}")
    private String message;


//    This endpoind is only used to check ms-config-server
    @GetMapping(path = "/config-message")
    private String configMessage(){
        return message;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "/user-role")
    private String userRole(){
        return "Security is configured for ROLES... This one is with USER";
    }
}
