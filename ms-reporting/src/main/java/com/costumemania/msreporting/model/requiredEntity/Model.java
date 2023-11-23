package com.costumemania.msreporting.model.requiredEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "model")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_model", unique = true, nullable = false)
    private Integer idModel;
    @Column(name = "name_model")
    private String nameModel;
    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;
    @Column(name = "url_image")
    private String urlImage;
    @ManyToOne
    @JoinColumn(name = "status_model")
    private StatusComponent statusModel;
}