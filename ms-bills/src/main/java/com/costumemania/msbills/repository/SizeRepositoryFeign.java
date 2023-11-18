package com.costumemania.msbills.repository;

import com.costumemania.msbills.model.Model;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name="ms-catalog")
public interface SizeRepositoryFeign {


    @GetMapping("/api/v1/size")
    ResponseEntity<List<Model>> getAll();

    @GetMapping("/api/v1/size/{idSize}")
    ResponseEntity<List<Model>> getById();
}
