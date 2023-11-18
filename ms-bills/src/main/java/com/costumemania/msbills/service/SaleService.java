package com.costumemania.msbills.service;

import com.costumemania.msbills.model.Sale;
import com.costumemania.msbills.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleService {
    @Autowired
    SaleRepository saleRepository;

    public List<Sale> getAllSale(){
        return saleRepository.findAll();
    }

    public Optional<Sale> getSaleById(Integer id){
        return saleRepository.findById(id);
    }

    public List<Sale> getSaleByStatus(Integer id){
        return saleRepository.findByStatus(id);
    }
    public List<Sale> getSaleByModel(Integer id){
        return saleRepository.findByModel(id);
    }
    public List<Sale> getSaleByUser(Integer id){
        return saleRepository.findByModel(id);
    }
    public List<Sale> getSaleByCatalog(Integer id){
        return saleRepository.findByCatalog(id);
    }
    public List<Sale> getSaleBySize(Integer id){
        return saleRepository.findBySize(id);
    }
}

