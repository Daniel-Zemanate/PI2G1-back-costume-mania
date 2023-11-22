package com.costumemania.msbills.repository;

import com.costumemania.msbills.configuration.feign.UserFeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="ms-users")
public interface UsersRepositoryFeign {
    @GetMapping(path = "/api/v1/users/{id}")
    ResponseEntity<?> userById(@PathVariable(name = "id") int id);
    @GetMapping("/api/v1/users/feign/{username}")
    UserFeign authUsersFeign(@PathVariable String username);
}
