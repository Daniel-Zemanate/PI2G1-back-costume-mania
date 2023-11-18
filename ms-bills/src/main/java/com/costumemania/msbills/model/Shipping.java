package com.costumemania.msbills.model;

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
    @Column(name = "destination")
    private String destination;
    @Column(name = "cost")
    private Float cost;

    @OneToMany
    @JoinColumn(name = "shipping_city")
    private Set<Sale> sales;
}
