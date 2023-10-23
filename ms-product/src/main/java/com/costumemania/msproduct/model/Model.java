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
    private Long id_mode;
    private String name_model;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private String url_adult_image;
    private String url_child_image;
}
