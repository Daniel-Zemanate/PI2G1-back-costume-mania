package com.costumemania.msbills.model;

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
@Table(name = "size")
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_size",unique = true, nullable = false)
    private Integer id;

    @Column(name = "adult")
    private Integer adult;

    @Column(name = "no_size")
    private String noSize;

    @Column(name = "size_description")
    private String sizeDescription;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getAdult() {
        return adult;
    }
    public String getNoSize() {
        return noSize;
    }
    public String getSizeDescription() {
        return sizeDescription;
    }
}
