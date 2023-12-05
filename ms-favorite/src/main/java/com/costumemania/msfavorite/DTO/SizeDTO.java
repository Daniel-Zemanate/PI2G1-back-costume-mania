package com.costumemania.msfavorite.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SizeDTO {

    private Integer id;

    private Integer adult;

    private String noSize;

    private String sizeDescription;

}
