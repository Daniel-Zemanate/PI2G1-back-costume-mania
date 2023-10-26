package com.costumemania.mscatalog.model;

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
    private Long catalogId;

    @ManyToOne
    @JoinColumn(name = "id_size")
    @Column(name = "size")
    private Size size;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Float price;
}
