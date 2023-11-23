package com.costumemania.msreporting.model.requiredEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
    @Column(name = "stock")
    private Integer stock;
    @Column(name = "price")
    private Float price;
    @ManyToOne
    @JoinColumn(name = "status_catalog")
    private StatusComponent statusCatalog;

    @Getter
    @Setter
    @Entity
    @Table(name = "size")
    public class Size {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_size", unique = true, nullable = false)
        private Integer id;
        @Column(name = "adult")
        private Integer adult;
        @Column(name = "no_size")
        private String noSize;
        @Column(name = "size_description")
        private String sizeDescription;
    }
}