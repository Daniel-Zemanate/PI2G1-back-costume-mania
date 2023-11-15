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
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category", unique = true, nullable = false)
    private Integer idCategory;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "status_category")
    private StatusComponent statusCategory;

    public Integer getIdCategory() {
        return idCategory;
    }
    public void setIdCategory(Integer idCategory) {
        this.idCategory = idCategory;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public StatusComponent getStatusCategory() {
        return statusCategory;
    }
    public void setStatusCategory(StatusComponent statusCategory) {
        this.statusCategory = statusCategory;
    }
}