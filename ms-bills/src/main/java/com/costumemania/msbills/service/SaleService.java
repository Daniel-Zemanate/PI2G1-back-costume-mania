package com.costumemania.msbills.service;

import com.costumemania.msbills.model.Sale;
import com.costumemania.msbills.model.Status;
import com.costumemania.msbills.model.requiredEntity.Catalog;
import com.costumemania.msbills.model.requiredEntity.User;
import com.costumemania.msbills.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    SaleRepository saleRepository;

    public List<Sale> getAllSales(){
        return saleRepository.findAll();
    }
    public Page<Sale> getAllSales(Pageable pageable){
        Page<Sale> salePage = saleRepository.findAll(pageable);
        return salePage;
    }

    public Optional<Sale>getById(Integer id){
        return saleRepository.findById(id);
    }

    public Optional<List<Sale>> getByStatus (Status status) {
        return saleRepository.getByStatus(status);
    }

    public Optional<List<Sale>> getByUser (User user) {
        return saleRepository.getByUser(user);
    };

    public Optional<List<Sale>> getByModel (Integer idModel) {
        return saleRepository.getByModel(idModel);
    };

    public Optional<List<Sale>> getByCatalog (Catalog catalog) {
        return saleRepository.getByCatalog(catalog);
    };

    public Optional<List<Sale>> getBySize (Integer idSize) {
        return saleRepository.getBySize(idSize);
    }

    public Sale create (Sale s) {
        return saleRepository.save(s);
    }
}
