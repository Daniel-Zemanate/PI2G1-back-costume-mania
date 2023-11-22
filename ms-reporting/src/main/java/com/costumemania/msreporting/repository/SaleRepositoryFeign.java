package com.costumemania.msreporting.repository;

import com.costumemania.msreporting.model.requiredEntity.Sale;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@FeignClient(name="ms-bills")
public interface SaleRepositoryFeign {
    @GetMapping("/api/v1/sale")
    ResponseEntity<List<Sale>> getAllSales();
}
