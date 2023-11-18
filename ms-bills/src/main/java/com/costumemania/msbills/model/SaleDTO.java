package com.costumemania.msbills.model;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Date;

public class SaleDTO {

    private Integer no_invoice;

    private Integer quantity;

    private String shippingAddress;

    private Integer model;

    private Date saleDate;

    private Date shippingDate;

    private Integer shippingCity;

    private Integer status;

    private Integer user;

    public SaleDTO(Integer no_invoice, Integer quantity, String shippingAddress, Integer model, Date saleDate, Date shippingDate, Integer shippingCity, Integer status, Integer user) {
        this.no_invoice = no_invoice;
        this.quantity = quantity;
        this.shippingAddress = shippingAddress;
        this.model = model;
        this.saleDate = saleDate;
        this.shippingDate = shippingDate;
        this.shippingCity = shippingCity;
        this.status = status;
        this.user = user;
    }

    public Integer getNo_invoice() {
        return no_invoice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public Integer getModel() {
        return model;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public Integer getShippingCity() {
        return shippingCity;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getUser() {
        return user;
    }
}
