package com.costumemania.msfavorite.DTO;

import com.costumemania.msfavorite.model.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
public class ModelCatalogDTO {

    private Integer idFav;
    private Integer users;
    private Integer idModel;
    private String nameModel;
    private Category category;
    private String urlImage;
    private List<CatalogResponse.SizeByModel> sizes = new ArrayList<>();


    public static class SizeByModel {
        private Integer idCatalog;
        private String size;
        private Integer quantity;

        public SizeByModel() {
        }

        public SizeByModel(Integer idCatalog, String size, Integer quantity) {
            this.idCatalog = idCatalog;
            this.size = size;
            this.quantity = quantity;
        }

    }

}
