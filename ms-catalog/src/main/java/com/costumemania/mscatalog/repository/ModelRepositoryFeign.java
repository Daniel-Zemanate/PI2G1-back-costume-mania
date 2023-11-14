package com.costumemania.mscatalog.repository;

import com.costumemania.mscatalog.model.Category;
import com.costumemania.mscatalog.model.Model;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name="ms-product")
public interface ModelRepositoryFeign {

    @GetMapping("/api/v1/model")
    ResponseEntity<List<Model>> getAllModel();

    @GetMapping("/api/v1/model/SEC/{id}")
    Model getByIdModelSEC (@PathVariable Integer id);

    @GetMapping("/api/v1/model/{id}")
    ResponseEntity<Optional<Model>>getByIdModel(@PathVariable Integer id);

    @GetMapping("api/v1/category/{idCategory}")
    ResponseEntity<Category> getdById (@PathVariable Integer idCategory);

    @GetMapping("/api/v1/model/category/id/{idCategory}")
    ResponseEntity<List<Model>> getModelByIdCategory(@PathVariable Integer idCategory);

    @GetMapping("/api/v1/model/name/{name}")
    ResponseEntity<Optional<List<Model>>> getByNameModel(@PathVariable String name);

    @GetMapping("/api/v1/model/name/{name}/category/id/{category}")
    ResponseEntity<List<Model>> getByNameAndCategoryModel(@PathVariable String name, @PathVariable Integer category);
}
