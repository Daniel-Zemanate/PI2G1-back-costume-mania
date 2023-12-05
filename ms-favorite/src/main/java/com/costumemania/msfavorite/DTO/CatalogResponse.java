package com.costumemania.msfavorite.DTO;

import com.costumemania.msfavorite.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CatalogResponse {


    private Integer count;
    private Integer idModel;
    private String nameModel;
    private Category category;
    private String urlImage;
    private Integer sizeType;
    private Float price;



    private List<SizeByModel> sizes = new ArrayList<>();

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

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public Integer getIdCatalog() {
            return idCatalog;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }

    @Override
    public String toString() {
        return "CatalogResponse{" +
                ", idModel=" + idModel +
                ", nameModel='" + nameModel + '\'' +
                ", category=" + category +
                ", image='" + urlImage + '\'' +
                ", sizeType=" + sizeType +
                ", price=" + price +
                ", sizes=" + sizes +
                '}';
    }
}
