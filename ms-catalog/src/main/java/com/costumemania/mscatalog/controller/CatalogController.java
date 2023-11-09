package com.costumemania.mscatalog.controller;

import com.costumemania.mscatalog.model.Catalog;
import com.costumemania.mscatalog.model.CatalogDTO;
import com.costumemania.mscatalog.model.Model;
import com.costumemania.mscatalog.model.Size;
import com.costumemania.mscatalog.service.CatalogService;
import com.costumemania.mscatalog.service.ModelService;
import com.costumemania.mscatalog.service.SizeService;
import feign.FeignException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final ModelService modelService;
    public CatalogController(CatalogService catalogService, SizeService sizeService, ModelService modelService) {
        this.catalogService = catalogService;
        this.sizeService = sizeService;
        this.modelService = modelService;
    }

    //////////////---------- TODO EL CATALOGO ----------//////////////

    @GetMapping
    public ResponseEntity<List<Catalog>> getAll(){
        return ResponseEntity.ok().body(catalogService.getCatalog());
    }

    @GetMapping("/page/{page}")
    public Page<Catalog> getAll(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 12);
        return catalogService.getCatalog(pageable);
    }

    /////////////////////////////////////////////////////////////////

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


//////////////---------- CATALOGO FILTRADO ----------//////////////

    @GetMapping("/bySize/{bolleanAdult}")
    public ResponseEntity<List<Catalog>> getBySize(@PathVariable Integer bolleanAdult){

        // first verify if the bollean is correct
        List<Size> sizeList = new ArrayList<>();
        sizeList = sizeService.getByAdult(bolleanAdult);

        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Catalog> result = new ArrayList<>();

        for (int i=0; i < sizeList.size(); i++) {
            List<Catalog> listBySize = new ArrayList<>();
            listBySize = catalogService.getCatalogBySize(sizeList.get(i));
            for (int j=0; j < listBySize.size(); j++) {
                result.add(listBySize.get(j));
            }
        }
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/byModel/{idModel}")
    public ResponseEntity<Optional<List<Catalog>>> getByModel(@PathVariable Integer idModel){

        // first verify if the model exists with feign
        try {
            ResponseEntity<Optional<Model>> response = modelService.getByIdModel(idModel);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // else...
        return ResponseEntity.ok().body(catalogService.getCatalogByModel(idModel));
    }

    @GetMapping("/byCategory/{idCategory}")
    public ResponseEntity<List<Catalog>> getByCategory(@PathVariable Integer idCategory){

        // first verify if the category exists with feign
        try {
            modelService.getCategorydById(idCategory);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // get every model within the category
        List<Catalog> result = new ArrayList<>();
        try {
            List<Model> allModels = modelService.getModelByIdCategory(idCategory).getBody();
            if (allModels.size() > 0) {
                for (int i = 0; i < allModels.size(); i++) {
                    try {
                        Optional<List<Catalog>> list = catalogService.getCatalogByModel(allModels.get(i).getIdModel());
                        if (!list.get().isEmpty()) {
                            for (int j = 0; j < list.get().size(); j++) {
                                result.add(list.get().get(j));
                            }
                        }
                    } catch (FeignException e) {
                        System.out.println("there isn´t catalog of model " + allModels.get(i).getIdModel());
                    }
                }
            }
        } catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // else...
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/byKeyWord/{keyWord}")
    public ResponseEntity<List<Catalog>> getByKeyWord(@PathVariable String keyWord){

        List<Optional<List<Model>>> modelList = new ArrayList<>();
        // first verify if exists any model with feign
        try {
            Optional<List<Model>> modelByName = modelService.getByNameModel(keyWord).getBody();
            if (!modelByName.get().isEmpty()) modelList.add(modelByName);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // get every catalog with the keyword
        List<Catalog> finalResult = new ArrayList<>();
        try {
            if (!modelList.isEmpty()) {
                if (modelList.get(0).get().size() > 0) {
                    for (int i = 0; i < modelList.get(0).get().size(); i++) {
                        try {
                            Optional<List<Catalog>> result = catalogService.getCatalogByModel(modelList.get(0).get().get(i).getIdModel());
                            if (!result.get().isEmpty()) {
                                for (int j = 0; j < result.get().size(); j++) {
                                    finalResult.add(result.get().get(j));
                                }
                            }
                        } catch (FeignException e) {
                            System.out.println("there isn´t catalog of model " + modelList.get(0).get().get(i).getIdModel());
                        }
                    }
                }
            }
        } catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // else...
        return ResponseEntity.ok().body(finalResult);
    }

    @GetMapping("/byKeyWord/{keyWord}/byCategory/{idCategory}")
    public ResponseEntity<List<Catalog>> getByKeyWordByCategory(@PathVariable String keyWord, @PathVariable Integer idCategory){

        List<List<Model>> modelList = new ArrayList<>();
        // first verify if exists any model with feign
        try {
            List<Model> searchModel = modelService.getModelByNameAndIdCategory(keyWord,idCategory).getBody();
            if (!searchModel.isEmpty()) modelList.add(searchModel);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // get every catalog with the keyword ant category
        List<Catalog> finalResult = new ArrayList<>();
        try {
            if (!modelList.isEmpty()) {
                if (modelList.get(0).size() > 0) {
                    for (int i = 0; i < modelList.get(0).size(); i++) {
                        try {
                            Optional<List<Catalog>> result = catalogService.getCatalogByModel(modelList.get(0).get(i).getIdModel());
                            if (!result.get().isEmpty()) {
                                for (int j = 0; j < result.get().size(); j++) {
                                    finalResult.add(result.get().get(j));
                                }
                            }
                        } catch (FeignException e) {
                            System.out.println("there isn´t catalog of model " + modelList.get(0).get(i).getIdModel());
                        }
                    }
                }
            }
        } catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // else...
        return ResponseEntity.ok().body(finalResult);
    }

    @GetMapping("/byCategory/{idCategory}/bySize/{bolleanAdult}")
    public ResponseEntity<List<Optional<Catalog>>> getByCategoryAndSize(@PathVariable Integer idCategory, @PathVariable Integer bolleanAdult){

        // first verify if the category exists with feign
        try {
            modelService.getCategorydById(idCategory);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // get every model within the category
        List<Model> modelList = new ArrayList<>();
        try {
            modelList = modelService.getModelByIdCategory(idCategory).getBody();
        } catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // second verify if the bollean is correct
        List<Size> sizeList = new ArrayList<>();
        sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Optional<Catalog>> finalList = new ArrayList<>();
        if (!modelList.isEmpty()) {
            if (modelList.size() > 0) {
                for (int i = 0; i < modelList.size(); i++) {
                    for (int j=0; j < sizeList.size(); j++) {
                        Optional<Catalog> result = catalogService.findByModelAndSize(modelList.get(i).getIdModel(),sizeList.get(j).getId());
                        if (!result.isEmpty()) finalList.add(result);
                    }
                }

            }
        }
        return ResponseEntity.ok().body(finalList);
    }

    @GetMapping("/byKeyWord/{keyWord}/bySize/{bolleanAdult}")
    public ResponseEntity<List<Optional<Catalog>>> getByKeyWordAndSize(@PathVariable String keyWord, @PathVariable Integer bolleanAdult){

        List<Optional<List<Model>>> modelList = new ArrayList<>();
        // first verify if exists any model with feign
        try {
            Optional<List<Model>> modelByName = modelService.getByNameModel(keyWord).getBody();
            if (!modelByName.get().isEmpty()) modelList.add(modelByName);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // second verify if the bollean is correct
        List<Size> sizeList = new ArrayList<>();
        sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Optional<Catalog>> finalList = new ArrayList<>();
        if (!modelList.isEmpty()) {
            if (modelList.get(0).get().size() > 0) {
                for (int i = 0; i < modelList.get(0).get().size(); i++) {
                    for (int j=0; j < sizeList.size(); j++) {
                        Optional<Catalog> result = catalogService.findByModelAndSize(modelList.get(0).get().get(i).getIdModel(),sizeList.get(j).getId());
                        if (!result.isEmpty()) finalList.add(result);
                    }
                }

            }
        }
        return ResponseEntity.ok().body(finalList);
    }

    @GetMapping("/byKeyWord/{keyWord}/byCategory/{idCategory}/bySize/{bolleanAdult}")
    public ResponseEntity<List<Optional<Catalog>>> getByKeyWordByCategoryBySize(@PathVariable String keyWord, @PathVariable Integer idCategory, @PathVariable Integer bolleanAdult) {

        List<List<Model>> modelList = new ArrayList<>();
        // first verify if exists any model with feign
        try {
            List<Model> searchModel = modelService.getModelByNameAndIdCategory(keyWord, idCategory).getBody();
            if (!searchModel.isEmpty()) modelList.add(searchModel);
        } catch (FeignException e) {
            return ResponseEntity.notFound().build();
        }
        // second verify if the bollean is correct
        List<Size> sizeList = new ArrayList<>();
        sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Optional<Catalog>> finalList = new ArrayList<>();
        if (!modelList.isEmpty()) {
            if (modelList.get(0).size() > 0) {
                for (int i = 0; i < modelList.get(0).size(); i++) {
                    for (int j=0; j < sizeList.size(); j++) {
                        Optional<Catalog> result = catalogService.findByModelAndSize(modelList.get(0).get(i).getIdModel(),sizeList.get(j).getId());
                        if (!result.isEmpty()) finalList.add(result);
                    }
                }

            }
        }
        return ResponseEntity.ok().body(finalList);
    }

    /////////////////////////////////////////////////////////////////

    //////////////----------  CATALOGO FILTRADO CON PAGINADO----------//////////////

    @GetMapping("/bySize/{bolleanAdult}/page/{page}")
    public ResponseEntity<Page<Catalog>> getBySizePageable(@PathVariable Integer bolleanAdult,@PathVariable Integer page){

        // first verify if the bollean is correct
        List<Size> sizeList = new ArrayList<>();
        sizeList = sizeService.getByAdult(bolleanAdult);

        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Catalog> result = new ArrayList<>();

        for (int i=0; i < sizeList.size(); i++) {
            List<Catalog> listBySize = new ArrayList<>();
            listBySize = catalogService.getCatalogBySize(sizeList.get(i));
            for (int j=0; j < listBySize.size(); j++) {
                result.add(listBySize.get(j));
            }
        }
        // else...
        //pagination
        int pageSize = 12;
        int totalItems = result.size();

        Pageable pageable = PageRequest.of(page, pageSize);

        if (pageable.getOffset() > totalItems) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Catalog> pageResult = result.subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));

        Page<Catalog> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);

        return ResponseEntity.ok().body(catalogPage);
    }

    @GetMapping("/byModel/{idModel}/page/{page}")
    public ResponseEntity<Optional<Page<Catalog>>> getByModelPageable(@PathVariable Integer idModel,@PathVariable Integer page){

        // first verify if the model exists with feign
        try {
            ResponseEntity<Optional<Model>> response = modelService.getByIdModel(idModel);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // else...
        Pageable pageable = PageRequest.of(page, 12);

        Optional<Page<Catalog>> catalogPage = catalogService.getCatalogByModel(idModel,pageable);

        return ResponseEntity.ok().body(catalogPage);

    }

    @GetMapping("/byCategory/{idCategory}/page/{page}")
    public ResponseEntity<Page<Catalog>> getByCategoryPageable(@PathVariable Integer idCategory,@PathVariable Integer page){

        // first verify if the category exists with feign
        try {
            modelService.getCategorydById(idCategory);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // get every model within the category
        List<Catalog> result = new ArrayList<>();
        try {
            List<Model> allModels = modelService.getModelByIdCategory(idCategory).getBody();
            if (allModels.size() > 0) {
                for (int i = 0; i < allModels.size(); i++) {
                    try {
                        Optional<List<Catalog>> list = catalogService.getCatalogByModel(allModels.get(i).getIdModel());
                        if (!list.get().isEmpty()) {
                            for (int j = 0; j < list.get().size(); j++) {
                                result.add(list.get().get(j));
                            }
                        }
                    } catch (FeignException e) {
                        System.out.println("there isn´t catalog of model " + allModels.get(i).getIdModel());
                    }
                }
            }
        } catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // else...
        //pagination
        int pageSize = 12;
        int totalItems = result.size();

        Pageable pageable = PageRequest.of(page, pageSize);

        if (pageable.getOffset() > totalItems) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Catalog> pageResult = result.subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));

        Page<Catalog> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);

        return ResponseEntity.ok().body(catalogPage);
    }


    @GetMapping("/byKeyWord/{keyWord}/page/{page}")
    public ResponseEntity<Page<Catalog>> getByKeyWordPageable(@PathVariable String keyWord,@PathVariable Integer page){

        List<Optional<List<Model>>> modelList = new ArrayList<>();
        // first verify if exists any model with feign
        try {
            Optional<List<Model>> modelByName = modelService.getByNameModel(keyWord).getBody();
            if (!modelByName.get().isEmpty()) modelList.add(modelByName);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // get every catalog with the keyword
        List<Catalog> finalResult = new ArrayList<>();
        try {
            if (!modelList.isEmpty()) {
                if (modelList.get(0).get().size() > 0) {
                    for (int i = 0; i < modelList.get(0).get().size(); i++) {
                        try {
                            Optional<List<Catalog>> result = catalogService.getCatalogByModel(modelList.get(0).get().get(i).getIdModel());
                            if (!result.get().isEmpty()) {
                                for (int j = 0; j < result.get().size(); j++) {
                                    finalResult.add(result.get().get(j));
                                }
                            }
                        } catch (FeignException e) {
                            System.out.println("there isn´t catalog of model " + modelList.get(0).get().get(i).getIdModel());
                        }
                    }
                }
            }
        } catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // else...
        //pagination
        int pageSize = 12;
        int totalItems = finalResult.size();

        Pageable pageable = PageRequest.of(page, pageSize);

        if (pageable.getOffset() > totalItems) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Catalog> pageResult = finalResult.subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));

        Page<Catalog> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);

        return ResponseEntity.ok().body(catalogPage);
    }


    @GetMapping("/byKeyWord/{keyWord}/byCategory/{idCategory}/page/{page}")
    public ResponseEntity<Page<Catalog>> getByKeyWordByCategoryPageable(@PathVariable String keyWord, @PathVariable Integer idCategory,@PathVariable Integer page){

        List<List<Model>> modelList = new ArrayList<>();
        // first verify if exists any model with feign
        try {
            List<Model> searchModel = modelService.getModelByNameAndIdCategory(keyWord,idCategory).getBody();
            if (!searchModel.isEmpty()) modelList.add(searchModel);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // get every catalog with the keyword ant category
        List<Catalog> finalResult = new ArrayList<>();
        try {
            if (!modelList.isEmpty()) {
                if (modelList.get(0).size() > 0) {
                    for (int i = 0; i < modelList.get(0).size(); i++) {
                        try {
                            Optional<List<Catalog>> result = catalogService.getCatalogByModel(modelList.get(0).get(i).getIdModel());
                            if (!result.get().isEmpty()) {
                                for (int j = 0; j < result.get().size(); j++) {
                                    finalResult.add(result.get().get(j));
                                }
                            }
                        } catch (FeignException e) {
                            System.out.println("there isn´t catalog of model " + modelList.get(0).get(i).getIdModel());
                        }
                    }
                }
            }
        } catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // else...
        //pagination
        int pageSize = 12;
        int totalItems = finalResult.size();

        Pageable pageable = PageRequest.of(page, pageSize);

        if (pageable.getOffset() > totalItems) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Catalog> pageResult = finalResult.subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));

        Page<Catalog> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);

        return ResponseEntity.ok().body(catalogPage);
    }

    @GetMapping("/byCategory/{idCategory}/bySize/{bolleanAdult}/page/{page}")
    public ResponseEntity<Page<Optional<Catalog>>> getByCategoryAndSizePageable(@PathVariable Integer idCategory, @PathVariable Integer bolleanAdult,@PathVariable Integer page){

        // first verify if the category exists with feign
        try {
            modelService.getCategorydById(idCategory);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // get every model within the category
        List<Model> modelList = new ArrayList<>();
        try {
            modelList = modelService.getModelByIdCategory(idCategory).getBody();
        } catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // second verify if the bollean is correct
        List<Size> sizeList = new ArrayList<>();
        sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Optional<Catalog>> finalList = new ArrayList<>();
        if (!modelList.isEmpty()) {
            if (modelList.size() > 0) {
                for (int i = 0; i < modelList.size(); i++) {
                    for (int j=0; j < sizeList.size(); j++) {
                        Optional<Catalog> result = catalogService.findByModelAndSize(modelList.get(i).getIdModel(),sizeList.get(j).getId());
                        if (!result.isEmpty()) finalList.add(result);
                    }
                }

            }
        }
        //pagination
        int pageSize = 12;
        int totalItems = finalList .size();

        Pageable pageable = PageRequest.of(page, pageSize);

        if (pageable.getOffset() > totalItems) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Optional<Catalog>> pageResult = finalList .subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));

        Page<Optional<Catalog>> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);

        return ResponseEntity.ok().body(catalogPage);
    }

    @GetMapping("/byKeyWord/{keyWord}/bySize/{bolleanAdult}/page/{page}")
    public ResponseEntity<Page<Optional<Catalog>>> getByKeyWordAndSizePageable(@PathVariable String keyWord, @PathVariable Integer bolleanAdult,@PathVariable Integer page){

        List<Optional<List<Model>>> modelList = new ArrayList<>();
        // first verify if exists any model with feign
        try {
            Optional<List<Model>> modelByName = modelService.getByNameModel(keyWord).getBody();
            if (!modelByName.get().isEmpty()) modelList.add(modelByName);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // second verify if the bollean is correct
        List<Size> sizeList = new ArrayList<>();
        sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Optional<Catalog>> finalList = new ArrayList<>();
        if (!modelList.isEmpty()) {
            if (modelList.get(0).get().size() > 0) {
                for (int i = 0; i < modelList.get(0).get().size(); i++) {
                    for (int j=0; j < sizeList.size(); j++) {
                        Optional<Catalog> result = catalogService.findByModelAndSize(modelList.get(0).get().get(i).getIdModel(),sizeList.get(j).getId());
                        if (!result.isEmpty()) finalList.add(result);
                    }
                }

            }
        }
        //pagination
        int pageSize = 12;
        int totalItems = finalList .size();

        Pageable pageable = PageRequest.of(page, pageSize);

        if (pageable.getOffset() > totalItems) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Optional<Catalog>> pageResult = finalList .subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));

        Page<Optional<Catalog>> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);

        return ResponseEntity.ok().body(catalogPage);
    }

    @GetMapping("/byKeyWord/{keyWord}/byCategory/{idCategory}/bySize/{bolleanAdult}/page/{page}")
    public ResponseEntity<Page<Optional<Catalog>>> getByKeyWordByCategoryBySizePageable(@PathVariable String keyWord, @PathVariable Integer idCategory, @PathVariable Integer bolleanAdult,@PathVariable Integer page) {

        List<List<Model>> modelList = new ArrayList<>();
        // first verify if exists any model with feign
        try {
            List<Model> searchModel = modelService.getModelByNameAndIdCategory(keyWord, idCategory).getBody();
            if (!searchModel.isEmpty()) modelList.add(searchModel);
        } catch (FeignException e) {
            return ResponseEntity.notFound().build();
        }
        // second verify if the bollean is correct
        List<Size> sizeList = new ArrayList<>();
        sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Optional<Catalog>> finalList = new ArrayList<>();
        if (!modelList.isEmpty()) {
            if (modelList.get(0).size() > 0) {
                for (int i = 0; i < modelList.get(0).size(); i++) {
                    for (int j=0; j < sizeList.size(); j++) {
                        Optional<Catalog> result = catalogService.findByModelAndSize(modelList.get(0).get(i).getIdModel(),sizeList.get(j).getId());
                        if (!result.isEmpty()) finalList.add(result);
                    }
                }

            }
        }
        //pagination
        int pageSize = 12;
        int totalItems = finalList .size();

        Pageable pageable = PageRequest.of(page, pageSize);

        if (pageable.getOffset() > totalItems) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Optional<Catalog>> pageResult = finalList .subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));

        Page<Optional<Catalog>> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);

        return ResponseEntity.ok().body(catalogPage);
    }
    
    /////////////////////////////////////////////////////////////////


    @GetMapping("/news")
    public ResponseEntity<List<Catalog>> getNews(){
        return ResponseEntity.ok().body(catalogService.getNews());
    }

    @PostMapping("/create")
    public ResponseEntity<Catalog> createModel(@RequestBody CatalogDTO catalogDTO){

        // verify model with Feign
        try {
            ResponseEntity<Optional<Model>> response = modelService.getByIdModel(catalogDTO.getModel());
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // verify size
        Optional<Size> searchSize = sizeService.getById(catalogDTO.getSize());
        if(searchSize.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // verify quantity
        if(catalogDTO.getQuantity()<0){
            return ResponseEntity.badRequest().build();
        }
        // verify price
        if(catalogDTO.getPrice()<0){
            return ResponseEntity.badRequest().build();
        }
        // create
        Catalog catalogCreated = new Catalog();
        catalogCreated.setModel(modelService.getByIdModelSEC(catalogDTO.getModel()));
        catalogCreated.setSize(sizeService.getByIdSEC(catalogDTO.getSize()));
        catalogCreated.setQuantity(catalogDTO.getQuantity());
        catalogCreated.setPrice(catalogDTO.getPrice());
        return ResponseEntity.accepted().body(catalogService.save(catalogCreated));
    }

    // method just for users to buy
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

    // method only for admin
    @PutMapping("/modify/{idCatalog}")
    public ResponseEntity<Catalog> modifyCatalog(@PathVariable Integer idCatalog, @RequestBody CatalogDTO catalogDTO) {

        // verify model with Feign
        try {
            ResponseEntity<Optional<Model>> response = modelService.getByIdModel(catalogDTO.getModel());
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // verify size
        Optional<Size> searchSize = sizeService.getById(catalogDTO.getSize());
        if(searchSize.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // verify quantity
        if(catalogDTO.getQuantity()<0){
            return ResponseEntity.badRequest().build();
        }
        // verify price
        if(catalogDTO.getPrice()<0){
            return ResponseEntity.badRequest().build();
        }
        // create
        Catalog catalogCreated = new Catalog();
        catalogCreated.setIdCatalog(idCatalog);
        catalogCreated.setModel(modelService.getByIdModelSEC(catalogDTO.getModel()));
        catalogCreated.setSize(sizeService.getByIdSEC(catalogDTO.getSize()));
        catalogCreated.setQuantity(catalogDTO.getQuantity());
        catalogCreated.setPrice(catalogDTO.getPrice());
        return ResponseEntity.accepted().body(catalogService.save(catalogCreated));
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

    @DeleteMapping("/byModel/{idModel}")
    public ResponseEntity<String> deleteByModel (@PathVariable Integer idModel) {

        // verify if there are results
        Optional<List<Catalog>> catalogProof = catalogService.getCatalogByModel(idModel);
        if (catalogProof.get().isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // else
        catalogService.deleteByModel(idModel);
        return ResponseEntity.ok().body("Catalog items with ID model " + idModel + " deleted");
    }
}
