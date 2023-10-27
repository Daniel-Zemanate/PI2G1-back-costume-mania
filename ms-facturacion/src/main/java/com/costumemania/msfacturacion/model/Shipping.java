package com.costumemania.msfacturacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="shipping")
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_shipping")
    private Integer shippingId;
    @Column(nullable = false)
    private String destination;
    @Column(nullable = false)
    private Float cost;

    @OneToMany
    @JoinColumn(name = "id_shipping")
    private Set<Sale> sales;
}
