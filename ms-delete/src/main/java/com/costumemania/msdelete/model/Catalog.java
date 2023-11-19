package com.costumemania.msdelete.model;

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
@Table(name = "catalog")
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catalog",unique = true, nullable = false)
    private Integer idCatalog;

    @ManyToOne
    @JoinColumn(name = "model")
    private Model model;

    @ManyToOne
    @JoinColumn(name = "size")
    private Size size;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Float price;
}
