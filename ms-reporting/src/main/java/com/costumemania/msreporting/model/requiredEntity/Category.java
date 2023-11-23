package com.costumemania.msreporting.model.requiredEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category",unique = true, nullable = false)
    private Integer idCategory;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "status_category")
    private StatusComponent statusCategory;
}
