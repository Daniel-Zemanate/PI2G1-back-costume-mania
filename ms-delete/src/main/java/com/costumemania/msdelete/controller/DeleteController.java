package com.costumemania.msdelete.controller;

import com.costumemania.msdelete.model.Model;
import com.costumemania.msdelete.service.CatalogService;
import com.costumemania.msdelete.service.ModelService;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/deleteMS")
public class DeleteController {

    private final ModelService modelService;
    private final CatalogService catalogService;
    public DeleteController(ModelService modelService, CatalogService catalogService) {
        this.modelService = modelService;
        this.catalogService = catalogService;
    }

    @DeleteMapping("/deleteModel/{idModel}")
    public ResponseEntity<String> deleteModel(@PathVariable Integer idModel){

        String message = ", and all the related catalog to that model.";
        // deleting catalog. If there isn´t any, continue with model
        try {
            catalogService.deleteByModel(idModel);
        }
        catch (FeignException e){
            message = "";
        }
        // deleting model. If there isn´t any, it´s a 404
        try {
            modelService.deleteModel(idModel);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // else it´s a 200
        return ResponseEntity.ok().body("Model item with ID " + idModel + " deleted" + message);
    }

    @DeleteMapping("/deleteCategory/{idCategory}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer idCategory){

        // deleting catalog. If there isn´t any, continue with model
        String message1 = " and catalog items";
        try {
            ResponseEntity<List<Model>> response = modelService.getByIdCategory(idCategory);
            if (response.getBody().size() > 0) {
                for (int i = 0; i < response.getBody().size(); i++) {
                    try {
                        catalogService.deleteByModel(response.getBody().get(i).getIdModel());
                    } catch (FeignException e) {
                        System.out.println("there isn´t catalog of model " + response.getBody().get(i).getIdModel());
                    }
                }
            }
        }
        catch (FeignException e){
            message1 = "";
        }
        // deleting model. If there isn´t any, continue with category
        String message2 = ", and all the related models";
        try {
            modelService.deleteModelbyCat(idCategory);
        }
        catch (FeignException e){
            message2 = "";
        }
        // deleting category. If there isn´t any, it´s a 404
        try {
            modelService.delete(idCategory);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // else it´s a 200
        return ResponseEntity.ok().body("Category with ID " + idCategory + " deleted" + message2 + message1);
    }
}
