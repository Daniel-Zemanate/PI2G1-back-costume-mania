package com.costumemania.mscatalog.service;

import com.costumemania.mscatalog.model.Catalog;
import com.costumemania.mscatalog.model.CatalogDTO;
import com.costumemania.mscatalog.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Optional<Catalog> getCatalogById(Integer id){
        return catalogRepository.findById(id);
    }




    public Catalog create (CatalogDTO catalogDTO) {
        return catalogRepository.create(catalogDTO);
    };

    public Catalog update (CatalogDTO catalogDTO) {
        return catalogRepository.update(catalogDTO);
    };


    public List<Catalog> getLastCatalog(){
        return catalogRepository.findNews();
    }

    public List<Catalog> getCatalogByModel(Long idModel){
        return catalogRepository.findAllByModel(idModel);
    }

    public List<Catalog> getCatalogByModelName(String modelName){
        return catalogRepository.findAllByModelName(modelName);
    }

    public List<Catalog> getCatalogByCategory(Long idCategory){
        return catalogRepository.findAllByCategory(idCategory);
    }

    public List<Catalog> getCatalogBySize(Long idSize){
        return catalogRepository.findAllBySize(idSize);
    }

    public void delete(Long id){
        catalogRepository.delete(id);
    }
}
