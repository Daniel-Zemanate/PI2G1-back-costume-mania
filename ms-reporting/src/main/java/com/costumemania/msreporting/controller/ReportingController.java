package com.costumemania.msreporting.controller;

import com.costumemania.msreporting.model.jsonResponses.*;
import com.costumemania.msreporting.model.requiredEntity.Sale;
import com.costumemania.msreporting.service.SaleService;
import feign.FeignException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
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
    public ResponseEntity<AverageAndSaleList> averageShippingTime() {
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
        Collections.sort(sales, Comparator.comparing(Sale::getSaleDate));
        List<Sale> salesWithShipping = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getShippingDate() != null) {
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
        float average = (float) totalDays / salesWithShipping.size();
        double averageWith2Decimals = Math.round(average * 100) / 100.0;
        AverageShippingTime shippingTime = new AverageShippingTime(salesWithShipping.get(0).getSaleDate().toLocalDate(),
                salesWithShipping.get(salesWithShipping.size() - 1).getSaleDate().toLocalDate(),
                sales.size(),
                salesWithShipping.size(),
                averageWith2Decimals);

        AverageAndSaleList result = new AverageAndSaleList(shippingTime, salesWithShipping);
        return ResponseEntity.ok(result);
    }

    // average per month
    @GetMapping("/report1/{month}/{year}")
    public ResponseEntity<AverageAndSaleList> averageShippingTimeByMonth(@PathVariable int month, @PathVariable int year) {
        // get every sale
        List<Sale> sales = new ArrayList<>();
        String firstDay = year + "-" + month + "-01";
        // to generate last date
        LocalDate nextMonth;
        if (month == 12) {
            nextMonth = LocalDate.of(year + 1, 1, 1);
        } else {
            nextMonth = LocalDate.of(year, month + 1, 1);
        }
        LocalDate lastDay = nextMonth.minusDays(1);
        try {
            ResponseEntity<List<Sale>> respSales = saleService.getByDates(firstDay, String.valueOf(lastDay));
            if (respSales.getStatusCode() == HttpStatus.OK) {
                sales = respSales.getBody();
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
            if (sale.getShippingDate() != null) {
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
        float average = (float) totalDays / salesWithShipping.size();
        double averageWith2Decimals = Math.round(average * 100) / 100.0;
        AverageShippingTime shippingTime = new AverageShippingTime(salesWithShipping.get(0).getSaleDate().toLocalDate(),
                salesWithShipping.get(salesWithShipping.size() - 1).getSaleDate().toLocalDate(),
                sales.size(),
                salesWithShipping.size(),
                averageWith2Decimals);

        AverageAndSaleList result = new AverageAndSaleList(shippingTime, salesWithShipping);
        return ResponseEntity.ok(result);
    }

    // average per customized period
    @GetMapping("/report1/dates/{firstDate}/{lastDate}")
    public ResponseEntity<AverageAndSaleList> averageShippingCustom(@PathVariable String firstDate, @PathVariable String lastDate) {
        // get every sale
        List<Sale> sales = new ArrayList<>();
        try {
            ResponseEntity<List<Sale>> respSales = saleService.getByDates(firstDate, lastDate);
            if (respSales.getStatusCode() == HttpStatus.OK) {
                sales = respSales.getBody();
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
            if (sale.getShippingDate() != null) {
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
        float average = (float) totalDays / salesWithShipping.size();
        double averageWith2Decimals = Math.round(average * 100) / 100.0;
        AverageShippingTime shippingTime = new AverageShippingTime(salesWithShipping.get(0).getSaleDate().toLocalDate(),
                salesWithShipping.get(salesWithShipping.size() - 1).getSaleDate().toLocalDate(),
                sales.size(),
                salesWithShipping.size(),
                averageWith2Decimals);

        AverageAndSaleList result = new AverageAndSaleList(shippingTime, salesWithShipping);
        return ResponseEntity.ok(result);
    }

    // array per month
    @GetMapping("/report1/detailed")
    public ResponseEntity<List<ShippingTimePeriod>> getReportDetailed() {
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
            for (int i = firstMonth.getMonth(); i <= lastMonth.getMonth(); i++) {
                ResponseEntity<AverageAndSaleList> response = averageShippingTimeByMonth(i, firstMonth.getYear());
                if (response.getStatusCode() == HttpStatus.OK) {

                    ShippingTimePeriod newPeriod = new ShippingTimePeriod(
                            i + "/" + lastMonth.getYear(),
                            response.getBody().getAverageShippingTime().getAverageDelay());
                    result.add(newPeriod);
                }
            }
            return ResponseEntity.ok(result);
        }
        // if they are from different year, iterator per year and month
        for (int j = firstMonth.getYear(); j <= lastMonth.getYear(); j++) {
            for (int i = 1; i <= 12; i++) {
                ResponseEntity<AverageAndSaleList> response = averageShippingTimeByMonth(i, j);
                if (response.getStatusCode() == HttpStatus.OK) {
                    ShippingTimePeriod newPeriod = new ShippingTimePeriod(
                            i + "/" + j,
                            response.getBody().getAverageShippingTime().getAverageDelay());
                    result.add(newPeriod);
                }
            }
        }
        return ResponseEntity.ok(result);
    }

    // complete info for dashboard
    @GetMapping("/report1/complete")
    public ResponseEntity<ShippingTimeComplete> report1Complete() {
        double minDelay;
        String dateMin;
        double maxDelay;
        String dateMax;
        ResponseEntity<List<ShippingTimePeriod>> list = getReportDetailed();
        if (list.getStatusCode() == HttpStatus.OK && !list.getBody().isEmpty()) {
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

        // convert dates to string
        String[] splitMax = dateMax.split("/");
        String[] splitMin = dateMin.split("/");
        Calendar calMax = Calendar.getInstance();
        calMax.setTime(new Date(Integer.parseInt(splitMax[1]), Integer.parseInt(splitMax[0]) - 1, 1));
        String monthYearMax = calMax.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + splitMax[1];
        Calendar calMin = Calendar.getInstance();
        calMin.setTime(new Date(Integer.parseInt(splitMin[1]), Integer.parseInt(splitMin[0]) - 1, 1));
        String monthYearMin = calMin.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + splitMin[1];

        // final result
        ShippingTimeComplete shippingTimeComplete = new ShippingTimeComplete();
        shippingTimeComplete.setGeneralShippingTime(averageShippingTime().getBody().getAverageShippingTime());
        shippingTimeComplete.setMaxDelay(monthYearMax);
        shippingTimeComplete.setMinDelay(monthYearMin);
        shippingTimeComplete.setDetailedShippingTime(list.getBody());
        return ResponseEntity.ok(shippingTimeComplete);
    }


    //////////////////////////////////////////////////////////////////

    //////////////---------- Download ----------//////////////

    // function to get PDF
    public ResponseEntity<byte[]> pdfGenerator(String file, List<SaleDTO> list, AverageAndSaleList averageAndSaleList) {
        try {
            // Load .jrxml file and compile into a JasperReport
            JasperReport jasperReport = JasperCompileManager.compileReport(file);
            // parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Costume Mania");
            parameters.put("firstDate", averageAndSaleList.getAverageShippingTime().getFirstDate());
            parameters.put("lastDate", averageAndSaleList.getAverageShippingTime().getLastDate());
            parameters.put("averageDelay", (float) averageAndSaleList.getAverageShippingTime().getAverageDelay());
            parameters.put("quantitySales", averageAndSaleList.getAverageShippingTime().getQuantitySales());
            parameters.put("quantityDeliveredSales", averageAndSaleList.getAverageShippingTime().getQuantityDeliveredSales());
            // detailed data
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
            //  configure the response to bytes in PDF
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "salesReport.pdf");
            return new ResponseEntity<>(pdfBytes, headers, org.springframework.http.HttpStatus.OK);
        } catch (JRException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/generatePdfReport")
    public ResponseEntity<byte[]> generatePdfReportAllSale() {
        // get info
        AverageAndSaleList averageAndSaleList = averageShippingTime().getBody();
        // report deliveried sales
        List<SaleDTO> saleDTOList = new ArrayList<>();
        for (Sale sale : averageAndSaleList.getSaleList()) {
            saleDTOList.add(
                    new SaleDTO(
                            sale.getInvoice(),
                            sale.getCatalog().getModel().getNameModel(),
                            sale.getSaleDate().toLocalDate(),
                            sale.getShippingDate().toLocalDate(),
                            sale.getQuantity(),
                            sale.getStatus().getStatus()));
        }
        // show PDF
        return pdfGenerator(
                "ms-reporting/src/main/resources/AllSaleShippingReport.jrxml",
                saleDTOList,
                averageAndSaleList);
    }

    @GetMapping("/generatePdfReport/{month}/{year}")
    public ResponseEntity<byte[]> generatePdfReportPerMonth(@PathVariable int month, @PathVariable int year) {
        // get info
        ResponseEntity<AverageAndSaleList> averageAndSaleList = averageShippingTimeByMonth(month, year);
        if (averageAndSaleList.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.unprocessableEntity().build();
        }
        // just deliveried sales
        List<SaleDTO> saleDTOList = new ArrayList<>();
        for (Sale sale : averageAndSaleList.getBody().getSaleList()) {
            saleDTOList.add(
                    new SaleDTO(
                            sale.getInvoice(),
                            sale.getCatalog().getModel().getNameModel(),
                            sale.getSaleDate().toLocalDate(),
                            sale.getShippingDate().toLocalDate(),
                            sale.getQuantity(),
                            sale.getStatus().getStatus()));
        }
        // show PDF
        return pdfGenerator(
                "ms-reporting/src/main/resources/SaleByMonthShippingReport.jrxml",
                saleDTOList,
                averageAndSaleList.getBody());
    }

    // average per customized period
    @GetMapping("/generatePdfReport/dates/{firstDate}/{lastDate}")
    public ResponseEntity<byte[]> generatePdfaverageShippingCustom(@PathVariable String firstDate, @PathVariable String lastDate) {
        // get info
        ResponseEntity<AverageAndSaleList> averageAndSaleList = averageShippingCustom(firstDate, lastDate);
        if (averageAndSaleList.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.unprocessableEntity().build();
        }
        // report deliveried sales
        List<SaleDTO> saleDTOList = new ArrayList<>();
        for (Sale sale : averageAndSaleList.getBody().getSaleList()) {
            saleDTOList.add(
                    new SaleDTO(
                            sale.getInvoice(),
                            sale.getCatalog().getModel().getNameModel(),
                            sale.getSaleDate().toLocalDate(),
                            sale.getShippingDate().toLocalDate(),
                            sale.getQuantity(),
                            sale.getStatus().getStatus()));
        }
        // show PDF
        return pdfGenerator(
                "ms-reporting/src/main/resources/CustomSaleShippingReport.jrxml",
                saleDTOList,
                averageAndSaleList.getBody());
    }
}
