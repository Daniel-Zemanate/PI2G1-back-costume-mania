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
        // deleting all the models and catalog items related to the category
        // verify if there are results

        /*Optional<List<Catalog>> catalogProof = catalogService.getCatalogByModel(idModel);
        if (catalogProof.get().isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // else
        catalogService.deleteByModel(idModel);
        return ResponseEntity.ok().body("Catalog items with ID model " + idModel + " deleted");*/

        // else...
        categoryService.delete(idCategory);
        return ResponseEntity.ok().body("Category item with ID " + idCategory + " deleted with all the related catalog");
        // hacer vinculacion con catalogo para borrar categoria
        /* 1. borrar todos los modelos de esa categoria
        2. en modelo, eliminar to-do el catalogo de ese modelo
        3. crear un nuevo endpoint que use una query para eliminar to-do junto
         */
    }
}
