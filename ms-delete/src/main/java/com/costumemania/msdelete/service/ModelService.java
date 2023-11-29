package com.costumemania.msdelete.service;

import com.costumemania.msdelete.model.Category;
import com.costumemania.msdelete.model.Model;
import com.costumemania.msdelete.repository.ModelRepositoryFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Optional;

@Service
public class ModelService {
    @Autowired
    ModelRepositoryFeign modelRepositoryFeign;

    public ResponseEntity<Model> makeInactive(@RequestHeader("Authorization") String token, @PathVariable Integer idModel) {
        return modelRepositoryFeign.makeInactive(token, idModel);
    }

    public ResponseEntity<String> makeInactivByCat (@RequestHeader("Authorization") String token, @PathVariable Integer idCategory) {
        return modelRepositoryFeign.makeInactivByCat(token, idCategory);
    };

    public ResponseEntity<Category> makeInactiveCat (@RequestHeader("Authorization") String token, @PathVariable Integer idCategory) {
        return modelRepositoryFeign.makeInactiveCat(token, idCategory);
    }
}
