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
        // verify category
        Optional<Category> searchCategory = categoryService.getdById(modelDTO.getCategory());
        if(searchCategory.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // verify name empty
        if(modelDTO.getNameModel().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        // create
        Model modelCreated = new Model();
        modelCreated.setNameModel(modelDTO.getNameModel());
        modelCreated.setCategory(categoryService.categorydById(modelDTO.getCategory()));
        modelCreated.setUrlImage(modelDTO.getUrlImage());
        return ResponseEntity.accepted().body(modelService.saveModel(modelCreated));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Model> updateModel(@PathVariable Integer id, @RequestBody ModelDTO modelDTO){
        // verify ID
        Optional<Model> searchModel = modelService.getByIdModel(id);
        if(searchModel.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // verify category
        Optional<Category> searchCategory = categoryService.getdById(modelDTO.getCategory());
        if(searchCategory.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // verify name empty
        if(modelDTO.getNameModel().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        // create
        Model modelCreated = new Model();
        modelCreated.setIdModel(id);
        modelCreated.setNameModel(modelDTO.getNameModel());
        modelCreated.setCategory(categoryService.categorydById(modelDTO.getCategory()));
        modelCreated.setUrlImage(modelDTO.getUrlImage());
        return ResponseEntity.accepted().body(modelService.saveModel(modelCreated));
    }

    @GetMapping
    @ResponseStatus(code= HttpStatus.OK)
    public ResponseEntity<List<Model>> getAllModel(){
        return ResponseEntity.ok(modelService.getAllModel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Model>>getByIdModel(@PathVariable Integer id){
        // verify model empty
        Optional<Model>model= modelService.getByIdModel(id);
        if(model.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(model);
    }

    @GetMapping("/SEC/{id}")
    public ResponseEntity<Model> getByIdModelSEC (@PathVariable Integer id){
        return ResponseEntity.ok().body(modelService.getByIdModelSEC(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Optional<List<Model>>> getByNameModel(@PathVariable String name){
        // verify list by name empty
        Optional<List<Model>> model = modelService.getByNameModel(name);
        if(model.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(model);
    }

    @GetMapping("/category/id/{idCategory}")
    public ResponseEntity<List<Model>> getByIdCategory(@PathVariable Integer idCategory){
        // verify category empty
        Optional<Category> searchedCategory = categoryService.getdById(idCategory);
        if(searchedCategory.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // verify list by category empty
        List<Model> listModel = modelService.getByIdCategoryModel(idCategory);
        if (listModel.isEmpty()){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(listModel);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Model>> getByCategory(@PathVariable String category){
        // verify category empty
        Optional<Category> searchedCategory = categoryService.getByName(category);
        if(searchedCategory.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        // verify list by category empty
        List<Model> listModel = modelService.getByCategoryModel(category);
        if (listModel.isEmpty()){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(listModel);
    }

    @GetMapping("/name/{name}/category/{category}")
    public ResponseEntity<List<Model>> getByNameAndCategoryModel(@PathVariable String name,@PathVariable Integer category){
        // verify list by name empty
        Optional<List<Model>> model = modelService.getByNameModel(name);
        if(model.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // verify category empty
        Optional<Category> searchedCategory = categoryService.getdById(category);
        if(searchedCategory.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // verify list by category empty
        List<Model> listModel = modelService.getByIdCategoryModel(category);
        if (listModel.isEmpty()){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(modelService.getByNameAndCategoryModel(name,category));
    }

    // DEJO DE ANDAR LA CONEXION CON CATALOGO!!!

    @DeleteMapping("/delete/{idModel}")
    public ResponseEntity<String> deleteModel(@PathVariable Integer idModel){
        // verify model empty
        Optional<Model> model =modelService.getByIdModel(idModel);
        if(model.isEmpty()){
           return ResponseEntity.notFound().build();
        }
        // deleting model
        modelService.deleteModel(idModel);
        return ResponseEntity.ok().body("Model item with ID " + idModel + " deleted.");
    }

    @DeleteMapping("/deleteByCategory/{idCategory}")
    public ResponseEntity<String> deleteModelByCategory(@PathVariable Integer idCategory){
        // verify if there are results
        List<Model> modelProof = modelService.getByIdCategoryModel(idCategory);
        if(modelProof.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // deleting model
        modelService.deleteModelByCat(idCategory);
        return ResponseEntity.ok().body("Model items from Category ID " + idCategory + " deleted.");
    }
}