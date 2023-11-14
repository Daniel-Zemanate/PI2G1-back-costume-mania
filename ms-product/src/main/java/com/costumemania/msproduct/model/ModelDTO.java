package com.costumemania.msproduct.model;

public class ModelDTO {

    private String nameModel;
    private Integer category;
    private String urlImage;

    public ModelDTO(String nameModel, Integer category, String urlImage) {
        this.nameModel = nameModel;
        this.category = category;
        this.urlImage = urlImage;
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
}
