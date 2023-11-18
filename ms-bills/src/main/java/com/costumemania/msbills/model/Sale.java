package com.costumemania.msbills.model;

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
    private Long saleId;

    @Column(name="no_invoice")
    private Integer no_invoice;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="shipping_address")
    private String shippingAddress;

    @ManyToOne
    @JoinColumn(name = "model")
    private Model model;


    @Column(name="sale_date")
    private Date saleDate;

    @Column(name="shipping_date")
    private Date shippingDate;

    @ManyToOne
    @JoinColumn(name = "shipping_city")
    private Shipping shippingCity;

    @ManyToOne
    @JoinColumn(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "id_user")
    private User user;

}
