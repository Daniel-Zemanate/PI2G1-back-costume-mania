package com.costumemania.mscatalog.client;

import com.costumemania.mscatalog.model.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;



@FeignClient("ms-product")
@LoadBalancerClient(value = "ms-product", configuration = com.costumemania.mscatalog.configuration.LoadBalanceConfiguration.class)
public interface ModelFeign {

    @GetMapping("/api/v1/model/{id}")
    Optional<Model> getByIdModel(@PathVariable Integer idModel);
    @GetMapping("/api/v1/model/name/{name}")
    Optional<List<Model>> getByNameModel(@PathVariable String nameModel);



}
