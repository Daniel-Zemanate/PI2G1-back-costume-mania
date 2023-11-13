package com.costumemania.msusers.model.dto;

import com.costumemania.msusers.model.entity.Role;
import com.costumemania.msusers.model.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserAccountResponse {
    Integer id;
    String dni;
    String username;
    String email;
    String firstName;
    String lastName;
    Boolean status;
    LocalDate createdAt;
    LocalDate updatedAt;
    Role role;

    public static UserAccountResponse fromUserEntity(UserEntity user){
        UserAccountResponse userResponse = UserAccountResponse.builder()
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
                .build();

        return userResponse;
    }

}
