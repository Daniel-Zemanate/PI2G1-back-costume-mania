package com.costumemania.msfavorite.model;

import lombok.Getter;
import lombok.Setter;


public record Model(Integer idModel,String nameModel, Category category, String urlImage) {

}
