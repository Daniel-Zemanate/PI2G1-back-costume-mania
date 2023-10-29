package com.costumemania.mscatalog.controller;

import com.costumemania.mscatalog.model.Catalog;
import com.costumemania.mscatalog.service.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/catalog")
public class CatalogControler {

    private final CatalogService catalogService;

    public CatalogControler(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public List<Catalog> getAll(){
        return catalogService.getCatalog();
    }
}
