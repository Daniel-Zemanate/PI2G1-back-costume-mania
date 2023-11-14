package com.costumemania.msusers.configuration.security.service;

import com.costumemania.msusers.configuration.security.KeycloakProvider;
import com.costumemania.msusers.configuration.security.model.dto.KeycloakUserDTO;
import jakarta.validation.constraints.NotNull;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeycloakServiceImpl implements IKeycloakService {


    /**
     * List of users from keycloak
     *
     * @return List<UserRepresentation>
     */
    @Override
    public List<UserRepresentation> findAllUsers() {
        return KeycloakProvider.getRealmResource().users().list();
    }

    /**
     * Find user by username
     *
     * @param username
     * @return List<UserRepresentation>
     */
    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return KeycloakProvider.getRealmResource().users().searchByUsername(username, true);
    }

    /**
     * Create new user in keycloak
     *
     * @param keycloakUserDTO
     * @return String
     */
    @Override
    public String createUser(@NotNull KeycloakUserDTO keycloakUserDTO) {
        int status = 0;

        UsersResource usersResource = KeycloakProvider.getUserResource();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(keycloakUserDTO.getFirsName());
        userRepresentation.setLastName(keycloakUserDTO.getLastName());
        userRepresentation.setEmail(keycloakUserDTO.getEmail());
        userRepresentation.setUsername(keycloakUserDTO.getUsername());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        Response response = usersResource.create(userRepresentation);
        status = response.getStatus();

        if (status == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);
            credentialRepresentation.setValue(keycloakUserDTO.getPassword());

            usersResource.get(userId).resetPassword(credentialRepresentation);

            RealmResource realmResource = KeycloakProvider.getRealmResource();

            List<RoleRepresentation> roles = null;

            if (keycloakUserDTO.getRoles() == null || keycloakUserDTO.getRoles().isEmpty()) {
                roles = List.of(realmResource.roles().get("USER").toRepresentation());
            } else {
                roles = realmResource.roles().list().stream()
                        .filter(role -> keycloakUserDTO.getRoles().stream()
                                .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                        .collect(Collectors.toList());
            }
            realmResource.users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(roles);

            return "User created successfully";
        } else if (status == 409) {

            return "User already exists";
        } else {
            return "Error creating user, please contact admin.";
        }
    }

    /**
     * Delete user in keycloak
     * @param userId
     * @return void
     */
    @Override
    public void deleteUser(String userId) {
        KeycloakProvider.getUserResource()
                .get(userId)
                .remove();

    }

    /**
     * Update user in keycloak
     * @param userId
     * @param keycloakUserDTO
     */
    @Override
    public void updateUser(String userId, @NotNull KeycloakUserDTO keycloakUserDTO) {

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(keycloakUserDTO.getPassword());


        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(keycloakUserDTO.getFirsName());
        userRepresentation.setLastName(keycloakUserDTO.getLastName());
        userRepresentation.setEmail(keycloakUserDTO.getEmail());
        userRepresentation.setUsername(keycloakUserDTO.getUsername());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource userResource = KeycloakProvider.getUserResource().get(userId);
        userResource.update(userRepresentation);
    }
}
