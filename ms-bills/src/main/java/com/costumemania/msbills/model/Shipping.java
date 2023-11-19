package com.costumemania.msbills.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="shipping")
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_shipping",unique = true, nullable = false)
    private Integer idShippping;
    @Column(name = "destination")
    private String destination;
    @Column(name = "cost")
    private Float cost;

    public String getDestination() {
        return destination;
    }
    public Float getCost() {
        return cost;
    }
    public void setCost(Float cost) {
        this.cost = cost;
    }
    public Integer getIdShippping() {
        return idShippping;
    }
    public void setIdShippping(Integer idShippping) {
        this.idShippping = idShippping;
    }
}
