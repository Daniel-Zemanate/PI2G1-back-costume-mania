package com.costumemania.msfavorite.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CatalogDTO {

    private Integer idCatalog;

    private ModelCatalogDTO model;

    private SizeDTO size;

    private Integer stock;

    private Float price;

    private StatusComponentDTO statusCatalog;

    public Integer getIdCatalog() {
        return idCatalog;
    }

    public void setIdCatalog(Integer idCatalog) {
        this.idCatalog = idCatalog;
    }

    public ModelCatalogDTO getModel() {
        return model;
    }

    public void setModel(ModelCatalogDTO model) {
        this.model = model;
    }

    public SizeDTO getSize() {
        return size;
    }

    public void setSize(SizeDTO size) {
        this.size = size;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public StatusComponentDTO getStatusCatalog() {
        return statusCatalog;
    }

    public void setStatusCatalog(StatusComponentDTO statusCatalog) {
        this.statusCatalog = statusCatalog;
    }
}
