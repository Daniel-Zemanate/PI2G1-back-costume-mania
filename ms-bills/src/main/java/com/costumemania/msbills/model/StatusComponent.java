package com.costumemania.msbills.model;

import jakarta.persistence.*;

@Entity
@Table(name = "statuscomponent")
public class StatusComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status",unique = true, nullable = false)
    private Integer id;
    @Column(name = "description")
    private String description;

    public StatusComponent() {}
    public StatusComponent(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
}
