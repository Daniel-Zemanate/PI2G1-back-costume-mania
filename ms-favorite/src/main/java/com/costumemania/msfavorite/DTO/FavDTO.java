package com.costumemania.msfavorite.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FavDTO {


    private Integer model;
    private Integer count;

    public FavDTO(Integer model, Integer count) {
        this.model = model;
        this.count = count;
    }

    @Override
    public String toString() {
        return "FavDTO{" +
                " model=" + model +
                ", count=" + count +
                '}';
    }

}
