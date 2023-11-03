package com.costumemania.mscatalog.service;

import com.costumemania.mscatalog.model.Model;
import com.costumemania.mscatalog.repository.ModelRepositoryFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ModelService {
    @Autowired
    ModelRepositoryFeign modelRepositoryFeign;

    public ResponseEntity<Optional<Model>> getByIdModel(Integer id){
        return modelRepositoryFeign.getByIdModel(id);
    }
    public Model getByIdModelSEC(Integer id){
        return modelRepositoryFeign.getByIdModelSEC(id);
    }
}
