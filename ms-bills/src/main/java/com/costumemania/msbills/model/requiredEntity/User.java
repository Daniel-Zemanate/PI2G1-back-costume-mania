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
    public void setId(Integer id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }
}
