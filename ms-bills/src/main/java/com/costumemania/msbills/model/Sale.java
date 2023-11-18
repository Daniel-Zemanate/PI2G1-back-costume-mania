package com.costumemania.msbills.model;

import com.costumemania.msbills.model.requiredEntity.Catalog;
import com.costumemania.msbills.model.requiredEntity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sale",unique = true, nullable = false)
    private Integer idSale;
    @Column(name = "no_invoice")
    private Integer invoice;
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;
    @ManyToOne
    @JoinColumn(name = "catalog")
    private Catalog catalog;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "shipping_address")
    private String address;
    @ManyToOne
    @JoinColumn(name = "shipping_city")
    private Shipping city;
    @ManyToOne
    @JoinColumn(name = "status")
    private Status status;
    @Column(name = "sale_date")
    private Date saleDate; //ver si no es date de sql
    @Column(name = "shipping_date")
    private Date shippingDate;
}
