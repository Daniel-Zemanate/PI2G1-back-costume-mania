package com.costumemania.msreporting.service;

import com.costumemania.msreporting.model.jsonResponses.DateJson;
import com.costumemania.msreporting.model.requiredEntity.Sale;
import com.costumemania.msreporting.repository.SaleRepositoryFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class SaleService {
    @Autowired
    SaleRepositoryFeign saleRepositoryFeign;

    public ResponseEntity<List<Sale>> getAllSales() {
        return saleRepositoryFeign.getAllSales();
    };
    public ResponseEntity<List<Sale>> getByDates (@PathVariable String date1, @PathVariable String date2) {
        return saleRepositoryFeign.getByDates(date1,date2);
    };
    public ResponseEntity<DateJson> getFirstOrLastDate (@PathVariable int order) {
        return saleRepositoryFeign.getFirstOrLastDate(order);
    };
}