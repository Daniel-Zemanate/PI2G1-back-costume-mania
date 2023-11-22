package com.costumemania.msbills.service;

import com.costumemania.msbills.model.requiredEntity.Catalog;
import com.costumemania.msbills.repository.CatalogRepositoryFeign;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogService {
    @Autowired
    CatalogRepositoryFeign catalogRepositoryFeign;

    public ResponseEntity<Optional<Catalog>> getById(@PathVariable Integer idCatalog) {
        return catalogRepositoryFeign.getById(idCatalog);
    }
    public ResponseEntity<Optional<List<Catalog>>> getByModel2(@PathVariable Integer idModel) {
        return catalogRepositoryFeign.getByModel2(idModel);
    }
    public ResponseEntity<List<Catalog>> getBySize(@PathVariable Integer bolleanAdult) {
        return catalogRepositoryFeign.getBySize(bolleanAdult);
    }
    public ResponseEntity<Catalog> catalogSold(@RequestHeader("Authorization") String token, @PathVariable Integer idCatalog, @PathVariable Integer quantity) {
        return catalogRepositoryFeign.catalogSold(token, idCatalog, quantity);
    };
}