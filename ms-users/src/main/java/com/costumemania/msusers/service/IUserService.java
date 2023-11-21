package com.costumemania.msusers.service;

import com.costumemania.msusers.model.dto.CreateUserRequest;
import com.costumemania.msusers.model.dto.UpdateFromAdmin;
import com.costumemania.msusers.model.dto.UpdateUserRequest;
import com.costumemania.msusers.model.dto.UserAccountResponse;

import java.util.Set;

public interface IUserService {

    UserAccountResponse createUser(CreateUserRequest user);

    UserAccountResponse getByUsername(String username) throws Exception;

    UserAccountResponse getById(int id);

    UserAccountResponse getByDni(String dni);

    Set<UserAccountResponse> getAllUsers();

    void deleteUserById(int id);

    UserAccountResponse updateUserFromUser(UpdateUserRequest user);

    UserAccountResponse updateUserFromAdmin(UpdateFromAdmin user);

}
