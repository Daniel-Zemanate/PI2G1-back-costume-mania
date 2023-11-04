package com.costumemania.msdelete.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="ms-catalog")
public interface CatalogRepositoryFeign {
    @DeleteMapping("/api/v1/catalog/byModel/{idModel}")
    ResponseEntity<String> deleteByModel (@PathVariable Integer idModel);
}
