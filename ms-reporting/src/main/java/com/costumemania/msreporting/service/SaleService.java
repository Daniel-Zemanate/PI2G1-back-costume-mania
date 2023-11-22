package com.costumemania.msreporting.service;

import com.costumemania.msreporting.model.requiredEntity.Sale;
import com.costumemania.msreporting.repository.SaleRepositoryFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {
    @Autowired
    SaleRepositoryFeign saleRepositoryFeign;

    public ResponseEntity<List<Sale>> getAllSales() {
        return saleRepositoryFeign.getAllSales();
    };
}