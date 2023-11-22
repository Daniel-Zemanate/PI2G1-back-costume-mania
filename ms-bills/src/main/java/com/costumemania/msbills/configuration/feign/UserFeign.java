package com.costumemania.msbills.configuration.feign;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserFeign {

    private Integer id;
    private String dni;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Boolean status;
    private Boolean softDelete;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Role role;
}
