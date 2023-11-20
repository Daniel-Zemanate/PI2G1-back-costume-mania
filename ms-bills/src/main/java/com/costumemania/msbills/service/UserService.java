package com.costumemania.msbills.service;

import com.costumemania.msbills.repository.UsersRepositoryFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class UserService {
    @Autowired
    UsersRepositoryFeign usersRepositoryFeign;

    public ResponseEntity<?> userById(@PathVariable(name = "id") int id) {
        return usersRepositoryFeign.userById(id);
    };
}