package com.costumemania.mscatalog.repository;

import com.costumemania.mscatalog.model.Model;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name="ms-product")
public interface ModelRepositoryFeign {

    @GetMapping("/api/v1/model/SEC/{id}")
    public Model getByIdModelSEC (@PathVariable Integer id);

    @GetMapping("/api/v1/model/{id}")
    public ResponseEntity<Optional<Model>>getByIdModel(@PathVariable Integer id);
}
