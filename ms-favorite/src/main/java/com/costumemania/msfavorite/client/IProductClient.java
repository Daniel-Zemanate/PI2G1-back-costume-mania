package com.costumemania.msfavorite.client;

import com.costumemania.msfavorite.LoadBalancerConfiguration;
import com.costumemania.msfavorite.model.Model;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(name= "ms-product")
@LoadBalancerClient(name = "ms-product", configuration = LoadBalancerConfiguration.class)
public interface IProductClient {

    @GetMapping("/api/v1/model")
    List<Model> getAllModel();

}
