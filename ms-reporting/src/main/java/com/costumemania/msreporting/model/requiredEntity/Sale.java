package com.costumemania.msreporting.model.requiredEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
    @JoinColumn(name = "users")
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
    private LocalDateTime saleDate; //ver si no es date de sql
    @Column(name = "shipping_date")
    private LocalDateTime shippingDate;

    @Getter
    @Setter
    @Entity
    @Table(name="shipping")
    public class Shipping {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_shipping", unique = true, nullable = false)
        private Integer idShippping;
        @Column(name = "destination")
        private String destination;
        @Column(name = "cost")
        private Float cost;
    }

    @Getter
    @Setter
    @Entity
    @Table(name="status")
    public class Status {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_status")
        private Integer idStatus;
        @Column(name = "status", nullable = false)
        private String status;
    }
}
