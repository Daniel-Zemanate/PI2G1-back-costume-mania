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
    public Optional<Catalog> getCatalogById(Integer id){
        return catalogRepository.findById(id);
    }
    public Catalog getCatalogByIdSEC(Integer id){
        return catalogRepository.findByIdSEC(id);
    }
    public List<Catalog> getCatalogBySize (Size size) {
        return catalogRepository.findBySize(size);
    };
    public List<Catalog> getCatalogByIdModel(Integer idModel){
        return catalogRepository.findByIdModel(idModel);

    }
    public List<Catalog> getCatalogByNameModel(String nameModel) {
        return catalogRepository.findByNameModel(nameModel);
    }

    public List<Catalog>getCatalogByIDCategoryNameModel(Integer idCategory,String nameModel){
    return catalogRepository.findByIdCategoryNameModel(idCategory,nameModel);
    }

    public List<Catalog> getNews () {
        return catalogRepository.findNews();
    };
    public Catalog save (Catalog c) {
        return catalogRepository.save(c);
    }
    public void delete (Integer id) {
        catalogRepository.deleteById(id);
    }


}
