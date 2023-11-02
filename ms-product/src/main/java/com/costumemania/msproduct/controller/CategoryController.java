package com.costumemania.msproduct.controller;

import com.costumemania.msproduct.model.Category;
import com.costumemania.msproduct.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok().body(categoryService.getAll());
    };
    @GetMapping("/{idCategory}")
    public ResponseEntity<Category> getdById (@PathVariable Integer idCategory) {
        // first verify if the ID exist
        Optional<Category> categoryProof = categoryService.getdById(idCategory);
        if (categoryProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // else...
        return ResponseEntity.ok().body(categoryService.categorydById(idCategory));
    };

    @GetMapping("/name/{categoryName}")
    public ResponseEntity<Object> getByName (@PathVariable String categoryName) {
        // first verify if the name exist
        Optional<Category> categoryProof = categoryService.getByName(categoryName);
        if (categoryProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // else...
        return ResponseEntity.ok().body(categoryProof);
    };

    @PostMapping("/create")
    public ResponseEntity<Category> create (@RequestBody Category c) {
        return ResponseEntity.accepted().body(categoryService.create(c));
    }

    @PutMapping("/{idCategory}")
    public ResponseEntity<Category> update (@PathVariable Integer idCategory, @RequestBody Category c) {
        // first verify if the ID exist
        Optional<Category> categoryProof = categoryService.getdById(idCategory);
        if (categoryProof.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        c.setIdCategory(categoryProof.get().getIdCategory());
        return ResponseEntity.ok().body(categoryService.create(c));
    }

    @DeleteMapping("/delete/{idCategory}")
    public ResponseEntity<Object> delete (@PathVariable Integer idCategory) {
        // first verify if the ID exist
        Optional<Category> categoryProof = categoryService.getdById(idCategory);
        if (categoryProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // else...
        categoryService.delete(idCategory);
        return ResponseEntity.ok().body("Category item with ID " + idCategory + " deleted");
        // hacer vinculacion con catalogo para borrar categoria
    }
}
