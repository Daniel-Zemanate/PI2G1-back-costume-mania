package com.costumemania.mscatalog.model;

public class CatalogDTO {
    private Integer model;
    private Integer size;
    private Integer quantity;
    private Float price;
    private Integer status;

    public CatalogDTO(Integer model, Integer size, Integer quantity, Float price, Integer status) {
        this.model = model;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
    }

    public Integer getModel() {
        return model;
    }
    public Integer getSize() {
        return size;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public Float getPrice() {
        return price;
    }
    public Integer getStatus() {
        return status;
    }
}
