package com.costumemania.msusers.service;

import com.costumemania.msusers.model.dto.CreateUserRequest;
import com.costumemania.msusers.model.dto.UserAccountResponse;
import com.costumemania.msusers.model.entity.UserEntity;
import com.mysql.cj.jdbc.exceptions.SQLError;

import java.sql.SQLException;
import java.util.Set;

public interface IUserService {

    UserAccountResponse createUser(CreateUserRequest user);
    UserAccountResponse getByUsername(String username) throws Exception;
    UserAccountResponse getById(int id);
    Set<UserAccountResponse> getAllUsers();

}
