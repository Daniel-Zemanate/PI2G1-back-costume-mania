package com.costumemania.msreporting.controller;

import com.costumemania.msreporting.model.jsonResponses.*;
import com.costumemania.msreporting.model.requiredEntity.Sale;
import com.costumemania.msreporting.service.SaleService;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

// EVERY API HERE IS TO ADMIN
@RestController
@RequestMapping("/api/v1/reporting")
public class ReportingController {

    private final SaleService saleService;
    public ReportingController(SaleService saleService) {
        this.saleService = saleService;
    }

    // general average with every sale
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


            //////////////////////////////////////////////////////////////////

            //////////////---------- Download ----------//////////////

    @GetMapping("/generatePdfReport")
    public ResponseEntity<byte[]> generatePdfReportAllSale(HttpServletResponse response) {
        //System.setProperty("net.sf.jasperreports.builder.logger.factory", "net.sf.jasperreports.engine.simple.SimpleLoggerFactory");
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
        Collections.sort(sales, Comparator.comparing(Sale::getSaleDate));
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
        double averageWith2Decimals = Math.round(average*100) / 100.0;

        AverageShippingTime averageShippingResul = new AverageShippingTime(salesWithShipping.get(0).getSaleDate(),
                salesWithShipping.get(salesWithShipping.size()-1).getSaleDate(),
                sales.size(),
                salesWithShipping.size(),
                (float) averageWith2Decimals);

        // just deliveried sales
        List<SaleDTO> saleDTOList = new ArrayList<>();
        for (Sale sale: salesWithShipping) {
            saleDTOList.add(new SaleDTO(sale.getInvoice(),sale.getCatalog().getModel().getNameModel(),sale.getSaleDate().toLocalDate(),sale.getShippingDate().toLocalDate(), sale.getQuantity(),sale.getStatus().getStatus()));
        }

        try {
            // Load .jrxml file and compile into a JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport("ms-reporting/src/main/resources/AllSaleShippingReport.jrxml");
            // parameters
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(saleDTOList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Costume Mania");
        parameters.put("firstDate", averageShippingResul.getFirstDate().toLocalDate());
        parameters.put("lastDate", averageShippingResul.getLastDate().toLocalDate());
        parameters.put("averageDelay", averageShippingResul.getAverageDelay());
        parameters.put("quantitySales", averageShippingResul.getQuantitySales());
        parameters.put("quantityDeliveredSales", averageShippingResul.getQuantityDeliveredSales());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            //  configure the response to bytes in PDF
        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "salesReport.pdf");
        return new ResponseEntity<>(pdfBytes, headers, org.springframework.http.HttpStatus.OK);
        } catch (JRException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/generatePdfReport/{month}/{year}")
    public ResponseEntity<byte[]> generatePdfReportPerMonth(HttpServletResponse response, @PathVariable int month, @PathVariable int year) {
        //System.setProperty("net.sf.jasperreports.builder.logger.factory", "net.sf.jasperreports.engine.simple.SimpleLoggerFactory");
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
        Collections.sort(sales, Comparator.comparing(Sale::getSaleDate));
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
        double averageWith2Decimals = Math.round(average*100) / 100.0;

        AverageShippingTime averageShippingResul = new AverageShippingTime(salesWithShipping.get(0).getSaleDate(),
                salesWithShipping.get(salesWithShipping.size()-1).getSaleDate(),
                sales.size(),
                salesWithShipping.size(),
                (float) averageWith2Decimals);

        // just deliveried sales
        List<SaleDTO> saleDTOList = new ArrayList<>();
        for (Sale sale: salesWithShipping) {
            saleDTOList.add(new SaleDTO(sale.getInvoice(),sale.getCatalog().getModel().getNameModel(),sale.getSaleDate().toLocalDate(),sale.getShippingDate().toLocalDate(), sale.getQuantity(),sale.getStatus().getStatus()));
        }

        try {
            // Load .jrxml file and compile into a JasperReport
            JasperReport jasperReport = JasperCompileManager.compileReport("ms-reporting/src/main/resources/SaleByMonthShippingReport.jrxml");
            // parameters
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(saleDTOList);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Costume Mania");
            parameters.put("firstDate", averageShippingResul.getFirstDate().toLocalDate());
            parameters.put("lastDate", averageShippingResul.getLastDate().toLocalDate());
            parameters.put("averageDelay", averageShippingResul.getAverageDelay());
            parameters.put("quantitySales", averageShippingResul.getQuantitySales());
            parameters.put("quantityDeliveredSales", averageShippingResul.getQuantityDeliveredSales());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            //  configure the response to bytes in PDF
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "salesReport.pdf");
            return new ResponseEntity<>(pdfBytes, headers, org.springframework.http.HttpStatus.OK);
        } catch (JRException e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    // average per customized period
    @GetMapping("/generatePdfReport/dates/{firstDate}/{lastDate}")
    public ResponseEntity<byte[]> generatePdfaverageShippingCustom (@PathVariable String firstDate, @PathVariable String lastDate){
        //System.setProperty("net.sf.jasperreports.builder.logger.factory", "net.sf.jasperreports.engine.simple.SimpleLoggerFactory");
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
        Collections.sort(sales, Comparator.comparing(Sale::getSaleDate));
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
        double averageWith2Decimals = Math.round(average*100) / 100.0;

        AverageShippingTime averageShippingResul = new AverageShippingTime(salesWithShipping.get(0).getSaleDate(),
                salesWithShipping.get(salesWithShipping.size()-1).getSaleDate(),
                sales.size(),
                salesWithShipping.size(),
                (float) averageWith2Decimals);

        // just deliveried sales
        List<SaleDTO> saleDTOList = new ArrayList<>();
        for (Sale sale: salesWithShipping) {
            saleDTOList.add(new SaleDTO(sale.getInvoice(),sale.getCatalog().getModel().getNameModel(),sale.getSaleDate().toLocalDate(),sale.getShippingDate().toLocalDate(), sale.getQuantity(),sale.getStatus().getStatus()));
        }

        try {
            // Load .jrxml file and compile into a JasperReport
            JasperReport jasperReport = JasperCompileManager.compileReport("ms-reporting/src/main/resources/CustomSaleShippingReport.jrxml");
            // parameters
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(saleDTOList);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Costume Mania");
            parameters.put("firstDate", averageShippingResul.getFirstDate().toLocalDate());
            parameters.put("lastDate", averageShippingResul.getLastDate().toLocalDate());
            parameters.put("averageDelay", averageShippingResul.getAverageDelay());
            parameters.put("quantitySales", averageShippingResul.getQuantitySales());
            parameters.put("quantityDeliveredSales", averageShippingResul.getQuantityDeliveredSales());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            //  configure the response to bytes in PDF
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "salesReport.pdf");
            return new ResponseEntity<>(pdfBytes, headers, org.springframework.http.HttpStatus.OK);
        } catch (JRException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}