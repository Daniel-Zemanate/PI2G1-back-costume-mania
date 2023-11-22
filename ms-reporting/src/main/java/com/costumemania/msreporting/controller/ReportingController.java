package com.costumemania.msreporting.controller;

import com.costumemania.msreporting.model.jsonResponses.AverageShippingTime;
import com.costumemania.msreporting.model.requiredEntity.Sale;
import com.costumemania.msreporting.service.SaleService;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

// EVERY API HERE ARE TO ADMIN
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

        // una forma de obtener el ultimo dia del mes. Ojo que no va a funcionar para el limite diciembre/enero, hay que ponerle logica
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,1);
        int last = calendar.getActualMaximum ( Calendar.DATE);
        String lastDay = year + "-" + month + "-" + last;
        // otra forma podria ser restando 1 al primero del mes anterior. Pero tambien hay que agregar lógica.

        try {
            sales = saleService.getByDates(firstDay, lastDay).getBody();
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
}