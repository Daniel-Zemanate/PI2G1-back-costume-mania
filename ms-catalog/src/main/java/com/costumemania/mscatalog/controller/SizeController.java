package com.costumemania.mscatalog.controller;

import com.costumemania.mscatalog.model.Size;
import com.costumemania.mscatalog.service.SizeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/size")
public class SizeController {
    private final SizeService sizeService;

    public SizeController(SizeService sizeService) {
        this.sizeService = sizeService;
    }

    // api para ver todos los talles
    @GetMapping
    public List<Size> getAll(){
        return sizeService.getAll();
    }

    // api para ver talles segun el id del talle
    @GetMapping("/{idSize}")
    public Optional<Size> getById(@PathVariable Integer idSize){
        return sizeService.getById(idSize);
    }

    // api para ver talles segun si es de adulto (1) o si es de niño (0)
    @GetMapping("/type/{bolleanAdult}")
    public List<Size> getSizeType(@PathVariable Integer bolleanAdult){
        return sizeService.getByAdult(bolleanAdult);
    }
}
