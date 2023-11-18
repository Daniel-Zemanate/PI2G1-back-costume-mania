package com.costumemania.mscatalog.model;

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

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "price")
    private Float price;

    @ManyToOne
    @JoinColumn(name = "status_catalog")
    private StatusComponent statusCatalog;

    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public void setModel(Model model) {
        this.model = model;
    }
    public void setSize(Size size) {
        this.size = size;
    }
    public void setPrice(Float price) {
        this.price = price;
    }
    public void setIdCatalog(Integer idCatalog) {
        this.idCatalog = idCatalog;
    }
    public Integer getIdCatalog() {
        return idCatalog;
    }
    public Model getModel() {
        return model;
    }
    public Size getSize() {
        return size;
    }
    public Float getPrice() {
        return price;
    }
    public StatusComponent getStatus() {
        return statusCatalog;
    }
    public void setStatus(StatusComponent status) {
        this.statusCatalog = status;
    }
}
