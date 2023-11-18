package com.costumemania.msbills.service;

import com.costumemania.msbills.model.Status;
import com.costumemania.msbills.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusService {

    @Autowired
    StatusRepository statusRepository;

    public List<Status> getAllStatus(){
        return statusRepository.findAll();
    }

    public Optional<Status>getById(Integer id){
        return statusRepository.findById(id);
    }

}
