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
    private final ModelRepository modelRepository;

    @Autowired
    public ModelService(ModelRepository modelRepository) {

        this.modelRepository = modelRepository;
    }

    public Model saveModel(@RequestBody Model model){
        return modelRepository.save(model);
    }

    public List<Model> getAllModel(){
        return modelRepository.findAll();
    }
   // ver opcional
    public Optional<Model> getByIdModel(Integer id){
        return modelRepository.findById(id);
    }

    public Optional<Model> getByNameModel(String name){
        return modelRepository.findByName(name);
    }

    public List<Model> getByNameAndCategoryModel(String name,String category){
        return modelRepository.findByNameAndCategory(name,category);
    }

    public List<Model> getByCategoryModel(String category){
        return modelRepository.findByCategory(category);
    }

    public void deleteByIdModel(Integer id){
       modelRepository.deleteById(id);
    }

    public void updateModel(Model model){
        if(modelRepository.existsById(model.getIdModel())){
            modelRepository.save(model);
        }
    }

}
