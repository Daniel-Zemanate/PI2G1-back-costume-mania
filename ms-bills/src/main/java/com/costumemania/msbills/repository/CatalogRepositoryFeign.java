package com.costumemania.msbills.repository;

import com.costumemania.msbills.LoadBalancerConfiguration;
import com.costumemania.msbills.model.requiredEntity.Catalog;
import feign.Headers;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Optional;

@FeignClient(name="ms-catalog")
@LoadBalancerClient(name = "ms-catalog", configuration = LoadBalancerConfiguration.class)
@Headers("Authorization: {token}")
public interface CatalogRepositoryFeign {
    @GetMapping("/api/v1/catalog/{idCatalog}")
    ResponseEntity<Optional<Catalog>> getById(@PathVariable Integer idCatalog);
    @GetMapping("/api/v1/catalog/byModel2/{idModel}")
    ResponseEntity<Optional<List<Catalog>>> getByModel2(@PathVariable Integer idModel);
    @GetMapping("/api/v1/catalog/bySize/{bolleanAdult}")
    ResponseEntity<List<Catalog>> getBySize(@PathVariable Integer bolleanAdult);
    @PutMapping("/api/v1/catalog/{idCatalog}/{quantity}")
    ResponseEntity<Catalog> catalogSold(@RequestHeader("Authorization") String token, @PathVariable Integer idCatalog, @PathVariable Integer quantity);
}
