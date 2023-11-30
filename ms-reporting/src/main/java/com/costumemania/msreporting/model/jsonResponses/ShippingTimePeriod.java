package com.costumemania.msreporting.model.jsonResponses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShippingTimePeriod {
    String period;
    double averageShippingTime;
}