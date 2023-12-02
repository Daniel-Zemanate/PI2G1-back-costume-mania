package com.costumemania.msusers.service;

import com.costumemania.msusers.model.dto.*;

import java.util.Set;

public interface IUserService {

    UserAccountResponse createUser(CreateUserRequest user);

    UserAccountResponse getByUsername(String username) throws Exception;

    UserAccountResponse getById(int id);

    UserExists userExists(int id);

    UserAccountResponse getByDni(String dni);

    Set<UserAccountResponse> getAllUsers();

    void deleteUserById(int id);

    UserAccountResponse updateUserFromUser(UpdateUserRequest user);

    UserAccountResponse updateUserFromAdmin(UpdateFromAdmin user);

    void resetPassword(ResetPassRequest resetPassRequest);

}
