package com.costumemania.msbills.controller;


import com.costumemania.msbills.service.ModelService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sale")
public class SaleController {


    //private final CatalogService catalogService;
    //private final SizeService sizeService;
    private final ModelService modelService;

    public SaleController(ModelService modelService) {
        this.modelService = modelService;
    }


}
