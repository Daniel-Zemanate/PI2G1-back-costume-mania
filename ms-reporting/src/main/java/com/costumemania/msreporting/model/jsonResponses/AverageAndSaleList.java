package com.costumemania.msreporting.model.jsonResponses;

import com.costumemania.msreporting.model.requiredEntity.Sale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AverageAndSaleList {
    private AverageShippingTime averageShippingTime;
    private List<Sale> saleList;
}
