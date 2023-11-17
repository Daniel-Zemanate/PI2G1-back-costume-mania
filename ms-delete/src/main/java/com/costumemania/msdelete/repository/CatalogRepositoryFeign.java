package com.costumemania.msdelete.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name="ms-catalog")
public interface CatalogRepositoryFeign {
    @DeleteMapping("/api/v1/catalog/byModel/{idModel}")
    ResponseEntity<String> deleteByModel (@PathVariable Integer idModel);
    @PutMapping("/api/v1/catalog/deleteByM/{idModel}")
    ResponseEntity<String> makeInactivByModel (@PathVariable Integer idModel);
    @PutMapping("/api/v1/catalog/deleteByC/{idCategory}")
    ResponseEntity<String> makeInactivByCat (@PathVariable Integer idCategory);
}
