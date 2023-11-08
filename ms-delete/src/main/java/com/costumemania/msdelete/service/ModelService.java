package com.costumemania.msdelete.service;

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

    public ResponseEntity<List<Model>> getByIdCategory(Integer idCategory) {
        return modelRepositoryFeign.getByIdCategory(idCategory);
    };

    public ResponseEntity<String> deleteModel(Integer idModel) {
        return modelRepositoryFeign.deleteModel(idModel);
    };

    public ResponseEntity<String> deleteModelbyCat(Integer idCategory) {
        return modelRepositoryFeign.deleteModelByCategory(idCategory);
    };

    public ResponseEntity<String> delete(@PathVariable Integer idCategory) {
        return modelRepositoryFeign.delete(idCategory);
    };
}
