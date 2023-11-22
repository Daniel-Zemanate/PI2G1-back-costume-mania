package com.costumemania.msbills.repository;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name= "ms-users")
@Headers("Authorization: {token}")
public interface UsersRepositoryFeign {
    @GetMapping(path = "/api/v1/users/{id}")
    ResponseEntity<?> userById(@RequestHeader("Authorization") String token, @PathVariable(name = "id") int id);
}