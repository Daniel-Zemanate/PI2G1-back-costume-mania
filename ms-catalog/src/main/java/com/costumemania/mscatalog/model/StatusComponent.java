package com.costumemania.mscatalog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
}