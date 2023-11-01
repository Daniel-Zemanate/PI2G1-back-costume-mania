package com.costumemania.msproduct.controller;

import com.costumemania.msproduct.model.Model;
import com.costumemania.msproduct.service.ModelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/models")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    //Andando
    @PostMapping
    @ResponseStatus(code= HttpStatus.CREATED)
    public ResponseEntity<Model> createModel(@RequestBody Model model){
        modelService.saveModel(model);
        return ResponseEntity.ok(model);
    }

    @PutMapping
    @ResponseStatus(code=HttpStatus.OK)
    public ResponseEntity updateModel(@RequestBody Model model){
        modelService.updateModel(model);
        return ResponseEntity.ok().build();
    }

    //Andando
    @GetMapping
    @ResponseStatus(code= HttpStatus.OK)
    public ResponseEntity<List<Model>> getAllModel(){
        return ResponseEntity.ok(modelService.getAllModel());
    }

    //Andando
    @GetMapping("/id/{id}")
    @ResponseStatus(code= HttpStatus.OK)
    public ResponseEntity<Optional<Model>>getByIdModel(@PathVariable Integer id){
return ResponseEntity.ok(modelService.getByIdModel(id));

    }
    //Andando
    @GetMapping("/name/{name}")
    @ResponseStatus(code= HttpStatus.OK)
    public ResponseEntity<Model> getByNameModel(@PathVariable String name){
        return ResponseEntity.ok(modelService.getByNameModel(name));
    }
    @GetMapping("/category/{category}")
    @ResponseStatus(code= HttpStatus.OK)
    public ResponseEntity<List<Model>> getByCategoryModel(@PathVariable String category){
        return ResponseEntity.ok(modelService.getByCategoryModel(category));
    }
    @GetMapping("/name/{name}/category/{category}")
    @ResponseStatus(code= HttpStatus.OK)
    public ResponseEntity<List<Model>> getByNameAndCategoryModel(@PathVariable String name,@PathVariable String category){
        return ResponseEntity.ok(modelService.getByNameAndCategoryModel(name,category));
    }

    //Andando
    @DeleteMapping("/{id}")
    @ResponseStatus(code=HttpStatus.OK)
    public ResponseEntity deleteModel(@PathVariable Integer id){
        modelService.deleteByIdModel(id);
        return ResponseEntity.ok().build();
    }

}
