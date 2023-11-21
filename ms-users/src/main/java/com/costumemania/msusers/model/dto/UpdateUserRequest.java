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
public class UpdateUserRequest {

    Integer id;
    String dni;
//    String username;
    String email;
    String password;
    String firstName;
    String lastName;


    public static UserEntity toUserEntity(UpdateUserRequest user){
        return UserEntity.builder()
                .id(user.getId())
                .dni(user.getDni())
//                .username(user.getUsername())
                .username(user.getEmail())
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .status(true)
                .softDelete(false)
//                .createdAt(LocalDate.now()) //Parameter from ddbb
                .updatedAt(LocalDate.now())
                .role(Role.USER)
                .build();
    }

}