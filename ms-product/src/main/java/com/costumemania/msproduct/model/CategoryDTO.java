package com.costumemania.msproduct.model;

public class CategoryDTO {

    private Integer idCategory;
    private String name;
    private Integer status;

    public CategoryDTO(Integer idCategory, String name, Integer status) {
        this.idCategory = idCategory;
        this.name = name;
        this.status = status;
    }

    public Integer getIdCategory() {
        return idCategory;
    }
    public String getName() {
        return name;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
}