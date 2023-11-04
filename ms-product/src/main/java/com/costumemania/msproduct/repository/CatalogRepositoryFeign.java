package com.costumemania.msproduct.repository;

import com.costumemania.msproduct.LoadBalancerConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="ms-catalog")
@LoadBalancerClient(name = "ms-catalog", configuration = LoadBalancerConfiguration.class)
public interface CatalogRepositoryFeign {
    @DeleteMapping("/api/v1/catalog/byModel/{idModel}")
    public ResponseEntity<String> deleteByModel (@PathVariable Integer idModel);
}
