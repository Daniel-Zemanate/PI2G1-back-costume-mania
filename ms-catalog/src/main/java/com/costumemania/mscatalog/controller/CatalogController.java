package com.costumemania.mscatalog.controller;

import com.costumemania.mscatalog.model.Catalog;
import com.costumemania.mscatalog.service.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/catalog")
public class CatalogController {
    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public List<Catalog> getAll(){
        return catalogService.getCatalog();
    }

    @GetMapping("/{idCatalog}")
    public Optional<Catalog> getById(@PathVariable Integer idCatalog){
        return catalogService.getCatalogById(idCatalog);
    }
}
