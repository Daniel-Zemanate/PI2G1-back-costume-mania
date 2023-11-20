package com.costumemania.msbills.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="ms-users")
public interface UsersRepositoryFeign {
    @GetMapping(path = "/users/{id}")
    ResponseEntity<?> userById(@PathVariable(name = "id") int id);
}
