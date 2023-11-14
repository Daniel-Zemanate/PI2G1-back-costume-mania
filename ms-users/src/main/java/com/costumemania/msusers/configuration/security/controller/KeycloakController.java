package com.costumemania.msusers.configuration.security.controller;

import com.costumemania.msusers.configuration.security.model.dto.KeycloakUserDTO;
import com.costumemania.msusers.configuration.security.service.IKeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(path = "/keycloak/admin")
@PreAuthorize("hasRole('ADMIN')")
public class KeycloakController {

    @Autowired
    IKeycloakService keycloakService;

    @GetMapping(path = "/search")
    public ResponseEntity<?> findAllUsers() {

        return ResponseEntity.ok(keycloakService.findAllUsers());
    }

    @GetMapping(path = "/search/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable(name = "username") String username) {

        return ResponseEntity.ok(keycloakService.searchUserByUsername(username));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> createUser(@RequestBody KeycloakUserDTO keycloakUserDTO) throws URISyntaxException {

        String response = keycloakService.createUser(keycloakUserDTO);
        return ResponseEntity.created(new URI("/keycloak/user/create")).body(response);
    }

    @PutMapping(path = "/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable(name = "userId") String userId, @RequestBody KeycloakUserDTO keycloakUserDTO) {

        keycloakService.updateUser(userId, keycloakUserDTO);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping(path = "/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "userId") String userId) {

        keycloakService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }


}
