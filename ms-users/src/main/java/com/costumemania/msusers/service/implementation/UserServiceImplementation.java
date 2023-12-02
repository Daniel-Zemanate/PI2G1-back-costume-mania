package com.costumemania.msusers.service.implementation;

import com.costumemania.msusers.configuration.mail.model.SimpleMailStructure;
import com.costumemania.msusers.configuration.mail.service.IEmailService;
import com.costumemania.msusers.model.dto.*;
import com.costumemania.msusers.model.entity.UserEntity;
import com.costumemania.msusers.repository.IUserRepository;
import com.costumemania.msusers.service.IUserService;
import jakarta.ws.rs.NotFoundException;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImplementation implements IUserService {

    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IEmailService emailService;


    public UserServiceImplementation(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserAccountResponse createUser(CreateUserRequest user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserEntity userEntity = CreateUserRequest.toUserEntity(user);

        userEntity = save(userEntity);

        UserAccountResponse userResponse = UserAccountResponse.fromUserEntity(userEntity);

        return userResponse;
    }

    @Override
    public UserAccountResponse getByUsername(String username) {
        Optional<UserEntity> userExists = userRepository.findOneByUsername(username);
        if (userExists.isEmpty()) throw new NotFoundException(String.format("Username: %s Not found", username));

        UserAccountResponse userResponse = UserAccountResponse.fromUserEntity(userExists.get());
        return userResponse;
    }

    @Override
    public UserAccountResponse getById(int id) {
        Optional<UserEntity> userExists = userRepository.findById(id);
        if (userExists.isEmpty()) throw new RuntimeException(String.format("User id: %s Not found", id));

        UserAccountResponse userResponse = UserAccountResponse.fromUserEntity(userExists.get());
        return userResponse;
    }

    @Override
    public UserExists userExists(int id) {
        return UserExists.fromUserAccountResponse(getById(id));
    }

    @Override
    public UserAccountResponse getByDni(String dni) {

        Optional<UserEntity> userExists = userRepository.findByDni(dni);
        if (userExists.isEmpty()) throw new RuntimeException(String.format("User dni: %s Not found", dni));

        UserAccountResponse userResponse = UserAccountResponse.fromUserEntity(userExists.get());
        return userResponse;
    }

    @Override
    public Set<UserAccountResponse> getAllUsers() {
        Set<UserAccountResponse> setUsers = userRepository.findAll().stream()
                .map(user -> UserAccountResponse.fromUserEntity(user)).collect(Collectors.toSet());
        return setUsers;
    }

    @Override
    public void deleteUserById(int id) {

        getById(id);
        userRepository.deleteById(id);
    }

    @Override
    public UserAccountResponse updateUserFromUser(UpdateUserRequest user) {

        UserAccountResponse foundUser = getById(user.getId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserEntity updateUser = UpdateUserRequest.toUserEntity(user);
        updateUser.setCreatedAt(foundUser.getCreatedAt());

        updateUser = save(updateUser);

        UserAccountResponse response = UserAccountResponse.fromUserEntity(updateUser);

        return response;
    }

    @Override
    public UserAccountResponse updateUserFromAdmin(UpdateFromAdmin user) {

        UserAccountResponse foundUser = getById(user.getId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserEntity updateUser = UpdateFromAdmin.toUserEntity(user);
        updateUser.setCreatedAt(foundUser.getCreatedAt());

        updateUser = save(updateUser);

        UserAccountResponse response = UserAccountResponse.fromUserEntity(updateUser);

        return response;
    }

    @Override
    public void resetPassword(ResetPassRequest resetPassRequest) {

        //Validating if user exists before next steps
        UserAccountResponse user = getByUsername(resetPassRequest.getEmail());

        String fullUsername = (user.getFirstName() + " " + user.getLastName()).toUpperCase();
        String newPassword = RandomStringUtils.randomAlphanumeric(9);

        //Build a UpdateUserRequest entity to reuse updateUserFromUser() method to achieve reset password
        UpdateUserRequest updateUserPass = UpdateUserRequest.fromUserAccountResponse(user);
        updateUserPass.setPassword(newPassword);

        updateUserFromUser(updateUserPass);


        SimpleMailStructure simpleMailStructure = SimpleMailStructure.builder()
                .toUser(new String[]{user.getEmail()})
                .subject("DO NOT REPLY: RESET PASSWORD; COSTUME MANIA")
                .message(String.format("Hello, %s.\n\n" +
                        "You have request reset your password, with this temporary password you will be able to access our platform. Please, change your password as soon as possible.\n\n" +
                        "New temporary password: %s", fullUsername, newPassword))
                .build();

        emailService.sendEmail(simpleMailStructure);

    }

    //Common method to be used for create and update
    private UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }
}
