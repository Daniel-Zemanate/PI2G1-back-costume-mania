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

    public Optional<Catalog> getCatalogById(Integer id){
        return catalogRepository.findById(id);
    }
    public Catalog getCatalogByIdSEC(Integer id){
        return catalogRepository.findByIdSEC(id);
    }

    public List<Catalog> getCatalogBySize (Size size) {
        return catalogRepository.findBySize(size);
    };
    public List<Catalog> getCatalogBySize (Integer bollean, Integer idModel) {
        return catalogRepository.findBySize(bollean, idModel);
    };

    public List<Catalog> findByCategory (Integer category, Integer idModel) {
        return catalogRepository.findByCategory(category, idModel);
    };

    public Optional<Catalog> validateCreate (Integer idModel, Integer booleanAdult, String size) {
        return catalogRepository.validateCreate(idModel, booleanAdult, size);
    };

    // EN PRINCIPIO NO NECESITAMOS EL PAGINADO PORQUE EL MODELO VA A DEVOLVER UN SOLO ITEM
    public Optional<Page<Catalog>> getCatalogByModel (Integer idModel,Pageable pageable) {
        Optional<Page<Catalog>> catalogPage =  catalogRepository.findByModelPageable(idModel,pageable);
        return catalogPage;
    };
    public Optional<Catalog> findByModelAndSize (Integer idModel, Integer size) {
        return catalogRepository.findByModelAndSize(idModel, size);
    };
    public List<Catalog> getNews () {
        return catalogRepository.findNews();
    };
    public Catalog save (Catalog c) {
        return catalogRepository.save(c);
    }
    public void delete (Integer id) {
        catalogRepository.deleteById(id);
    }
    public void deleteByModel (Integer idModel) {
        catalogRepository.deleteByModel(idModel);
    }
}
