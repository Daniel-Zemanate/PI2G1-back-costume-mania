package com.costumemania.msbills.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status")
    private Integer statusId;
    @Column(name = "status", nullable = false)
    private String status;
    @OneToMany
    @JoinColumn(name = "status")
    private Set<Sale> sales;
}
