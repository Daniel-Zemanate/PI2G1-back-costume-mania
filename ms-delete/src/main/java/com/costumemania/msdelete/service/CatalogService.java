package com.costumemania.msdelete.service;

import com.costumemania.msdelete.repository.CatalogRepositoryFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class CatalogService {
    @Autowired
    CatalogRepositoryFeign catalogRepositoryFeign;

    public ResponseEntity<String> makeInactivByModel (@PathVariable Integer idModel) {
        return catalogRepositoryFeign.makeInactivByModel(idModel);
    }
    public ResponseEntity<String> makeInactivByCat (@PathVariable Integer idCategory) {
        return catalogRepositoryFeign.makeInactivByCat(idCategory);
    }
}