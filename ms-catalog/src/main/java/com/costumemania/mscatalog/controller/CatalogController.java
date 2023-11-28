package com.costumemania.mscatalog.controller;

import com.costumemania.mscatalog.model.*;
import com.costumemania.mscatalog.service.CatalogService;
import com.costumemania.mscatalog.service.ModelService;
import com.costumemania.mscatalog.service.SizeService;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/catalog")
public class CatalogController {
    private final CatalogService catalogService;
    private final SizeService sizeService;
    private final ModelService modelService;
    public CatalogController(CatalogService catalogService, SizeService sizeService, ModelService modelService) {
        this.catalogService = catalogService;
        this.sizeService = sizeService;
        this.modelService = modelService;
    }

    //////////////////////////////////////////////////////////////////
    // FUNCTION to transform catalog to CatalogResponse
    private CatalogResponse transformCatalog (List<Catalog> c) {
        List<CatalogResponse.SizeByModel> listSize = new ArrayList<>();
        for (Catalog catalog : c) {
            listSize.add(new CatalogResponse.SizeByModel(catalog.getIdCatalog(),
                    catalog.getSize().getNoSize(),
                    catalog.getStock()));
        }
        return new CatalogResponse(
                c.get(0).getModel().getNameModel(),
                c.get(0).getModel().getIdModel(),
                c.get(0).getModel().getCategory().getName(),
                c.get(0).getModel().getUrlImage(),
                c.get(0).getSize().getAdult(),
                c.get(0).getPrice(),
                listSize
        );
    }
    //////////////////////////////////////////////////////////////////

    //////////////---------- TODO EL CATALOGO ----------//////////////

    // public - devuelve absolutamente todo, en items de catálogo
    @GetMapping
    public ResponseEntity<List<Catalog>> getAll(){
        return ResponseEntity.ok().body(catalogService.getCatalog());
    }

    // public - devuelve absolutamente todo, en items de catálogo
    @GetMapping("/page/{page}")
    public Page<Catalog> getAll(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 12);
        return catalogService.getCatalog(pageable);
    }

    // public - Devuelve solo los items que están activos, agrupados por modelo
    @GetMapping("/all/page/{page}")
    public ResponseEntity<Page<CatalogResponse>> getAllGroup(@PathVariable Integer page){
        try {
            List<Model> modelIterator = modelService.getAllModel().getBody();
            // get active catalog
            List <CatalogResponse> catalogResponses = new ArrayList<>();
            for (Model model : modelIterator) {
                Optional<List<Catalog>> listCatalog = catalogService.getActiveCatalogByModel(model.getIdModel());
                if (!listCatalog.get().isEmpty()) {
                    catalogResponses.add(transformCatalog(listCatalog.get()));
                }
            }
            //pagination
            int pageSize = 12;
            int totalItems = catalogResponses.size();
            Pageable pageable = PageRequest.of(page, pageSize);
            if (pageable.getOffset() > totalItems) {
                return ResponseEntity.notFound().build();
            }
            // else...
            List<CatalogResponse> pageResult = catalogResponses.subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));
            Page<CatalogResponse> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);
            return ResponseEntity.ok().body(catalogPage);
        }
        catch (FeignException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /////////////////////////////////////////////////////////////////

    // public - devuelve los inactivos también
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

    // public - Devuelve listado tipo catálogo
    @GetMapping("/byModel2/{idModel}")
    public ResponseEntity<Optional<List<Catalog>>> getByModel2(@PathVariable Integer idModel){
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
    // public - Devuelve listado tipo catálogoResponse
    @GetMapping("/byModel/{idModel}")
    public ResponseEntity<CatalogResponse> getByModel(@PathVariable Integer idModel){
        // first verify if the model exists with feign
        try {
            ResponseEntity<Optional<Model>> response = modelService.getByIdModel(idModel);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // else...
        if (catalogService.getCatalogByModel(idModel).get().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(transformCatalog(catalogService.getActiveCatalogByModel(idModel).get()));
    }

    // public - devuelve Catalogo y sin paginar
    @GetMapping("/bySize/{bolleanAdult}")
    public ResponseEntity<List<Catalog>> getBySize(@PathVariable Integer bolleanAdult){
        // first verify if the bollean is correct
        List<Size> sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Catalog> result = new ArrayList<>();
        for (Size size : sizeList) {
            List<Catalog> listBySize = catalogService.getCatalogBySize(size);
            result.addAll(listBySize);
        }
        return ResponseEntity.ok().body(result);
    }

    // public - devuelve Catalogo y sin paginar
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
                for (Model allModel : allModels) {
                    try {
                        Optional<List<Catalog>> list = catalogService.getCatalogByModel(allModel.getIdModel());
                        if (!list.get().isEmpty()) {
                            result.addAll(list.get());
                        }
                    } catch (FeignException e) {
                        System.out.println("there isn´t catalog of model " + allModel.getIdModel());
                    }
                }
            }
        } catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // else...
        return ResponseEntity.ok().body(result);
    }

    // public - devuelve Catalogo y sin paginar
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
                                finalResult.addAll(result.get());
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

    // public - devuelve Catalogo y sin paginar
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
                                finalResult.addAll(result.get());
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

    // public - devuelve Catalogo y sin paginar
    @GetMapping("/byCategory/{idCategory}/bySize/{bolleanAdult}")
    public ResponseEntity<List<Catalog>> getByCategoryAndSize(@PathVariable Integer idCategory, @PathVariable Integer bolleanAdult){
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
        List<Size> sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Catalog> finalList = new ArrayList<>();
        if (!modelList.isEmpty()) {
            if (modelList.size() > 0) {
                for (Model model : modelList) {
                    for (Size size : sizeList) {
                        Optional<List<Catalog>> result = catalogService.findByModelAndSize(model.getIdModel(), size.getId());
                        if (!result.get().isEmpty()) finalList.addAll(result.get());
                    }
                }

            }
        }
        return ResponseEntity.ok().body(finalList);
    }

    // public - devuelve Catalogo y sin paginar
    @GetMapping("/byKeyWord/{keyWord}/bySize/{bolleanAdult}")
    public ResponseEntity<List<Catalog>> getByKeyWordAndSize(@PathVariable String keyWord, @PathVariable Integer bolleanAdult){
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
        List<Size> sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Catalog> finalList = new ArrayList<>();
        if (!modelList.isEmpty()) {
            if (modelList.get(0).get().size() > 0) {
                for (int i = 0; i < modelList.get(0).get().size(); i++) {
                    for (Size size : sizeList) {
                        Optional<List<Catalog>> result = catalogService.findByModelAndSize(modelList.get(0).get().get(i).getIdModel(), size.getId());
                        if (!result.get().isEmpty()) finalList.addAll(result.get());
                    }
                }

            }
        }
        return ResponseEntity.ok().body(finalList);
    }

    // public - devuelve Catalogo y sin paginar
    @GetMapping("/byKeyWord/{keyWord}/byCategory/{idCategory}/bySize/{bolleanAdult}")
    public ResponseEntity<List<Catalog>> getByKeyWordByCategoryBySize(@PathVariable String keyWord, @PathVariable Integer idCategory, @PathVariable Integer bolleanAdult) {
        List<List<Model>> modelList = new ArrayList<>();
        // first verify if exists any model with feign
        try {
            List<Model> searchModel = modelService.getModelByNameAndIdCategory(keyWord, idCategory).getBody();
            if (!searchModel.isEmpty()) modelList.add(searchModel);
        } catch (FeignException e) {
            return ResponseEntity.notFound().build();
        }
        // second verify if the bollean is correct
        List<Size> sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Catalog> finalList = new ArrayList<>();
        if (!modelList.isEmpty()) {
            if (modelList.get(0).size() > 0) {
                for (int i = 0; i < modelList.get(0).size(); i++) {
                    for (Size size : sizeList) {
                        Optional<List<Catalog>> result = catalogService.findByModelAndSize(modelList.get(0).get(i).getIdModel(), size.getId());
                        if (!result.get().isEmpty()) finalList.addAll(result.get());
                    }
                }

            }
        }
        return ResponseEntity.ok().body(finalList);
    }

    /////////////////////////////////////////////////////////////////

    //////////////----------  CATALOGO FILTRADO CON PAGINADO----------//////////////

    // public - devuelve Catalogo paginado
    @GetMapping("/bySize2/{bolleanAdult}/page/{page}")
    public ResponseEntity<Page<Catalog>> getBySizePageable2(@PathVariable Integer bolleanAdult,@PathVariable Integer page){
        // first verify if the bollean is correct
        List<Size> sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Catalog> result = new ArrayList<>();
        for (Size size : sizeList) {
            List<Catalog> listBySize = catalogService.getCatalogBySize(size);
            result.addAll(listBySize);
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
    // public - devuelve CatalogoResponse paginado - solo activos
    @GetMapping("/bySize/{bolleanAdult}/page/{page}")
    public ResponseEntity<Page<CatalogResponse>> getBySizePageable(@PathVariable Integer bolleanAdult,@PathVariable Integer page){
        // first verify if the bollean is correct
        List<Size> sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        try {
            List<Model> modelIterator = modelService.getAllModel().getBody();
            List<CatalogResponse> catalogResponses = new ArrayList<>();
            for (Model model : modelIterator) {
                List<Catalog> listCatalog = catalogService.getActiveCatalogBySize(bolleanAdult, model.getIdModel());
                if (!listCatalog.isEmpty()) {
                    catalogResponses.add(transformCatalog(listCatalog));
                }
            }
            //pagination
            int pageSize = 12;
            int totalItems = catalogResponses.size();
            Pageable pageable = PageRequest.of(page, pageSize);
            if (pageable.getOffset() > totalItems) {
                return ResponseEntity.notFound().build();
            }
            // else...
            List<CatalogResponse> pageResult = catalogResponses.subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));
            Page<CatalogResponse> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);
            return ResponseEntity.ok().body(catalogPage);
        }
        catch (FeignException e) {
                return ResponseEntity.internalServerError().build();
        }
    }

    // public - devuelve Catalogo paginado Act e Inact
    @GetMapping("/byCategory2/{idCategory}/page/{page}")
    public ResponseEntity<Page<Catalog>> getByCategoryPageable2(@PathVariable Integer idCategory,@PathVariable Integer page){
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
                            result.addAll(list.get());
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
    // public - devuelve CatalogoResponse paginado
    @GetMapping("/byCategory/{idCategory}/page/{page}")
    public ResponseEntity<Page<CatalogResponse>> getByCategoryPageable(@PathVariable Integer idCategory,@PathVariable Integer page){
        // first verify if the category exists with feign
        try {
            modelService.getModelByIdCategory(idCategory);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // get every model within the category
        List<Catalog> result = new ArrayList<>();
        try {
            List<Model> allModels = modelService.getModelByIdCategory(idCategory).getBody();
            List<CatalogResponse> catalogResponses = new ArrayList<>();
            if (allModels.size() > 0) {
                for (Model model : allModels) {
                    List<Catalog> listCatalog = catalogService.findActiveByCategory(idCategory, model.getIdModel());
                    if (!listCatalog.isEmpty()) {
                        catalogResponses.add(transformCatalog(listCatalog));
                    }
                }
            }
            if (catalogResponses.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            //pagination
            int pageSize = 12;
            int totalItems = catalogResponses.size();
            Pageable pageable = PageRequest.of(page, pageSize);
            if (pageable.getOffset() > totalItems) {
                return ResponseEntity.notFound().build();
            }
            // else...
            List<CatalogResponse> pageResult = catalogResponses.subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));
            Page<CatalogResponse> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);
            return ResponseEntity.ok().body(catalogPage);
        } catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
    }

    // public - devuelve Catalogo paginado
    @GetMapping("/byKeyWord2/{keyWord}/page/{page}")
    public ResponseEntity<Page<Catalog>> getByKeyWordPageable2(@PathVariable String keyWord,@PathVariable Integer page){
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
                                finalResult.addAll(result.get());
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
    // public - devuelve CatalogoResponse paginado
    @GetMapping("/byKeyWord/{keyWord}/page/{page}")
    public ResponseEntity<Page<CatalogResponse>> getByKeyWordPageable(@PathVariable String keyWord,@PathVariable Integer page){
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
        List <CatalogResponse> catalogResponses = new ArrayList<>();
        if (!modelList.isEmpty()) {
            if (!modelList.get(0).get().isEmpty()) {
                for (int i = 0; i < modelList.get(0).get().size(); i++) {
                    Optional<List<Catalog>> listCatalog = catalogService.getActiveCatalogByModel(modelList.get(0).get().get(i).getIdModel());
                    if (!listCatalog.get().isEmpty()) {
                        catalogResponses.add(transformCatalog(listCatalog.get()));
                    }
                }
            }
        }
        if (catalogResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        //pagination
        int pageSize = 12;
        int totalItems = catalogResponses.size();
        Pageable pageable = PageRequest.of(page, pageSize);
        if (pageable.getOffset() > totalItems) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<CatalogResponse> pageResult = catalogResponses.subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));
        Page<CatalogResponse> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);
        return ResponseEntity.ok().body(catalogPage);
    }

    // public - devuelve Catalogo paginado
    @GetMapping("/byKeyWord2/{keyWord}/byCategory/{idCategory}/page/{page}")
    public ResponseEntity<Page<Catalog>> getByKeyWordByCategoryPageable2(@PathVariable String keyWord, @PathVariable Integer idCategory,@PathVariable Integer page){
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
                if (!modelList.get(0).isEmpty()) {
                    for (int i = 0; i < modelList.get(0).size(); i++) {
                        try {
                            Optional<List<Catalog>> result = catalogService.getCatalogByModel(modelList.get(0).get(i).getIdModel());
                            if (!result.get().isEmpty()) {
                                finalResult.addAll(result.get());
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
    // public - devuelve CatalogoResponse paginado
    @GetMapping("/byKeyWord/{keyWord}/byCategory/{idCategory}/page/{page}")
    public ResponseEntity<Page<CatalogResponse>> getByKeyWordByCategoryPageable(@PathVariable String keyWord, @PathVariable Integer idCategory,@PathVariable Integer page){
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
        List <CatalogResponse> catalogResponses = new ArrayList<>();
        if (!modelList.isEmpty()) {
            if (!modelList.get(0).isEmpty()) {
                for (int i = 0; i < modelList.get(0).size(); i++) {
                    Optional<List<Catalog>> listCatalog = catalogService.getActiveCatalogByModel(modelList.get(0).get(i).getIdModel());
                    if (!listCatalog.get().isEmpty()) {
                        catalogResponses.add(transformCatalog(listCatalog.get()));
                    }
                }
            }
        }
        if (catalogResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        //pagination
        int pageSize = 12;
        int totalItems = catalogResponses.size();
        Pageable pageable = PageRequest.of(page, pageSize);
        if (pageable.getOffset() > totalItems) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<CatalogResponse> pageResult = catalogResponses.subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));
        Page<CatalogResponse> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);
        return ResponseEntity.ok().body(catalogPage);
    }

    // public - devuelve Catalogo paginado
    @GetMapping("/byCategory2/{idCategory}/bySize/{bolleanAdult}/page/{page}")
    public ResponseEntity<Page<Catalog>> getByCategoryAndSizePageable2(@PathVariable Integer idCategory, @PathVariable Integer bolleanAdult,@PathVariable Integer page){
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
        List<Size> sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Catalog> finalList = new ArrayList<>();
        if (!modelList.isEmpty()) {
            if (modelList.size() > 0) {
                for (Model model : modelList) {
                    for (Size size : sizeList) {
                        Optional<List<Catalog>> result = catalogService.findByModelAndSize(model.getIdModel(), size.getId());
                        if (!result.get().isEmpty()) finalList.addAll(result.get());
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
        List<Catalog> pageResult = finalList .subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));
        Page<Catalog> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);
        return ResponseEntity.ok().body(catalogPage);
    }
    // public - devuelve CatalogoResponse paginado
    @GetMapping("/byCategory/{idCategory}/bySize/{bolleanAdult}/page/{page}")
    public ResponseEntity<Page<CatalogResponse>> getByCategoryAndSizePageable(@PathVariable Integer idCategory, @PathVariable Integer bolleanAdult,@PathVariable Integer page){
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
        List<Size> sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<CatalogResponse> catalogResponses = new ArrayList<>();
        for (Model model : modelList) {
            List<Catalog> listCatalog = catalogService.getActiveCatalogBySize(bolleanAdult, model.getIdModel());
            if (!listCatalog.isEmpty()) {
                catalogResponses.add(transformCatalog(listCatalog));
            }
        }
        if (catalogResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        //pagination
        int pageSize = 12;
        int totalItems = catalogResponses.size();
        Pageable pageable = PageRequest.of(page, pageSize);
        if (pageable.getOffset() > totalItems) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<CatalogResponse> pageResult = catalogResponses.subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));
        Page<CatalogResponse> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);
        return ResponseEntity.ok().body(catalogPage);
    }

    // public - devuelve Catalogo paginado
    @GetMapping("/byKeyWord2/{keyWord}/bySize/{bolleanAdult}/page/{page}")
    public ResponseEntity<Page<Catalog>> getByKeyWordAndSizePageable2(@PathVariable String keyWord, @PathVariable Integer bolleanAdult,@PathVariable Integer page){
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
        List<Size> sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Catalog> finalList = new ArrayList<>();
        if (!modelList.isEmpty()) {
            if (modelList.get(0).get().size() > 0) {
                for (int i = 0; i < modelList.get(0).get().size(); i++) {
                    for (Size size : sizeList) {
                        Optional<List<Catalog>> result = catalogService.findByModelAndSize(modelList.get(0).get().get(i).getIdModel(), size.getId());
                        if (!result.get().isEmpty()) finalList.addAll(result.get());
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
        List<Catalog> pageResult = finalList .subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));
        Page<Catalog> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);
        return ResponseEntity.ok().body(catalogPage);
    }
    // public - devuelve CatalogoResponse paginado
    @GetMapping("/byKeyWord/{keyWord}/bySize/{bolleanAdult}/page/{page}")
    public ResponseEntity<Page<CatalogResponse>> getByKeyWordAndSizePageable(@PathVariable String keyWord, @PathVariable Integer bolleanAdult,@PathVariable Integer page){
        List<Model> modelList = new ArrayList<>();
        // first verify if exists any model with feign
        try {
            Optional<List<Model>> modelByName = modelService.getByNameModel(keyWord).getBody();
            if (!modelByName.get().isEmpty()) modelList.addAll(modelByName.get());
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // second verify if the bollean is correct
        List<Size> sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<CatalogResponse> catalogResponses = new ArrayList<>();
        for (Model model : modelList) {
            List<Catalog> listCatalog = catalogService.getActiveCatalogBySize(bolleanAdult, model.getIdModel());
            if (!listCatalog.isEmpty()) {
                catalogResponses.add(transformCatalog(listCatalog));
            }
        }
        if (catalogResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        //pagination
        int pageSize = 12;
        int totalItems = catalogResponses.size();
        Pageable pageable = PageRequest.of(page, pageSize);
        if (pageable.getOffset() > totalItems) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<CatalogResponse> pageResult = catalogResponses.subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));
        Page<CatalogResponse> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);
        return ResponseEntity.ok().body(catalogPage);
    }

    // public - devuelve Catalogo paginado
    @GetMapping("/byKeyWord2/{keyWord}/byCategory/{idCategory}/bySize/{bolleanAdult}/page/{page}")
    public ResponseEntity<Page<Catalog>> getByKeyWordByCategoryBySizePageable2(@PathVariable String keyWord, @PathVariable Integer idCategory, @PathVariable Integer bolleanAdult,@PathVariable Integer page) {
        List<List<Model>> modelList = new ArrayList<>();
        // first verify if exists any model with feign
        try {
            List<Model> searchModel = modelService.getModelByNameAndIdCategory(keyWord, idCategory).getBody();
            if (!searchModel.isEmpty()) modelList.add(searchModel);
        } catch (FeignException e) {
            return ResponseEntity.notFound().build();
        }
        // second verify if the bollean is correct
        List<Size> sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<Catalog> finalList = new ArrayList<>();
        if (!modelList.isEmpty()) {
            if (modelList.get(0).size() > 0) {
                for (int i = 0; i < modelList.get(0).size(); i++) {
                    for (Size size : sizeList) {
                        Optional<List<Catalog>> result = catalogService.findByModelAndSize(modelList.get(0).get(i).getIdModel(), size.getId());
                        if (!result.get().isEmpty()) finalList.addAll(result.get());
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
        List<Catalog> pageResult = finalList .subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));
        Page<Catalog> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);
        return ResponseEntity.ok().body(catalogPage);
    }
    // public - devuelve CatalogoResponse paginado
    @GetMapping("/byKeyWord/{keyWord}/byCategory/{idCategory}/bySize/{bolleanAdult}/page/{page}")
    public ResponseEntity<Page<CatalogResponse>> getByKeyWordByCategoryBySizePageable(@PathVariable String keyWord, @PathVariable Integer idCategory, @PathVariable Integer bolleanAdult,@PathVariable Integer page) {
        List<Model> modelList = new ArrayList<>();
        // first verify if exists any model with feign
        try {
            List<Model> searchModel = modelService.getModelByNameAndIdCategory(keyWord, idCategory).getBody();
            if (!searchModel.isEmpty()) modelList.addAll(searchModel);
        } catch (FeignException e) {
            return ResponseEntity.notFound().build();
        }
        // second verify if the bollean is correct
        List<Size> sizeList = sizeService.getByAdult(bolleanAdult);
        if (sizeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<CatalogResponse> catalogResponses = new ArrayList<>();
        for (Model model : modelList) {
            List<Catalog> listCatalog = catalogService.getActiveCatalogBySize(bolleanAdult, model.getIdModel());
            if (!listCatalog.isEmpty()) {
                catalogResponses.add(transformCatalog(listCatalog));
            }
        }
        if (catalogResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        //pagination
        int pageSize = 12;
        int totalItems = catalogResponses.size();
        Pageable pageable = PageRequest.of(page, pageSize);
        if (pageable.getOffset() > totalItems) {
            return ResponseEntity.notFound().build();
        }
        // else...
        List<CatalogResponse> pageResult = catalogResponses.subList((int) pageable.getOffset(), Math.min((int) pageable.getOffset() + pageSize, totalItems));
        Page<CatalogResponse> catalogPage = new PageImpl<>(pageResult, pageable, totalItems);
        return ResponseEntity.ok().body(catalogPage);
    }
    
    /////////////////////////////////////////////////////////////////

    // public - Solo trae los activos
    @GetMapping("/news/{limit}")
    public ResponseEntity<List<CatalogResponse>> getNews(@PathVariable Integer limit){
        List<Model> modelIterator = modelService.getNewsLimit(limit).getBody();
        // get active catalog
        List <CatalogResponse> catalogResponses = new ArrayList<>();
        for (Model model : modelIterator) {
            Optional<List<Catalog>> listCatalog = catalogService.getActiveCatalogByModel(model.getIdModel());
            if (!listCatalog.get().isEmpty()) {
                catalogResponses.add(transformCatalog(listCatalog.get()));
            }
        }

        return ResponseEntity.ok().body(catalogResponses);
    }

    // adm
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Catalog> createModel(@RequestBody CatalogDTO catalogDTO){
        // verify model with Feign - 404
        try {
            ResponseEntity<Optional<Model>> response = modelService.getByIdModel(catalogDTO.getModel());
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // verify size - 404
        Optional<Size> searchSize = sizeService.getById(catalogDTO.getSize());
        if(searchSize.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // verify if this Catalog exists - 422 Unprocessable Content
        Optional<Catalog> searchCatalog = catalogService.validateCreate(catalogDTO.getModel(), searchSize.get().getAdult(), searchSize.get().getNoSize());
        if(searchCatalog.isPresent()){
            return ResponseEntity.unprocessableEntity().build();
        }
        // verify quantity - 400
        if(catalogDTO.getQuantity()<0){
            return ResponseEntity.badRequest().build();
        }
        // verify price - 400
        if(catalogDTO.getPrice()<0){
            return ResponseEntity.badRequest().build();
        }
        // create - 202
        Catalog catalogCreated = new Catalog();
        catalogCreated.setModel(modelService.getByIdModelSEC(catalogDTO.getModel()));
        catalogCreated.setSize(sizeService.getByIdSEC(catalogDTO.getSize()));
        catalogCreated.setStock(catalogDTO.getQuantity());
        catalogCreated.setPrice(catalogDTO.getPrice());
        catalogCreated.setStatus(new StatusComponent(1, "active"));
        return ResponseEntity.accepted().body(catalogService.save(catalogCreated));
    }

    // users - to buy
    @PutMapping("/{idCatalog}/{quantity}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Catalog> catalogSold(@PathVariable Integer idCatalog, @PathVariable Integer quantity) {
        // verify if catalog exists - 404
        Optional<Catalog> searchCatalog = catalogService.getCatalogById(idCatalog);
        if (searchCatalog.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // verify quantity
        if(searchCatalog.get().getStock()-quantity<0){
            return ResponseEntity.badRequest().build();
        }
        //else...
        searchCatalog.get().setStock(searchCatalog.get().getStock()-quantity);
        return ResponseEntity.accepted().body(catalogService.save(searchCatalog.get()));
    }

    // adm
    @PutMapping("/modify/{idCatalog}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Catalog> modifyCatalog(@PathVariable Integer idCatalog, @RequestBody CatalogDTO catalogDTO) {
        // verify if catalog exists (active or inactive) - 404
        Optional<Catalog> searchCatalog = catalogService.getCatalogById(idCatalog);
        if (searchCatalog.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // verify model with Feign - 404
        try {
            ResponseEntity<Optional<Model>> response = modelService.getByIdModel(catalogDTO.getModel());
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // verify size - 404
        Optional<Size> searchSize = sizeService.getById(catalogDTO.getSize());
        if(searchSize.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // verify quantity - 400
        if(catalogDTO.getQuantity()<0){
            return ResponseEntity.badRequest().build();
        }
        // verify price - 400
        if(catalogDTO.getPrice()<0){
            return ResponseEntity.badRequest().build();
        }
        // create Catalog
        Catalog catalogCreated = new Catalog();
        catalogCreated.setIdCatalog(idCatalog);
        catalogCreated.setModel(modelService.getByIdModelSEC(catalogDTO.getModel()));
        catalogCreated.setSize(sizeService.getByIdSEC(catalogDTO.getSize()));
        catalogCreated.setStock(catalogDTO.getQuantity());
        catalogCreated.setPrice(catalogDTO.getPrice());
        if (catalogDTO.getStatus() == 1) {
            catalogCreated.setStatus(new StatusComponent(1, "active"));
        } else if (catalogDTO.getStatus() == 2) {
            catalogCreated.setStatus(new StatusComponent(2, "inactive"));
        } else {
            return ResponseEntity.badRequest().build();
        }
        // verify if this Catalog exists - 422 Unprocessable Content
        if (!Objects.equals(catalogDTO.getModel(), searchCatalog.get().getModel().getIdModel()) || !Objects.equals(catalogDTO.getSize(), searchCatalog.get().getSize().getId())) {
            Optional<Catalog> searchNewCatalog = catalogService.validateCreate(catalogCreated.getModel().getIdModel(), catalogCreated.getSize().getAdult(), catalogCreated.getSize().getNoSize());
            if(searchNewCatalog.isPresent()){
                return ResponseEntity.unprocessableEntity().build();
            }
        }
        // create - 202
        return ResponseEntity.accepted().body(catalogService.save(catalogCreated));
    }


    ///////////////////--------- APIS to DELETE CATALOG------------///////////////////

    // adm - deshabilita catalogo
    @PutMapping("/delete/{idCatalog}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Catalog> makeInactiv (@PathVariable Integer idCatalog) {
        // verify if catalog exists (active or inactive) - 404
        Optional<Catalog> searchCatalog = catalogService.getCatalogById(idCatalog);
        if (searchCatalog.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // modify state
        searchCatalog.get().setStatus(new StatusComponent(2, "inactive"));
        return ResponseEntity.ok().body(catalogService.save(searchCatalog.get()));
    }
    // adm - deshabilita catalogo por modelo
    @PutMapping("/deleteByM/{idModel}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> makeInactivByModel (@PathVariable Integer idModel) {
        Optional<List<Catalog>> searchCatalog = catalogService.getCatalogByModel(idModel);
        if (searchCatalog.get().isEmpty()) {
            return ResponseEntity.ok().body("there isn´t catalog with that model");
        }
        // modify state
        catalogService.inactiveByModel(idModel);
        return ResponseEntity.ok().body("all catalog with model " + idModel + " disabled");
    }
    // adm - deshabilita catalogo por categoria
    @PutMapping("/deleteByC/{idCategory}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> makeInactivByCat (@PathVariable Integer idCategory) {
        try {
            modelService.getCategorydById(idCategory);
        }
        catch (FeignException e){
            return ResponseEntity.notFound().build();
        }
        // modify state
        catalogService.inactiveByCategory(idCategory);
        return ResponseEntity.ok().body("all catalog with category " + idCategory + " disabled");
    }

    // adm - deprecated
    /*@DeleteMapping("/{idCatalog}")
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
    // adm - deprecated
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
    }*/
}
