package com.costumemania.msusers.controller;

import com.costumemania.msusers.model.dto.CreateUserRequest;
import com.costumemania.msusers.model.dto.UpdateFromAdmin;
import com.costumemania.msusers.model.dto.UpdateUserRequest;
import com.costumemania.msusers.model.dto.UserAccountResponse;
import com.costumemania.msusers.model.entity.UserEntity;
import com.costumemania.msusers.repository.IUserRepository;
import com.costumemania.msusers.service.IUserService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    //@Value("${message}")
    //private String message;
    private IUserService userService;

    private IUserRepository authUsersFeignRepository;

    public UserController(IUserService userService, IUserRepository authUsersFeignRepository) {
        this.userService = userService;
        this.authUsersFeignRepository = authUsersFeignRepository;
    }


    @PostMapping(path = "/create")
    @PreAuthorize("hasRole('NO_ACCESS')")
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
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> userByUsername(Principal principal) {
//    public ResponseEntity<?> userByUsername(@RequestParam(required = true) String username) {
        UserAccountResponse userResponse;
        String username = principal.getName();

        if (username.isEmpty() || username.isBlank()) {
            return ResponseEntity.badRequest().body("Something went wrong with Authentication info");
        }
        try {
            userResponse = userService.getByUsername(username);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(404));
        }

        return ResponseEntity.ok(userResponse);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
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

    @GetMapping(path = "/feign/{username}")
//    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserEntity> authUsersFeign(@PathVariable(name = "username") String username) {

        UserEntity userEntity = new UserEntity();
        try {
            userEntity = authUsersFeignRepository.findOneByUsername(username).get();
        } catch (Exception e) {
            return (ResponseEntity<UserEntity>) ResponseEntity.badRequest();
        }

        return ResponseEntity.ok(userEntity);
    }

    @GetMapping(path = "/all", produces = {"application/json"})
    public ResponseEntity<?> allUsers() {

        Set<UserAccountResponse> setUsers = new HashSet<>();
        ResponseEntity response;

        try {
            setUsers = userService.getAllUsers();
            response = ResponseEntity.ok(setUsers);
        } catch (Exception e) {
            response = ResponseEntity.badRequest().body("Something went wrong with your request. Try again, if error persists contact Admin for support: " + e.getMessage());
        }

        return response;
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") int id) {

        ResponseEntity response;

        try {
            userService.deleteUserById(id);
            response = ResponseEntity.accepted().body(id);
        } catch (Exception e) {
            response = ResponseEntity.badRequest().body("Something went wrong with your request. Try again, if error persists contact Admin for support: " + e.getMessage());
        }

        return response;
    }

    @PutMapping(path = "/update")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> updateUserFromUser(@RequestBody UpdateUserRequest userRequest) {

        UserAccountResponse userResponse;
        ResponseEntity response;

        try {
            userResponse = userService.updateUserFromUser(userRequest);
            response = ResponseEntity.ok().body(userResponse);
        } catch (Exception e) {
            response = ResponseEntity.badRequest().body("Something went wrong with your request. Try again, if error persists contact Admin for support: " + e.getMessage());
        }

        return response;
    }

    @PutMapping(path = "/admin/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserFromAdmin(@RequestBody UpdateFromAdmin userRequest) {

        UserAccountResponse userResponse;
        ResponseEntity response;

        try {
            userResponse = userService.updateUserFromAdmin(userRequest);
            response = ResponseEntity.ok().body(userResponse);
        } catch (Exception e) {
            response = ResponseEntity.badRequest().body("Something went wrong with your request. Try again, if error persists contact Admin for support: " + e.getMessage());
        }

        return response;
    }


    //    This endpoind is only used to check ms-config-server
//    @GetMapping(path = "/config-message")
//    private String configMessage() {
//        return message;
//    }
}
