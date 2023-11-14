package com.costumemania.msusers.configuration.security.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Set;


@Builder
@Value
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KeycloakUserDTO {

    private String username;
    private String email;
    private String firsName;
    private String lastName;
    private String password;
    private Set<String> roles;

}
