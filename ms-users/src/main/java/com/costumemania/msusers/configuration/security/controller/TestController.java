package com.costumemania.msusers.configuration.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping(path = "/hello-1")
    public String helloAdmin() {

        return "Hello With Keycloak - ADMIN";
    }
    @GetMapping(path = "/hello-2")
    public String helloUser() {

        return "Hello With Keycloak - USER";
    }
}
