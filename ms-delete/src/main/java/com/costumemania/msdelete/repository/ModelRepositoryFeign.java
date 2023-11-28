package com.costumemania.msdelete.repository;

import com.costumemania.msdelete.LoadBalancerConfiguration;
import com.costumemania.msdelete.model.Category;
import com.costumemania.msdelete.model.Model;
import feign.Headers;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name="ms-product")
@LoadBalancerClient(name = "ms-product", configuration = LoadBalancerConfiguration.class)
@Headers("Authorization: {token}")
public interface ModelRepositoryFeign {
    @PutMapping("/api/v1/model/delete/{idModel}")
    ResponseEntity<Model> makeInactive(@RequestHeader("Authorization") String token, @PathVariable Integer idModel);
    @PutMapping("/api/v1/model/deleteByC/{idCategory}")
    ResponseEntity<String> makeInactivByCat (@RequestHeader("Authorization") String token, @PathVariable Integer idCategory);
    @PutMapping("/api/v1/category/delete/{idCategory}")
    ResponseEntity<Category> makeInactiveCat (@RequestHeader("Authorization") String token, @PathVariable Integer idCategory);
}
