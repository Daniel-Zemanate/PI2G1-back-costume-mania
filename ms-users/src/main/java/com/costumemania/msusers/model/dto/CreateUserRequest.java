package com.costumemania.msusers.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateUserRequest {
    @NotNull(message = "Dni can not be null")
    @NotBlank(message = "Dni can not be blank")
    String dni;
    @NotNull(message = "Username can not be null")
    @NotBlank(message = "Username can not be blank")
    String username;
    @NotNull(message = "Email can not be null")
    @Email(message = "Wrong email format",regexp = "^[\\w\\.-]+@[a-zA-Z\\d\\.-]+\\.[a-zA-Z]{2,}$")
    String email;
    @NotNull(message = "Password can not be null")
    @NotBlank(message = "Password can not be blank")
    @Length(min = 6, message = "Password should have at least 6 characters")
    String password;
    String firstName;
    String lastName;

}