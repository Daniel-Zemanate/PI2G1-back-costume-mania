package com.costumemania.msreporting.model.jsonResponses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class AverageShippingTime {
    private LocalDate firstDate;
    private LocalDate lastDate;
    private int quantitySales;
    private int quantityDeliveredSales;
    private double averageDelay;
}
