package com.costumemania.mscatalog.service;

import com.costumemania.mscatalog.model.Catalog;
import com.costumemania.mscatalog.model.Size;
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
    public List<Catalog> getCatalogBySize (Size size) {
        return catalogRepository.findBySize(size);
    };
    public List<Catalog> getNews () {
        return catalogRepository.findNews();
    };
    public void delete (Integer id) {
        catalogRepository.deleteById(id);
    }
}
