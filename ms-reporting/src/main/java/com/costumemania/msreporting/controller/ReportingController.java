package com.costumemania.msreporting.controller;

import com.costumemania.msreporting.model.jsonResponses.AverageShippingTime;
import com.costumemania.msreporting.model.jsonResponses.DateJson;
import com.costumemania.msreporting.model.jsonResponses.ShippingTimeComplete;
import com.costumemania.msreporting.model.jsonResponses.ShippingTimePeriod;
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

    // general average woth every sale
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

    // average per month
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

    // average per customized period
    @GetMapping("/report1/dates/{firstDate}/{lastDate}")
    public ResponseEntity<AverageShippingTime> averageShippingCustom (@PathVariable String firstDate, @PathVariable String lastDate){
        // get every sale
        List<Sale> sales = new ArrayList<>();
        try {
            ResponseEntity respSales = saleService.getByDates(firstDate, lastDate);
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

    // array per month
    @GetMapping("/report1/detailed")
    public ResponseEntity<List<ShippingTimePeriod>> getReportDetailed () {
        // get first and last dates
        DateJson firstMonth;
        DateJson lastMonth;
        try {
            firstMonth = saleService.getFirstOrLastDate(0).getBody();
            lastMonth = saleService.getFirstOrLastDate(1).getBody();
        } catch (FeignException e) {
            return ResponseEntity.internalServerError().build();
        }
        List<ShippingTimePeriod> result = new ArrayList<>();
        // if they are from same year, iterator per month
        if (firstMonth.getYear() == lastMonth.getYear()) {
            for (int i= firstMonth.getMonth(); i <= lastMonth.getMonth(); i++) {
                ResponseEntity<AverageShippingTime> response = averageShippingTimeByMonth (i, firstMonth.getYear());
                if (response.getStatusCode()==HttpStatus.OK) {
                    DateJson dateJson = new DateJson(i,firstMonth.getYear());
                    ShippingTimePeriod newPeriod = new ShippingTimePeriod(dateJson, response.getBody().getAverageDelay());
                    result.add(newPeriod);
                }
            }
            return ResponseEntity.ok(result);
        }
        // if they are from different year, iterator per year and month
        for (int j= firstMonth.getYear(); j <= lastMonth.getYear(); j++) {
            for (int i=1; i <= 12; i++) {
                ResponseEntity<AverageShippingTime> response = averageShippingTimeByMonth (i, j);
                if (response.getStatusCode()==HttpStatus.OK) {
                    DateJson dateJson = new DateJson(i,j);
                    ShippingTimePeriod newPeriod = new ShippingTimePeriod(dateJson, response.getBody().getAverageDelay());
                    result.add(newPeriod);
                }
            }
        }
        return ResponseEntity.ok(result);
    }

    // complete info for dashboard
    @GetMapping ("/report1/complete")
    public ResponseEntity<ShippingTimeComplete> report1Complete () {
        float minDelay;
        DateJson dateMin;
        float maxDelay;
        DateJson dateMax;
        ResponseEntity<List<ShippingTimePeriod>> list = getReportDetailed();
        if (list.getStatusCode()==HttpStatus.OK && !list.getBody().isEmpty()) {
            minDelay = list.getBody().get(0).getAverageShippingTime();
            maxDelay = list.getBody().get(0).getAverageShippingTime();
            dateMin = list.getBody().get(0).getPeriod();
            dateMax = list.getBody().get(0).getPeriod();
            for (ShippingTimePeriod period : list.getBody()) {
                if (period.getAverageShippingTime() <= minDelay) {
                    minDelay = period.getAverageShippingTime();
                    dateMin = period.getPeriod();
                }
                if (period.getAverageShippingTime() >= maxDelay) {
                    maxDelay = period.getAverageShippingTime();
                    dateMax = period.getPeriod();
                }
            }
        } else {
            return ResponseEntity.unprocessableEntity().build();
        }

        ShippingTimeComplete shippingTimeComplete = new ShippingTimeComplete();
        shippingTimeComplete.setGeneralShippingTime(averageShippingTime().getBody());
        shippingTimeComplete.setMaxDelay(dateMax);
        shippingTimeComplete.setMinDelay(dateMin);
        shippingTimeComplete.setDetailedShippingTime(list.getBody());
        return ResponseEntity.ok(shippingTimeComplete);
    }
}