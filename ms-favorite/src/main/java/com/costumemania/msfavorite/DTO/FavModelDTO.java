package com.costumemania.msfavorite.DTO;

import com.costumemania.msfavorite.model.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FavModelDTO {

    private Integer count;
    private Integer idModel;
    private String nameModel;
    private Category category;
    private String urlImage;
}
