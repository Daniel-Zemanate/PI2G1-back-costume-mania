package com.costumemania.msproduct.controller;

import com.costumemania.msproduct.model.Category;
import com.costumemania.msproduct.model.CategoryDTO;
import com.costumemania.msproduct.model.StatusComponent;
import com.costumemania.msproduct.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // public - solo devuelve las categorias activas
    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok().body(categoryService.getAll());
    };

    // adm - devuelve categorias activas e inactivas
    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllComplete() {
        return ResponseEntity.ok().body(categoryService.getAllComplete());
    };

    // public - devuelve solo categoria activa
    @GetMapping("/{idCategory}")
    public ResponseEntity<Category> getdById (@PathVariable Integer idCategory) {
        // first verify if the ID exist
        Optional<Category> categoryProof = categoryService.getdById(idCategory);
        if (categoryProof.isEmpty() || categoryProof.get().getStatusCategory().getId()==2){
            return ResponseEntity.notFound().build();
        }
        // else...
        return ResponseEntity.ok().body(categoryProof.get());
    };
    // adm - devuelve la categoria sin importar si está activa o no
    @GetMapping("/adm/{idCategory}")
    public ResponseEntity<Category> AdmGetdById (@PathVariable Integer idCategory) {
        // first verify if the ID exist
        Optional<Category> categoryProof = categoryService.getdById(idCategory);
        if (categoryProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // else...
        return ResponseEntity.ok().body(categoryProof.get());
    };

    // adm - devuelve la categoria sin importar si está activa o no
    @GetMapping("/name/{categoryName}")
    public ResponseEntity<Category> getByName (@PathVariable String categoryName) {
        // first verify if the name exist
        Optional<Category> categoryProof = categoryService.getByName(categoryName);
        if (categoryProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // else...
        return ResponseEntity.ok().body(categoryProof.get());
    };

    // adm
    @PostMapping("/create")
    public ResponseEntity<Category> create (@RequestBody CategoryDTO c) {
        // verify if this category exists - 422
        Optional<Category> searchCategory = categoryService.getByName(c.getName());
        if (searchCategory.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        Category cat = new Category();
        cat.setName(c.getName());
        cat.setStatusCategory(new StatusComponent(1,"active"));
        return ResponseEntity.accepted().body(categoryService.create(cat));
    }

    // adm
    @PutMapping("/{idCategory}")
    public ResponseEntity<Category> update (@PathVariable Integer idCategory, @RequestBody CategoryDTO c) {
        // first verify if the ID exist
        Optional<Category> categoryProof = categoryService.getdById(idCategory);
        if (categoryProof.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // verify if this modified Category exists - 422 Unprocessable Content
        if (!Objects.equals(c.getName(), categoryProof.get().getName()))  {
            Optional<Category> searchNewCategory = categoryService.getByName(c.getName());
            if (searchNewCategory.isPresent()) {
                return ResponseEntity.unprocessableEntity().build();
            }
        }
        // create category
        Category cat = new Category();
        cat.setIdCategory(idCategory);
        cat.setName(c.getName());
        if (c.getStatus() == 1) {
            cat.setStatusCategory(new StatusComponent(1, "active"));
        } else if (c.getStatus() == 2) {
            cat.setStatusCategory(new StatusComponent(2, "inactive"));
        } else {
            return ResponseEntity.badRequest().build();
        }
        // else...
        return ResponseEntity.ok().body(categoryService.create(cat));
    }

    // adm - deshabilita categoria
    @PutMapping("/delete/{idCategory}")
    public ResponseEntity<Category> makeInactive (@PathVariable Integer idCategory) {
        // first verify if the ID exist
        Optional<Category> categoryProof = categoryService.getdById(idCategory);
        if (categoryProof.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // modify state
        categoryProof.get().setStatusCategory(new StatusComponent(2, "inactive"));
        return ResponseEntity.ok().body(categoryService.create(categoryProof.get()));
    }

    // adm - deprecated
    @DeleteMapping("/delete/{idCategory}")
    public ResponseEntity<String> delete (@PathVariable Integer idCategory) {
        // first verify if the ID exist
        Optional<Category> categoryProof = categoryService.getdById(idCategory);
        if (categoryProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // else...
        categoryService.delete(idCategory);
        return ResponseEntity.ok().body("Category item with ID " + idCategory + " deleted.");
    }
}
