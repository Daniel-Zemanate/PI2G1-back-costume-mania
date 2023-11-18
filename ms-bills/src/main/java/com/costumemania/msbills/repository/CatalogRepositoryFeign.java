package com.costumemania.msbills.repository;

import com.costumemania.msbills.model.Catalog;
import com.costumemania.msbills.model.Model;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name="ms-catalog")
public interface CatalogRepositoryFeign {

    @GetMapping("/api/v1/catalog")
    ResponseEntity<List<Catalog>> getAll();

    @GetMapping("/api/v1/catalog/{idCatalog}")
    ResponseEntity<Optional<Catalog>> getById(@PathVariable Integer idCatalog);
}
