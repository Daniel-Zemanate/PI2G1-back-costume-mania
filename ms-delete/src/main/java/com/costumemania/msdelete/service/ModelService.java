package com.costumemania.msdelete.service;

import com.costumemania.msdelete.model.Category;
import com.costumemania.msdelete.model.Model;
import com.costumemania.msdelete.repository.ModelRepositoryFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class ModelService {
    @Autowired
    ModelRepositoryFeign modelRepositoryFeign;

    public ResponseEntity<Model> makeInactive(@PathVariable Integer idModel) {
        return modelRepositoryFeign.makeInactive(idModel);
    }

    public ResponseEntity<String> makeInactivByCat (@PathVariable Integer idCategory) {
        return modelRepositoryFeign.makeInactivByCat(idCategory);
    };

    public ResponseEntity<Category> makeInactiveCat (@PathVariable Integer idCategory) {
        return modelRepositoryFeign.makeInactiveCat(idCategory);
    }
}
