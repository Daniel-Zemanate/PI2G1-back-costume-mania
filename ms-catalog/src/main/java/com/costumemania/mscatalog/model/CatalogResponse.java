package com.costumemania.mscatalog.model;

import java.util.ArrayList;
import java.util.List;

public class CatalogResponse {

    private String model;
    private Integer modelId;
    private String category;
    private String image;
    private Integer sizeType;
    private Float price;
    private List<SizeByModel> sizes = new ArrayList<>();

    public static class SizeByModel {
        private Integer idCatalog;
        private String size;
        private Integer quantity;

        public SizeByModel() {
        }

        public SizeByModel(Integer idCatalog, String size, Integer quantity) {
            this.idCatalog = idCatalog;
            this.size = size;
            this.quantity = quantity;
        }

        public void setIdCatalog(Integer idCatalog) {
            this.idCatalog = idCatalog;
        }
        public void setSize(String size) {
            this.size = size;
        }
        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
        public Integer getIdCatalog() {
            return idCatalog;
        }
        public String getSize() {
            return size;
        }
        public Integer getQuantity() {
            return quantity;
        }
    }

    public CatalogResponse() {
    }

    public CatalogResponse(String model, Integer modelId, String category, String image, Integer sizeType, Float price, List<SizeByModel> sizes) {
        this.model = model;
        this.modelId = modelId;
        this.category = category;
        this.image = image;
        this.sizeType = sizeType;
        this.price = price;
        this.sizes = sizes;
    }

    public void setModel(String model) {
        this.model = model;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setSizeType(Integer sizeType) {
        this.sizeType = sizeType;
    }
    public void setPrice(Float price) {
        this.price = price;
    }
    public void setSizes(List<SizeByModel> sizes) {
        this.sizes = sizes;
    }

    public String getModel() {
        return model;
    }
    public String getImage() {
        return image;
    }
    public Integer getSizeType() {
        return sizeType;
    }
    public Float getPrice() {
        return price;
    }
    public List<SizeByModel> getSizes() {
        return sizes;
    }
    public Integer getModelId() {
        return modelId;
    }
    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
