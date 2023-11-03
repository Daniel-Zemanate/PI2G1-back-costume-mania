package com.costumemania.mscatalog.controller;

import com.costumemania.mscatalog.client.ModelFeign;
import com.costumemania.mscatalog.model.Catalog;
import com.costumemania.mscatalog.model.Size;
import com.costumemania.mscatalog.service.CatalogService;
import com.costumemania.mscatalog.service.SizeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/catalog")
public class CatalogController {
    private final CatalogService catalogService;
    private final SizeService sizeService;
    private final ModelFeign modelFeign;

    public CatalogController(CatalogService catalogService, SizeService sizeService, ModelFeign modelFeign) {
        this.catalogService = catalogService;
        this.sizeService = sizeService;
        this.modelFeign = modelFeign;
    }

    @GetMapping
    public ResponseEntity<List<Catalog>> getAll(){
        return ResponseEntity.ok().body(catalogService.getCatalog());
    }

    @GetMapping("/page/{page}")
    public Page<Catalog> getAll(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 20);
        return catalogService.getCatalog(pageable);
    }

    @GetMapping("/{idCatalog}")
    public ResponseEntity<Optional<Catalog>> getById(@PathVariable Integer idCatalog){

        // first verify if the ID exist
        Optional<Catalog> catalogProof = catalogService.getCatalogById(idCatalog);
        if (catalogProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        // else...
        return ResponseEntity.ok().body(catalogProof);
    }
    //       A REVISAR!
    @GetMapping("/model-name/{nameModel}")
    public ResponseEntity<List<Catalog>> getByNameModel( @PathVariable String nameModel){
        // first verify if the ID exist
        var model = modelFeign.getByNameModel(nameModel);
        if(model.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<Catalog> catalogProof = catalogService.getCatalogByNameModel(nameModel);
        if (catalogProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // else...
        return ResponseEntity.ok().body(catalogProof);
    }

    //       A REVISAR!
    @GetMapping("/model-id/{idModel}")
    public ResponseEntity<List<Catalog>> getByIdModel( @PathVariable Integer idModel){
        // first verify if the ID exist
        var model = modelFeign.getByIdModel(idModel);
        if(model.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<Catalog> catalogProof = catalogService.getCatalogByIdModel(idModel);
        if (catalogProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // else...
        return ResponseEntity.ok().body(catalogProof);
    }
//       A REVISAR!
    @GetMapping("/catalog-id/{idCatalog}/model-name/{nameModel}")
    public ResponseEntity<List<Catalog>> getByIdCategoryNameModel(@PathVariable Integer idCategory, @PathVariable String nameModel){
        Optional<List<ModelFeign.Model>> model = modelFeign.getByNameModel(nameModel);
        if(model.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<Catalog> catalogProof = catalogService.getCatalogByIDCategoryNameModel(idCategory,nameModel);
        if (catalogProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // else...
        return ResponseEntity.ok().body(catalogProof);
    }

    @GetMapping("/bySize/{bolleanAdult}")
    public ResponseEntity<List<List<Catalog>>> getBySize(@PathVariable Integer bolleanAdult){

        // first verify if the bollean is correct
        List<Size> sizeList = new ArrayList<>();
        sizeList = sizeService.getByAdult(bolleanAdult);

        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // else...
        List<List<Catalog>> result = new ArrayList<>();

        for (int i=0; i < sizeList.size(); i++) {
            List<Catalog> listBySize = new ArrayList<>();
            listBySize = catalogService.getCatalogBySize(sizeList.get(i));
            result.add(listBySize);
        }
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/news")
    public ResponseEntity<List<Catalog>> getNews(){
        return ResponseEntity.ok().body(catalogService.getNews());
    }

    @PutMapping("{idCatalog}")
    public ResponseEntity<Catalog> catalogSold(@PathVariable Integer idCatalog, @Param("quantity") Integer quantity) {
        // verify ID
        Optional<Catalog> searchModel = catalogService.getCatalogById(idCatalog);
        if(searchModel.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // verify quantity
        Catalog catalog = catalogService.getCatalogByIdSEC(idCatalog);
        if(catalog.getQuantity()-quantity<0){
            return ResponseEntity.badRequest().build();
        }
        //else...
        catalog.setQuantity(catalog.getQuantity()-quantity);
        return ResponseEntity.accepted().body(catalogService.save(catalog));
    }

    @DeleteMapping("/{idCatalog}")
    public ResponseEntity<String> delete(@PathVariable Integer idCatalog) {
        // first verify if the ID exist
        Optional<Catalog> catalogProof = catalogService.getCatalogById(idCatalog);
        if (catalogProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        // else...
        catalogService.delete(idCatalog);
        return ResponseEntity.ok().body("Catalog item with ID " + idCatalog + " deleted");
    }


}
