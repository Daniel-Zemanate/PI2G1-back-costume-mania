package com.costumemania.mscatalog.controller;

import com.costumemania.mscatalog.model.Size;
import com.costumemania.mscatalog.service.SizeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/size")
public class SizeControler {

    private final SizeService sizeService;

    public SizeControler(SizeService sizeService) {
        this.sizeService = sizeService;
    }

    @GetMapping
    public List<Size> getAll(){
        return sizeService.getAll();
    }

    @GetMapping("/{idSize}")
    public Optional<Size> getById(@PathVariable Integer idSize){
        return sizeService.getById(idSize);
    }

    @GetMapping("/type/{bolleanAdult}")
    public List<Size> getSizeType(@PathVariable Integer bolleanAdult){
        return sizeService.getByAdult(bolleanAdult);
    }
}
