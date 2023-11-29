package com.costumemania.msdelete.service;

import com.costumemania.msdelete.repository.CatalogRepositoryFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
public class CatalogService {
    @Autowired
    CatalogRepositoryFeign catalogRepositoryFeign;

    public ResponseEntity<String> makeInactivByModel (@RequestHeader("Authorization") String token, @PathVariable Integer idModel) {
        return catalogRepositoryFeign.makeInactivByModel(token, idModel);
    }
    public ResponseEntity<String> makeInactivByCat (@RequestHeader("Authorization") String token, @PathVariable Integer idCategory) {
        return catalogRepositoryFeign.makeInactivByCat(token, idCategory);
    }
}