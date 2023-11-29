package com.costumemania.msreporting.model.jsonResponses;


import java.io.Serializable;
import java.time.LocalDate;

public class SaleDTO implements Serializable {

    private Integer idSale;
    private String model;
    private LocalDate saleDate;
    private LocalDate shippingDate;
    private Integer quantity;
    private String status;

    public SaleDTO(Integer idSale, String model, LocalDate saleDate, LocalDate shippingDate, Integer quantity, String status) {
        this.idSale = idSale;
        this.model = model;
        this.saleDate = saleDate;
        this.shippingDate = shippingDate;
        this.quantity = quantity;
        this.status = status;
    }

    public Integer getIdSale() {
        return idSale;
    }

    public void setIdSale(Integer idSale) {
        this.idSale = idSale;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
