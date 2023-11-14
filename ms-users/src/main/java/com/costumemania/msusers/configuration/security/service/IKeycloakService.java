package com.costumemania.msusers.configuration.security.service;

import com.costumemania.msusers.configuration.security.model.dto.KeycloakUserDTO;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IKeycloakService {

    List<UserRepresentation> findAllUsers();
    List<UserRepresentation> searchUserByUsername(String username);
    String createUser(KeycloakUserDTO keycloakUserDTO);
    void deleteUser(String userId);
    void updateUser(String userId, KeycloakUserDTO keycloakUserDTO);

}
