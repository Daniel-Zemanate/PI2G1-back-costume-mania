package com.costumemania.msusers.model.dto;

import com.costumemania.msusers.model.entity.Role;
import com.costumemania.msusers.model.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateUserRequest {

    String dni;
    String username;
    String email;
    String password;
    String firstName;
    String lastName;


    public static UserEntity toUserEntity(CreateUserRequest user){
        return UserEntity.builder()
                .dni(user.getDni())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .status(true)
                .softDelete(false)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .role(Role.USER)
                .build();
    }

}