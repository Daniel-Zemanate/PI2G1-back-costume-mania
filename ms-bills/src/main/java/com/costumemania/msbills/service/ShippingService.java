package com.costumemania.msbills.service;

import com.costumemania.msbills.model.Shipping;
import com.costumemania.msbills.repository.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ShippingService {
    @Autowired
    ShippingRepository shippingRepository;

    public Shipping saveShipping(Shipping shipping){
        return shippingRepository.save(shipping);
    }

    public List<Shipping>getAllShipping(){
        return shippingRepository.findAll();
    }

    public Optional<Shipping> getByIdShipping(Integer id){
        return shippingRepository.findById(id);
    }

    public Optional<List<Shipping>> getByDestinationShipping(String destination){
        return shippingRepository.findByDestination(destination);
    }
}
