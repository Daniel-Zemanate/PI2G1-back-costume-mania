package com.costumemania.msdelete.repository;

import com.costumemania.msdelete.LoadBalancerConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name="ms-catalog")
@LoadBalancerClient(name = "ms-catalog", configuration = LoadBalancerConfiguration.class)
public interface CatalogRepositoryFeign {
    @PutMapping("/api/v1/catalog/deleteByM/{idModel}")
    ResponseEntity<String> makeInactivByModel (@PathVariable Integer idModel);
    @PutMapping("/api/v1/catalog/deleteByC/{idCategory}")
    ResponseEntity<String> makeInactivByCat (@PathVariable Integer idCategory);
}
