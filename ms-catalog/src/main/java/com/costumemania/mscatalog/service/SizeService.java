package com.costumemania.mscatalog.service;

import com.costumemania.mscatalog.model.Size;
import com.costumemania.mscatalog.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeService {
    @Autowired
    SizeRepository sizeRepository;

    public List<Size> getAll() {
        return sizeRepository.findAll();
    }
    public Optional<Size> getById (Integer id){
        return sizeRepository.findById(id);
    }
    public Size getByIdSEC (Integer id){
        return sizeRepository.findByIdSEC(id);
    }
    public List<Size> getByAdult(Integer adult) {
        return sizeRepository.findAllByAdult(adult);
    }
}
