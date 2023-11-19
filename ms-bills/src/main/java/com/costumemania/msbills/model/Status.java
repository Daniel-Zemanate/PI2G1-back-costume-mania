package com.costumemania.msbills.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status")
    private Integer idStatus;
    @Column(name = "status", nullable = false)
    private String status;

    public Status(Integer idStatus, String status) {
        this.idStatus = idStatus;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
    public Integer getIdStatus() {
        return idStatus;
    }
}
