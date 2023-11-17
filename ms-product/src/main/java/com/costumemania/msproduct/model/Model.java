package com.costumemania.msproduct.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "model")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_model",unique = true, nullable = false)
    private Integer idModel;

    @Column(name = "name_model")
    private String nameModel;

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;

    @Column(name = "url_image")
    private String urlImage;

    @ManyToOne
    @JoinColumn(name = "status_model")
    private StatusComponent statusModel;

    public Integer getIdModel() {
        return idModel;
    }
    public String getNameModel() {
        return nameModel;
    }
    public Category getCategory() {
        return category;
    }
    public String getUrlImage() {
        return urlImage;
    }
    public void setNameModel(String nameModel) {
        this.nameModel = nameModel;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
    public void setIdModel(Integer idModel) {
        this.idModel = idModel;
    }
    public StatusComponent getStatusModel() {
        return statusModel;
    }
    public void setStatusModel(StatusComponent statusModel) {
        this.statusModel = statusModel;
    }
}
