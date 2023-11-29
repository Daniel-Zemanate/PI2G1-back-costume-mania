package com.costumemania.msdelete.repository;

import com.costumemania.msdelete.LoadBalancerConfiguration;
import feign.Headers;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="ms-catalog")
@LoadBalancerClient(name = "ms-catalog", configuration = LoadBalancerConfiguration.class)
@Headers("Authorization: {token}")
public interface CatalogRepositoryFeign {
    @PutMapping("/api/v1/catalog/deleteByM/{idModel}")
    ResponseEntity<String> makeInactivByModel (@RequestHeader("Authorization") String token, @PathVariable Integer idModel);
    @PutMapping("/api/v1/catalog/deleteByC/{idCategory}")
    ResponseEntity<String> makeInactivByCat (@RequestHeader("Authorization") String token, @PathVariable Integer idCategory);
}
