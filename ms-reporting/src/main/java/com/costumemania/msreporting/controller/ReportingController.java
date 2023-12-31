package com.costumemania.msreporting.controller;

import com.costumemania.msreporting.model.jsonResponses.AverageShippingTime;
import com.costumemania.msreporting.model.requiredEntity.Sale;
import com.costumemania.msreporting.service.SaleService;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

// EVERY API HERE IS TO ADMIN
@RestController
@RequestMapping("/api/v1/reporting")
public class ReportingController {

    private final SaleService saleService;
    public ReportingController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping("/report1")
    public ResponseEntity<AverageShippingTime> averageShippingTime (){
        // get every sale
        List<Sale> sales = new ArrayList<>();
        try {
            sales = saleService.getAllSales().getBody();
            if (sales.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        } catch (FeignException e) {
            return ResponseEntity.internalServerError().build();
        }
        // get sales with shipping
        List<Sale> salesWithShipping = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getShippingDate()!=null) {
                salesWithShipping.add(sale);
            }
        }
        if (salesWithShipping.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        // average days
        long totalDays = 0;
        for (Sale sale : salesWithShipping) {
            totalDays += DAYS.between(sale.getSaleDate(), sale.getShippingDate());
        }
        float average = (float) totalDays /salesWithShipping.size();
        AverageShippingTime result = new AverageShippingTime(salesWithShipping.get(0).getSaleDate(),
                salesWithShipping.get(salesWithShipping.size()-1).getSaleDate(),
                sales.size(),
                salesWithShipping.size(),
                average);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/report1/{month}/{year}")
    public ResponseEntity<AverageShippingTime> averageShippingTimeByMonth (@PathVariable int month, @PathVariable int year){
        // get every sale
        List<Sale> sales = new ArrayList<>();
        String firstDay = year + "-" + month + "-01";
        // to generate last date
        LocalDate nextMonth;
        if (month == 12) {
            nextMonth = LocalDate.of(year+1,1,1);
        } else {
            nextMonth = LocalDate.of(year,month+1,1);
        }
        LocalDate lastDay = nextMonth.minusDays(1);
        try {
            ResponseEntity respSales = saleService.getByDates(firstDay, String.valueOf(lastDay));
            if (respSales.getStatusCode()== HttpStatus.OK) {
                sales = (List<Sale>) respSales.getBody();
                if (sales.isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (FeignException e) {
            return ResponseEntity.internalServerError().build();
        }
        // get sales with shipping
        List<Sale> salesWithShipping = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getShippingDate()!=null) {
                salesWithShipping.add(sale);
            }
        }
        if (salesWithShipping.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        // average days
        long totalDays = 0;
        for (Sale sale : salesWithShipping) {
            totalDays += DAYS.between(sale.getSaleDate(), sale.getShippingDate());
        }
        float average = (float) totalDays /salesWithShipping.size();
        AverageShippingTime result = new AverageShippingTime(salesWithShipping.get(0).getSaleDate(),
                salesWithShipping.get(salesWithShipping.size()-1).getSaleDate(),
                sales.size(),
                salesWithShipping.size(),
                average);
        return ResponseEntity.ok(result);
    }
}