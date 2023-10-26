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
    private Long modelId;

    @Column(name = "name_model")
    private String modelName;
    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;

    @Column(name = "url_adult_image")
    private String adultImageUrl;

    @Column(name = "url_child_image")
    private String childImageUrl;
}
