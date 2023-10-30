package com.costumemania.mscatalog.controller;

import com.costumemania.mscatalog.model.Catalog;
import com.costumemania.mscatalog.model.Size;
import com.costumemania.mscatalog.service.CatalogService;
import com.costumemania.mscatalog.service.SizeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/catalog")
public class CatalogController {
    private final CatalogService catalogService;
    private final SizeService sizeService;
    public CatalogController(CatalogService catalogService, SizeService sizeService) {
        this.catalogService = catalogService;
        this.sizeService = sizeService;
    }

    @GetMapping
    public ResponseEntity<List<Catalog>> getAll(){
        return ResponseEntity.ok().body(catalogService.getCatalog());
    }

    @GetMapping("/{idCatalog}")
    public ResponseEntity<Optional<Catalog>> getById(@PathVariable Integer idCatalog){

        // first verify if the ID exist
        Optional<Catalog> catalogProof = catalogService.getCatalogById(idCatalog);
        if (catalogProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        // else...
        return ResponseEntity.ok().body(catalogProof);
    }

    @GetMapping("/bySize/{bolleanAdult}")
    public ResponseEntity<List<List<Catalog>>> getBySize(@PathVariable Integer bolleanAdult){

        // first verify if the bollean is correct
        List<Size> sizeList = new ArrayList<>();
        sizeList = sizeService.getByAdult(bolleanAdult);

        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // else...
        List<List<Catalog>> result = new ArrayList<>();

        for (int i=0; i < sizeList.size(); i++) {
            List<Catalog> listBySize = new ArrayList<>();
            listBySize = catalogService.getCatalogBySize(sizeList.get(i));
            result.add(listBySize);
        }
        return ResponseEntity.ok().body(result);
    }
}
