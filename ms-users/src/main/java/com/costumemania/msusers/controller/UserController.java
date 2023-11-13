package com.costumemania.msusers.controller;

import com.costumemania.msusers.model.dto.CreateUserRequest;
import com.costumemania.msusers.model.dto.UserAccountResponse;
import com.costumemania.msusers.service.IUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Value("${message}")
    private String message;
    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }


    @PostMapping(path = "/create")
//    public ResponseEntity<?> create(@RequestBody @Valid CreateUserRequest userRequest) {
    public ResponseEntity<?> create(@RequestBody CreateUserRequest userRequest) {

        UserAccountResponse userResponse;
        try {
            userResponse = userService.createUser(userRequest);
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected system error during your request", HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(userResponse, HttpStatusCode.valueOf(201));
    }

    @GetMapping(path = "/me")
    public ResponseEntity<?> userByUsername(@RequestParam(required = true) String username) {
        UserAccountResponse userResponse;

        if (username.isEmpty() || username.isBlank()) {
            return ResponseEntity.badRequest().body("username parameter is required");
        }
        try {
            userResponse = userService.getByUsername(username);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatusCode.valueOf(404));
        }

        return ResponseEntity.ok(userResponse);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> userById(@PathVariable(name = "id") int id) {
        UserAccountResponse userResponse;

        if (id < 0) {
            return ResponseEntity.badRequest().body("user id out of range");
        }
        try {
            userResponse = userService.getById(id);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatusCode.valueOf(404));
        }

        return ResponseEntity.ok(userResponse);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<?> allUsers() {
        Set<UserAccountResponse> setUsers = new HashSet<>();
        try {
            setUsers = userService.getAllUsers();
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected system error during your request", HttpStatusCode.valueOf(500));
        }

        return ResponseEntity.ok(setUsers);
    }


    //    This endpoind is only used to check ms-config-server
    @GetMapping(path = "/config-message")
    private String configMessage() {
        return message;
    }
}
