package com.costumemania.msusers.configuration.security;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;

public class KeycloakProvider {
    private final static String SERVER_URL = "http://localhost:8180";
    private final static String REALM_NAME = "realm_costume_mania";
    private final static String REALM_MASTER = "master";
    private final static String ADMIN_CLI = "admin-cli";
    private final static String USER_CONSOLE = "admin";
    private final static String USER_PASSWORD = "admin";
    private final static String CLIENT_SECRET = "UoE5Z5zSQNYe8AVoSBImkPKhAflA17Tz";


    public static RealmResource getRealmResource() {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm(REALM_MASTER)
                .clientId(ADMIN_CLI)
                .username(USER_CONSOLE)
                .password(USER_PASSWORD)
                .clientSecret(CLIENT_SECRET)
                .resteasyClient(new ResteasyClientBuilderImpl()
                        .connectionPoolSize(10)
                        .build())
                .build();

        return keycloak.realm(REALM_NAME);
    }

    public static UsersResource getUserResource() {
        RealmResource realmResource = getRealmResource();
        return realmResource.users();
    }
}
