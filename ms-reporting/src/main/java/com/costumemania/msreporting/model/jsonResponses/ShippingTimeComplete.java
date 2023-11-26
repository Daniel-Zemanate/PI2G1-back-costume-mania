package com.costumemania.msreporting.model.jsonResponses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShippingTimeComplete {
    AverageShippingTime generalShippingTime;
    DateJson minDelay;
    DateJson maxDelay;
    List<ShippingTimePeriod> detailedShippingTime;
}
