package com.costumemania.msbills.service;

import com.costumemania.msbills.model.requiredEntity.Catalog;
import com.costumemania.msbills.repository.CatalogRepositoryFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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
}