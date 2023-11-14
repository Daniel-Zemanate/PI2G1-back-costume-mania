package com.costumemania.msfavorite.client;

import com.costumemania.msfavorite.model.Model;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(name= "ms-product")
public interface IProductClient {

    @GetMapping("/api/v1/model")
    List<Model> getAllModel();

}
