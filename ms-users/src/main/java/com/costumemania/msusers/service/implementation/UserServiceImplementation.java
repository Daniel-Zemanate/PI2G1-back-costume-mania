package com.costumemania.msusers.service.implementation;

import com.costumemania.msusers.model.dto.CreateUserRequest;
import com.costumemania.msusers.model.dto.UserAccountResponse;
import com.costumemania.msusers.model.entity.Role;
import com.costumemania.msusers.model.entity.UserEntity;
import com.costumemania.msusers.repository.IUserRepository;
import com.costumemania.msusers.service.IUserService;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImplementation implements IUserService {

    private IUserRepository userRepository;

    public UserServiceImplementation(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserAccountResponse createUser(CreateUserRequest user) {

        UserEntity userEntity = UserEntity.builder()
                .dni(user.getDni())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())//TODO: ENCRYPT PASS
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .role(Role.USER)
                .build();
        userEntity = userRepository.save(userEntity);

        UserAccountResponse userResponse = UserAccountResponse.builder()
                .id(userEntity.getId())
                .dni(userEntity.getDni())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .status(userEntity.getStatus())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .role(userEntity.getRole())
                .build();

        return userResponse;
    }

    @Override
    public UserAccountResponse getByUsername(String username) {
        Optional<UserEntity> userExists = userRepository.findByUsername(username);
        if (userExists.isEmpty()) throw new NotFoundException(String.format("Username: %s Not found", username));

        UserAccountResponse userResponse = UserAccountResponse.builder()
                .id(userExists.get().getId())
                .dni(userExists.get().getDni())
                .username(userExists.get().getUsername())
                .email(userExists.get().getEmail())
                .firstName(userExists.get().getFirstName())
                .lastName(userExists.get().getLastName())
                .status(userExists.get().getStatus())
                .createdAt(userExists.get().getCreatedAt())
                .updatedAt(userExists.get().getUpdatedAt())
                .role(userExists.get().getRole())
                .build();
        return userResponse;
    }

    @Override
    public UserAccountResponse getById(int id) {
        Optional<UserEntity> userExists = userRepository.findById(id);
        if (userExists.isEmpty()) throw new NotFoundException(String.format("User id: %s Not found", id));

        UserAccountResponse userResponse = UserAccountResponse.builder()
                .id(userExists.get().getId())
                .dni(userExists.get().getDni())
                .username(userExists.get().getUsername())
                .email(userExists.get().getEmail())
                .firstName(userExists.get().getFirstName())
                .lastName(userExists.get().getLastName())
                .status(userExists.get().getStatus())
                .createdAt(userExists.get().getCreatedAt())
                .updatedAt(userExists.get().getUpdatedAt())
                .role(userExists.get().getRole())
                .build();
        return userResponse;
    }

    @Override
    public Set<UserAccountResponse> getAllUsers() {
        Set<UserAccountResponse> setUsers = userRepository.findAll().stream()
                .map(user -> UserAccountResponse.builder()
                        .id(user.getId())
                        .dni(user.getDni())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .status(user.getStatus())
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt())
                        .role(user.getRole())
                        .build()).collect(Collectors.toSet());
        return setUsers;
    }
}
