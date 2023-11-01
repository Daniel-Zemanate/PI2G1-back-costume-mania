package com.costumemania.msproduct.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "model")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_model",unique = true, nullable = false)
    private Integer idModel;

    @Column(name = "name_model")
    private String nameModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", referencedColumnName = "id_category")
    private Category category;

    @Column(name = "url_image")
    private String urlImage;
}
