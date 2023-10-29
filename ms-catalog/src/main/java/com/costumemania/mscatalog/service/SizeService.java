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
    public Optional<Size> getById (Long id){
        return sizeRepository.findById(id);
    }
    public List<Size> getByAdultOrChild(Integer adult) {
        return sizeRepository.findAllByAdultOrChild(adult);
    }
}
