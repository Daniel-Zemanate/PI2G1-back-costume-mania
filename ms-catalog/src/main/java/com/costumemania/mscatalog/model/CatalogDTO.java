package com.costumemania.mscatalog.model;

public class CatalogDTO {
    private Integer model;
    private Integer size;
    private Integer quantity;
    private Float price;

    public CatalogDTO(Integer model, Integer size, Integer quantity, Float price) {
        this.model = model;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
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
}
