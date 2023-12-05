package com.costumemania.msfavorite.client;

import com.costumemania.msfavorite.DTO.CatalogDTO;
import com.costumemania.msfavorite.LoadBalancerConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(name= "ms-catalog")
@LoadBalancerClient(name = "ms-catalog", configuration = LoadBalancerConfiguration.class)
public interface ICatalogClient {

    @GetMapping("/api/v1/catalog")
    List<CatalogDTO> getAll();

}
