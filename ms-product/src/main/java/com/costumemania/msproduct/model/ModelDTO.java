package com.costumemania.msproduct.model;

public class ModelDTO {

    private String nameModel;
    private Integer category;
    private String urlImage;
    private Integer status;

    public ModelDTO(String nameModel, Integer category, String urlImage, Integer status) {
        this.nameModel = nameModel;
        this.category = category;
        this.urlImage = urlImage;
        this.status = status;
    }

    public String getNameModel() {
        return nameModel;
    }
    public Integer getCategory() {
        return category;
    }
    public String getUrlImage() {
        return urlImage;
    }
    public Integer getStatus() {
        return status;
    }
}
