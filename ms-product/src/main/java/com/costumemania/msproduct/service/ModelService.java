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
    public List<Model> getNewsLimit(Integer limit){
        return modelRepository.getNewsLimit(limit);
    }
    public List<Model> getAllComplete(){
        return modelRepository.findAllComplete();
    }
    public Optional<Model> getByIdModel(Integer id){
        return modelRepository.findById(id);
    }
    public Optional<List<Model>> getByNameModel(String name){
        return modelRepository.findByName(name);
    }
    public Optional<Model> admGetByNameModel(String name){
        return modelRepository.admFindByName(name);
    }
    public List<Model> getByNameAndCategoryModel(String name,Integer category){
        return modelRepository.findByNameAndCategory(name,category);
    }
    public Optional<Model> admGetByNameAndCategoryModel(String name,Integer category){
        return modelRepository.admFindByNameAndCategory(name,category);
    }
    public List<Model> getByIdCategoryModel(Integer idCategory) {
        return modelRepository.findByIdCategory(idCategory);
    }
    public List<Model> admGetByIdCategoryModel(Integer idCategory) {
        return modelRepository.admFindByIdCategory(idCategory);
    }
    public List<Model> getByCategoryModel(String category){
        return modelRepository.findByCategory(category);
    }
    public void inactiveByCategory (Integer idCategory) {
        modelRepository.inactiveByCategory(idCategory);
    };
}
