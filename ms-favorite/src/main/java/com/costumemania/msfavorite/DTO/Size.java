package com.costumemania.msfavorite.DTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class Size {

    private Integer id;

    private Integer adult;

    private String noSize;

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