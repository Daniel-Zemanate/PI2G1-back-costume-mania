package com.costumemania.mscatalog.service;

import com.costumemania.mscatalog.model.Catalog;
import com.costumemania.mscatalog.model.Size;
import com.costumemania.mscatalog.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogService {
    @Autowired
    CatalogRepository catalogRepository;

    public List<Catalog> getCatalog(){
        return catalogRepository.findAll();
    }
    public Page<Catalog> getCatalog(Pageable pageable){
        Page<Catalog> catalogPage = catalogRepository.findAll(pageable);
        return catalogPage;
    }
    public Optional<List<Catalog>> getCatalogByModel (Integer idModel) {
        return catalogRepository.findByModel(idModel);
    };
    public Optional<List<Catalog>> getActiveCatalogByModel (Integer idModel) {
        return catalogRepository.findActiveByModel(idModel);
    };

    public Optional<Catalog> getCatalogById(Integer id){
        return catalogRepository.findById(id);
    }

    public List<Catalog> getCatalogBySize (Size size) {
        return catalogRepository.findBySize(size);
    };
    public List<Catalog> getActiveCatalogBySize (Integer bollean, Integer idModel) {
        return catalogRepository.findActiveBySize(bollean, idModel);
    };
    public List<Catalog> findActiveByCategory (Integer category, Integer idModel) {
        return catalogRepository.findActiveByCategory(category, idModel);
    };

    public Optional<Catalog> validateCreate (Integer idModel, Integer booleanAdult, String size) {
        return catalogRepository.validateCreate(idModel, booleanAdult, size);
    };

    public Optional<List<Catalog>> findByModelAndSize (Integer idModel, Integer size) {
        return catalogRepository.findByModelAndSize(idModel, size);
    };
    public Catalog save (Catalog c) {
        return catalogRepository.save(c);
    }
    public void inactiveByModel (Integer idModel) {
        catalogRepository.inactiveByModel(idModel);
    };
    public void inactiveByCategory (Integer idCategory) {
        catalogRepository.inactiveByCategory(idCategory);
    };
}