package com.costumemania.msfavorite.DTO;

import com.costumemania.msfavorite.model.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class ModelDTO {


    private Integer idFav;
    private Integer idModel;
    private String nameModel;
    private Category category;
    private String urlImage;


    @Override
    public String toString() {
        return "ModelDTO{" +
                "idFav=" + idFav +
                ", idModel=" + idModel +
                ", nameModel='" + nameModel + '\'' +
                ", category=" + category +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }
}
