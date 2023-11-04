package com.costumemania.msdelete.repository;

import com.costumemania.msdelete.model.Model;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name="ms-product")
public interface ModelRepositoryFeign {

    @GetMapping("/api/v1/model/SEC/{id}")
    Model getByIdModelSEC (@PathVariable Integer id);

    @GetMapping("/api/v1/model/{id}")
    ResponseEntity<Optional<Model>>getByIdModel(@PathVariable Integer id);

    @GetMapping("/api/v1/model/category/id/{idCategory}")
    ResponseEntity<List<Model>> getByIdCategory(@PathVariable Integer idCategory);

    @DeleteMapping("/api/v1/model/delete/{idModel}")
    ResponseEntity<String> deleteModel(@PathVariable Integer idModel);

    @DeleteMapping("/api/v1/model/deleteByCategory/{idCategory}")
    ResponseEntity<String> deleteModelByCategory(@PathVariable Integer idCategory);

    @DeleteMapping("/api/v1/category/delete/{idCategory}")
    ResponseEntity<String> delete (@PathVariable Integer idCategory);
}
