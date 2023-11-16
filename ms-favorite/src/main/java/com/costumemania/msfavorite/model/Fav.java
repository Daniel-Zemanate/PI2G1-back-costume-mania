package com.costumemania.msfavorite.model;

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
@Table(name="fav")
public class Fav {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fav")
    private Integer favId;
    @Column(name = "users", nullable = false)
    private Integer user;
    @Column(nullable = false)
    private Integer model;

    @Override
    public String toString() {
        return "Fav{" +
                "favId=" + favId +
                ", user=" + user +
                ", model=" + model +
                '}';
    }
}
