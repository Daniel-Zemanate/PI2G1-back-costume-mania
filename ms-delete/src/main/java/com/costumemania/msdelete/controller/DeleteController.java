package com.costumemania.msdelete.controller;

import com.costumemania.msdelete.model.Model;
import com.costumemania.msdelete.service.CatalogService;
import com.costumemania.msdelete.service.ModelService;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("/deleteModel/{idModel}")
    public ResponseEntity<String> deleteModel(HttpServletRequest request, @PathVariable Integer idModel){
        String authorizationHeader = request.getHeader("Authorization");
        String message;
        // deleting catalog
        try {
            message = catalogService.makeInactivByModel(authorizationHeader, idModel).getBody();
        } catch (FeignException e){
            message = "We couldn´t verify catalog";
        }
        // deleting model
        try {
            ResponseEntity result = modelService.makeInactive(authorizationHeader, idModel);
            if (result.getStatusCode()== HttpStatus.OK) {
                message = "Model with id " + idModel + " disabled. " + message;
            }
            else {
                message = "We couldn´t verify model, " + message;
            }
        } catch (FeignException e){
            message = "We couldn´t verify model, " + message;
        }
        // else it´s a 200
        return ResponseEntity.ok().body(message);
    }

    @PutMapping("/deleteCategory/{idCategory}")
    public ResponseEntity<String> deleteCategory(HttpServletRequest request, @PathVariable Integer idCategory){
        String authorizationHeader = request.getHeader("Authorization");
        String message;
        // deleting catalog
        try {
            message = catalogService.makeInactivByCat(authorizationHeader, idCategory).getBody();
        } catch (FeignException e){
            message = "We couldn´t verify catalog in category";
        }
        // deleting model
        try {
            ResponseEntity result = modelService.makeInactivByCat(authorizationHeader, idCategory);
            if (result.getStatusCode()== HttpStatus.OK) {
                message = result.getBody() + ", " + message;
            }
            else {
                message = "We couldn´t verify models in category, " + message;
            }
        } catch (FeignException e){
            message = "We couldn´t verify models in category, " + message;
        }
        try {
            ResponseEntity result = modelService.makeInactiveCat(authorizationHeader, idCategory);
            if (result.getStatusCode()== HttpStatus.OK) {
                message = "Category with id " + idCategory + " disabled, " + message;
            }
            else {
                message = "We couldn´t verify category " + idCategory + ", " + message;
            }
        } catch (FeignException e){
            message = "We couldn´t verify category " + idCategory + ", " + message;
        }
        // else it´s a 200
        return ResponseEntity.ok().body(message);
    }
}
