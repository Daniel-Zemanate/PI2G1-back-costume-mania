package com.costumemania.msproduct.controller;

import com.costumemania.msproduct.model.Category;
import com.costumemania.msproduct.model.Model;
import com.costumemania.msproduct.model.ModelDTO;
import com.costumemania.msproduct.model.StatusComponent;
import com.costumemania.msproduct.service.CategoryService;
import com.costumemania.msproduct.service.ModelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
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

    // public - solo devuelve los modelos activos
    @GetMapping
    @ResponseStatus(code= HttpStatus.OK)
    public ResponseEntity<List<Model>> getAllModel(){
        return ResponseEntity.ok(modelService.getAllModel());
    }
    // adm - devuelve los modelos activos e inactivos
    @GetMapping("/all")
    public ResponseEntity<List<Model>> getAllComplete() {
        return ResponseEntity.ok().body(modelService.getAllComplete());
    };

    @GetMapping("/news/{limit}")
    public ResponseEntity<List<Model>> getNewsLimit(@PathVariable Integer limit) {
        return ResponseEntity.ok().body(modelService.getNewsLimit(limit));
    }

    // public - devuelve solo modelo activo
    @GetMapping("/{id}")
    public ResponseEntity<Model> getByIdModel(@PathVariable Integer id){
        // verify model empty
        Optional<Model>model= modelService.getByIdModel(id);
        if(model.isEmpty() || model.get().getStatusModel().getId()==2){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(model.get());
    }
    // adm - devuelve modelo sin importar si está activo o no
    @GetMapping("/adm/{id}")
    public ResponseEntity<Model> admGetByIdModel(@PathVariable Integer id){
        // verify model empty
        Optional<Model>model= modelService.getByIdModel(id);
        if(model.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(model.get());
    }

    // public - solo devuelve los activos
    @GetMapping("/name/{name}")
    public ResponseEntity<Optional<List<Model>>> getByNameModel(@PathVariable String name){
        // verify list by name empty
        Optional<List<Model>> model = modelService.getByNameModel(name);
        if(model.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(model);
    }
    // adm - devuelve modelo por NOMBRE EXACTO sin importar si está activo o no
    @GetMapping("/adm/name/{name}")
    public ResponseEntity<Model> admGetByNameModel(@PathVariable String name){
        // verify list by name empty
        Optional<Model> model = modelService.admGetByNameModel(name);
        if(model.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(model.get());
    }

    // public - devuelve solo los modelos activos
    @GetMapping("/category/id/{idCategory}")
    public ResponseEntity<List<Model>> getByIdCategory(@PathVariable Integer idCategory){
        // verify category empty and active
        Optional<Category> searchedCategory = categoryService.categorydById(idCategory);
        if(searchedCategory.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // verify list by category empty
        List<Model> listModel = modelService.getByIdCategoryModel(idCategory);
        if (listModel.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(listModel);
    }
    // adm - devuelve modelos activos e inactivos
    @GetMapping("/adm/category/id/{idCategory}")
    public ResponseEntity<List<Model>> admGetByIdCategory(@PathVariable Integer idCategory){
        // verify category empty
        Optional<Category> searchedCategory = categoryService.getdById(idCategory);
        if(searchedCategory.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // verify list by category empty
        List<Model> listModel = modelService.admGetByIdCategoryModel(idCategory);
        if (listModel.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(listModel);
    }

    // public - deprecated - gets every model by exact category name
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

    // public - devuelve solo los modelos activos
    @GetMapping("/name/{name}/category/id/{category}")
    public ResponseEntity<List<Model>> getByNameAndCategoryModel(@PathVariable String name,@PathVariable Integer category){
        // verify list by name empty
        Optional<List<Model>> seachrModel = modelService.getByNameModel(name);
        if(seachrModel.get().isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // verify category empty
        Optional<Category> searchedCategory = categoryService.categorydById(category);
        if(searchedCategory.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // verify final list
        List<Model> finalList = modelService.getByNameAndCategoryModel(name, category);
        if (finalList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(finalList);
    }

    // adm
    @PostMapping("/create")
    public ResponseEntity<Model> createModel(@RequestBody ModelDTO modelDTO){
        // verify if model exists - 422
        Optional<Model> searchModel = modelService.admGetByNameAndCategoryModel(modelDTO.getNameModel(), modelDTO.getCategory());
        if (searchModel.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
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
        modelCreated.setNameModel(modelDTO.getNameModel());
        modelCreated.setCategory(searchCategory.get());
        modelCreated.setUrlImage(modelDTO.getUrlImage());
        modelCreated.setStatusModel(new StatusComponent(1,"active"));
        return ResponseEntity.accepted().body(modelService.saveModel(modelCreated));
    }

    // adm
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
        // verify if this modified model exists - 422 Unprocessable Content
        if (!Objects.equals(modelDTO.getNameModel(), searchModel.get().getNameModel()) || !Objects.equals(modelDTO.getCategory(), searchModel.get().getCategory().getIdCategory()))  {
            Optional<Model> searchNewModel = modelService.admGetByNameAndCategoryModel(modelDTO.getNameModel(), modelDTO.getCategory());
            if (searchNewModel.isPresent()) {
                return ResponseEntity.unprocessableEntity().build();
            }
        }
        // create
        Model modelCreated = new Model();
        modelCreated.setIdModel(id);
        modelCreated.setNameModel(modelDTO.getNameModel());
        modelCreated.setCategory(searchCategory.get());
        modelCreated.setUrlImage(modelDTO.getUrlImage());
        if (modelDTO.getStatus() == 1) {
            modelCreated.setStatusModel(new StatusComponent(1, "active"));
        } else if (modelDTO.getStatus() == 2) {
            modelCreated.setStatusModel(new StatusComponent(2, "inactive"));
        } else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.accepted().body(modelService.saveModel(modelCreated));
    }

    // adm - deshabilita modelo
    @PutMapping("/delete/{idModel}")
    public ResponseEntity<Model> makeInactive (@PathVariable Integer idModel) {
        // first verify if the ID exist
        Optional<Model> modelProof = modelService.getByIdModel(idModel);
        if (modelProof.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // modify state
        modelProof.get().setStatusModel(new StatusComponent(2, "inactive"));
        return ResponseEntity.ok().body(modelService.saveModel(modelProof.get()));
    }
    // adm - deshabilita catalogo por categoria
    @PutMapping("/deleteByC/{idCategory}")
    public ResponseEntity<String> makeInactivByCat (@PathVariable Integer idCategory) {
        Optional<Category> categoryProof = categoryService.getdById(idCategory);
        if (categoryProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // modify state
        modelService.inactiveByCategory(idCategory);
        return ResponseEntity.ok().body("Every models within category " + idCategory + " disabled");
    }

    // adm - deprecated
/*    @DeleteMapping("/delete/{idModel}")
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
    // adm - deprecated
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
    }*/
}