package com.costumemania.msproduct.controller;

import com.costumemania.msproduct.model.Category;
import com.costumemania.msproduct.model.Model;
import com.costumemania.msproduct.model.ModelDTO;
import com.costumemania.msproduct.service.CategoryService;
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
    private final CategoryService categoryService;
    public ModelController(ModelService modelService, CategoryService categoryService) {
        this.modelService = modelService;
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<Model> createModel(@RequestBody ModelDTO modelDTO){
        Optional<Category> searchCategory = categoryService.getdById(modelDTO.getCategory());
        if(searchCategory.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        if(modelDTO.getNameModel().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        Model modelCreated = new Model();
        modelCreated.setNameModel(modelDTO.getNameModel());
        modelCreated.setCategory(categoryService.categorydById(modelDTO.getCategory()));
        modelCreated.setUrlImage(modelDTO.getUrlImage());
        return ResponseEntity.accepted().body(modelService.saveModel(modelCreated));
    }

    // HASTA QUE NO ANDE EL CREATE, no va a andar este.
    @PutMapping("/{id}")
    public ResponseEntity<Model> updateModel(@PathVariable Integer id, @RequestBody Model model){
        Optional<Model> searchModel = modelService.getByIdModel(id);
        if(searchModel.isEmpty()){
            return ResponseEntity.notFound().build();
        }
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
    @GetMapping("/category/id/{idCategory}")
    public ResponseEntity<List<Model>> getByIdCategory(@PathVariable Integer idCategory){
        Optional<Category> searchedCategory = categoryService.getdById(idCategory);
        if(searchedCategory.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<Model> listModel = modelService.getByIdCategoryModel(idCategory);
        if (listModel.isEmpty()){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(listModel);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Model>> getByCategory(@PathVariable String category){
        Optional<Category> searchedCategory = categoryService.getByName(category);
        if(searchedCategory.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        List<Model> listModel = modelService.getByCategoryModel(category);
        if (listModel.isEmpty()){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(listModel);
    }

    //MODIFICAR!! agregar validación para que si no encuentra un modelo, devuelva un 404 (y no un 200)
    //MODIFICAR!! agregar validación para que use la API de categoria para validar si existe. si no encuentra una categoria, devuelva un 404 (y no un 200)
    @GetMapping("/name/{name}/category/{category}")
    @ResponseStatus(code= HttpStatus.OK)
    public ResponseEntity<List<Model>> getByNameAndCategoryModel(@PathVariable String name,@PathVariable String category){
        return ResponseEntity.ok(modelService.getByNameAndCategoryModel(name,category));
    }


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
