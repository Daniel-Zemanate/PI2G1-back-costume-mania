package com.costumemania.mscatalog.service;

import com.costumemania.mscatalog.model.Category;
import com.costumemania.mscatalog.model.Model;
import com.costumemania.mscatalog.repository.ModelRepositoryFeign;
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

    public ResponseEntity<List<Model>> getAllModel() {
        return modelRepositoryFeign.getAllModel();
    };
    public ResponseEntity<List<Model>> getNewsLimit(Integer limit) {
        return modelRepositoryFeign.getNewsLimit(limit);
    };
    public ResponseEntity<Optional<Model>> getByIdModel(Integer id){
        return modelRepositoryFeign.getByIdModel(id);
    }
    public Model getByIdModelSEC(Integer id){
        return modelRepositoryFeign.getByIdModelSEC(id);
    }
    public ResponseEntity<Category> getCategorydById (Integer idCategory) {
        return modelRepositoryFeign.getdById(idCategory);
    };
    public  ResponseEntity<List<Model>> getModelByIdCategory(Integer idCategory) {
        return modelRepositoryFeign.getModelByIdCategory(idCategory);
    };
    public ResponseEntity<Optional<List<Model>>> getByNameModel(String name) {
        return modelRepositoryFeign.getByNameModel(name);
    };
    public ResponseEntity<List<Model>> getModelByNameAndIdCategory(String name,Integer category){
        return modelRepositoryFeign.getByNameAndCategoryModel(name,category);
    }
}
