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

    @Column(nullable = false)
    private Integer no_invoice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String shipping_address;

    @Column(nullable = false)
    private Date sale_date;

    @Column
    private Date shipping_date;
}
