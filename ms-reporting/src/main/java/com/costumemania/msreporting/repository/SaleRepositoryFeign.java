package com.costumemania.msreporting.repository;

import com.costumemania.msreporting.LoadBalancerConfiguration;
import com.costumemania.msreporting.model.jsonResponses.DateJson;
import com.costumemania.msreporting.model.requiredEntity.Sale;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="ms-bills")
@LoadBalancerClient(name = "ms-bills", configuration = LoadBalancerConfiguration.class)
public interface SaleRepositoryFeign {
    @GetMapping("/api/v1/sale")
    ResponseEntity<List<Sale>> getAllSales();
    @GetMapping("/api/v1/sale/dates/{date1}/{date2}")
    ResponseEntity<List<Sale>> getByDates (@PathVariable String date1, @PathVariable String date2);
    @GetMapping("/api/v1/sale/invoiceDate/{order}")
    ResponseEntity<DateJson> getFirstOrLastDate (@PathVariable int order);
}
