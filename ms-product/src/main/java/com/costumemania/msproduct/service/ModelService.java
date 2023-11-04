package com.costumemania.msproduct.service;

import com.costumemania.msproduct.model.Model;
import com.costumemania.msproduct.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class ModelService {
    @Autowired
    ModelRepository modelRepository;

    public Model saveModel(@RequestBody Model model){
        return modelRepository.save(model);
    }
    public List<Model> getAllModel(){
        return modelRepository.findAll();
    }
    public Optional<Model> getByIdModel(Integer id){
        return modelRepository.findById(id);
    }
    public Model getByIdModelSEC(Integer id){
        return modelRepository.findByIdSEC(id);
    }
    public Optional<List<Model>> getByNameModel(String name){
        return modelRepository.findByName(name);
    }
    public List<Model> getByNameAndCategoryModel(String name,Integer category){
        return modelRepository.findByNameAndCategory(name,category);
    }
    public List<Model> getByIdCategoryModel(Integer idCategory) {
        return modelRepository.findByIdCategory(idCategory);
    }
    public List<Model> getByCategoryModel(String category){
        return modelRepository.findByCategory(category);
    }
    public void deleteByIdModel(Integer id){
       modelRepository.deleteById(id);
    }
    public void deleteByCategory (Integer idCategory) {
        modelRepository.deleteByCategory(idCategory);
    }
}
