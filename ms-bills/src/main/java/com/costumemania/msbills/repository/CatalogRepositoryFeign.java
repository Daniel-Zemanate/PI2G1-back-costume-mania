package com.costumemania.msbills.repository;

import com.costumemania.msbills.model.requiredEntity.Catalog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name="ms-catalog")
public interface CatalogRepositoryFeign {
    @GetMapping("/api/v1/catalog/{idCatalog}")
    ResponseEntity<Optional<Catalog>> getById(@PathVariable Integer idCatalog);
    @GetMapping("/api/v1/catalog/byModel2/{idModel}")
    ResponseEntity<Optional<List<Catalog>>> getByModel2(@PathVariable Integer idModel);
}
