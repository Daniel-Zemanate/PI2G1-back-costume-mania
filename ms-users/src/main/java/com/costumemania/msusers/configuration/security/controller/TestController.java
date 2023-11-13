package com.costumemania.msusers.configuration.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/hello-1")
    public String helloAdmin() {

        return "Hello With Keycloak - ADMIN";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping(path = "/hello-2")
    public String helloUser() {

        return "Hello With Keycloak - USER";
    }
}
