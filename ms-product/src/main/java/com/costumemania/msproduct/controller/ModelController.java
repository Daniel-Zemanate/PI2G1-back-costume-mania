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
@RequestMapping("/api/v1/model")
public class ModelController {

    private final ModelService modelService;
    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    // AJUSTAR EL CREATE PORQUE NO ANDA!
    @PostMapping("/create")
    @ResponseStatus(code= HttpStatus.CREATED)
    public ResponseEntity<Model> createModel(@RequestBody Model model){
        modelService.saveModel(model.getNameModel(), model.getCategory().getIdCategory(), model.getUrlImage());
        return ResponseEntity.ok(model);
    }

    // HASTA QUE NO ANDE EL CREATE, no va a andar este.
    @PutMapping
    @ResponseStatus(code=HttpStatus.OK)
    public ResponseEntity updateModel(@RequestBody Model model){
        modelService.updateModel(model);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ResponseStatus(code= HttpStatus.OK)
    public ResponseEntity<List<Model>> getAllModel(){
        return ResponseEntity.ok(modelService.getAllModel());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Optional<Model>>getByIdModel(@PathVariable Integer id){
        Optional<Model>model= modelService.getByIdModel(id);
        if(model.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(model);
    }


    @GetMapping("/name/{name}")
    public ResponseEntity<Optional<Model>> getByNameModel(@PathVariable String name){
        Optional<Model> model = modelService.getByNameModel(name);
        if(model.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(model);
    }

    //MODIFICAR!! agregar validación para que use la API de categoria para validar si existe. si no encuentra una categoria, devuelva un 404 (y no un 200)
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Model>> getByCategoryModel(@PathVariable String category){
        List<Model> model = modelService.getByCategoryModel(category);
        if (model.isEmpty()){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    //MODIFICAR!! agregar validación para que si no encuentra un modelo, devuelva un 404 (y no un 200)
    //MODIFICAR!! agregar validación para que use la API de categoria para validar si existe. si no encuentra una categoria, devuelva un 404 (y no un 200)
    @GetMapping("/name/{name}/category/{category}")
    @ResponseStatus(code= HttpStatus.OK)
    public ResponseEntity<List<Model>> getByNameAndCategoryModel(@PathVariable String name,@PathVariable String category){
        return ResponseEntity.ok(modelService.getByNameAndCategoryModel(name,category));
    }

    //MODIFICAR!!
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteModel(@PathVariable Integer id){
        Optional<Model> model =modelService.getByIdModel(id);
        if(model.isEmpty()){
           return ResponseEntity.notFound().build();
        }
        modelService.deleteByIdModel(id);
        return ResponseEntity.ok().body("Model item with ID " + id + " deleted");
    }

}
