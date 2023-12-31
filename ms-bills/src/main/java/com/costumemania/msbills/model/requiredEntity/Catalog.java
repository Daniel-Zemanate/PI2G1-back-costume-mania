package com.costumemania.msbills.model.requiredEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "catalog")
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catalog",unique = true, nullable = false)
    private Integer idCatalog;
    @ManyToOne
    @JoinColumn(name = "model")
    private Model model;
    @ManyToOne
    @JoinColumn(name = "size")
    private Size size;
    @Column(name = "price")
    private Float price;
    @Column(name = "stock")
    private Integer stock;
    @ManyToOne
    @JoinColumn(name = "status_catalog")
    private StatusComponent statusCatalog;

    public StatusComponent getStatusCatalog() {
        return statusCatalog;
    }
    public Integer getStock() {
        return stock;
    }
    public Float getPrice() {
        return price;
    }
    public Integer getIdCatalog() {
        return idCatalog;
    }
    public Model getModel() {
        return model;
    }
}
