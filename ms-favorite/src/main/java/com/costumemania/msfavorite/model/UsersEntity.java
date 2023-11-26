package com.costumemania.msfavorite.model;

import java.time.LocalDate;

public record UsersEntity(String id, String dni, String username, String email, String password,
                          String first_name, String last_name, Boolean status, Boolean soft_delete,
                          LocalDate created_at, LocalDate updated_at, Role role) {
}


