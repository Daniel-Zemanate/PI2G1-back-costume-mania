package com.costumemania.msbills.model.requiredEntity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer id;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "status")
    private Boolean status = Boolean.TRUE;

    public Integer getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public Boolean getStatus() {
        return status;
    }
}
