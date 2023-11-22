package com.costumemania.msreporting.model.jsonResponses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AverageShippingTime {
    private LocalDateTime firstDate;
    private LocalDateTime lastDate;
    private int quantitySales;
    private int quantityDeliveredSales;
    private float averageDelay;
}
