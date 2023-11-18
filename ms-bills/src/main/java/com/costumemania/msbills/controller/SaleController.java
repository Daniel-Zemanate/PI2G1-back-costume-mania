package com.costumemania.msbills.controller;

import com.costumemania.msbills.model.Sale;
import com.costumemania.msbills.model.Status;
import com.costumemania.msbills.model.requiredEntity.Catalog;
import com.costumemania.msbills.model.requiredEntity.Model;
import com.costumemania.msbills.service.CatalogService;
import com.costumemania.msbills.service.SaleService;
import com.costumemania.msbills.service.StatusService;
import feign.FeignException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/sale")
public class SaleController {

    private final SaleService saleService;
    private final StatusService statusService;
    private final CatalogService catalogService;
    public SaleController(SaleService saleService, StatusService statusService, CatalogService catalogService) {
        this.saleService = saleService;
        this.statusService = statusService;
        this.catalogService = catalogService;
    }

    // adm
    @GetMapping
    public ResponseEntity<List<Sale>>getAllSales(){
        return ResponseEntity.ok(saleService.getAllSales());
    }

    // adm
    @GetMapping("/page/{page}")
    public Page<Sale> getAllSales(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 12);
        return saleService.getAllSales(pageable);
    }

    // adm
    @GetMapping("/{idSale}")
    public ResponseEntity<Optional<Sale>> getById(@PathVariable Integer idSale){
        // first verify if the ID exist
        Optional<Sale> saleProof = saleService.getById(idSale);
        if (saleProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // else...
        return ResponseEntity.ok().body(saleProof);
    }

    // adm
    @GetMapping("/status/{idStatus}")
    public ResponseEntity<List<Sale>> getByStatus (@PathVariable Integer idStatus){
        // first verify if the ID exist
        Optional<Status> statusProof = statusService.getById(idStatus);
        if (statusProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // else...
        Optional<List<Sale>> saleList = saleService.getByStatus(statusProof.get());
        if (saleList.get().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(saleList.get());
    }

    // user + adm
    // - AGREGAR EL FEIGN CUANDO ANDE USER!!!!
    /*@GetMapping("/user/{idUser}")
    public ResponseEntity<List<Sale>> getByStatus (@PathVariable Integer idUser){
        // first verify if the ID exist - AGREGAR EL FEIGN CUANDO ANDE USER!!!!
        /* TRY { Optional<User> userProof = userService.getById(idUser);
        } CATCH FEIGN EXCEPTION {
            return ResponseEntity.notFound().build();
        }
        // else...
        Optional<List<Sale>> saleList = saleService.getByUser(userProof);
        if (saleList.get().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(saleList.get());
    }*/

    // adm
    @GetMapping("/model/{idModel}")
    public ResponseEntity<List<Sale>> getByModel (@PathVariable Integer idModel){
        // verify if model exists
        List<Catalog> modelProof;
        try {
            modelProof = catalogService.getByModel2(idModel).getBody().get();
        } catch (FeignException e) {
            return ResponseEntity.notFound().build();
        }
        // else
        Optional<List<Sale>> saleList = saleService.getByModel(idModel);
        if (saleList.get().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(saleList.get());
    }

    // adm
    @GetMapping("/catalog/{idCatalog}")
    public ResponseEntity<List<Sale>> getByCatalog (@PathVariable Integer idCatalog){
        Catalog catalogProof;
        try {
            catalogProof = catalogService.getById(idCatalog).getBody().get();
        } catch (FeignException e) {
            return ResponseEntity.notFound().build();
        }
        Optional<List<Sale>> saleList = saleService.getByCatalog(catalogProof);
        if (saleList.get().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(saleList.get());
    }
}