package com.costumemania.msproduct.service;

import com.costumemania.msproduct.model.Category;
import com.costumemania.msproduct.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    };
    public List<Category> getAllComplete() {
        return categoryRepository.findAllComplete();
    };
    public Optional<Category> getdById (Integer id) {
        return categoryRepository.findById(id);
    };
    public Optional<Category> categorydById (Integer id) {
        return categoryRepository.categoryById(id);
    };
    public Optional<Category> getByName (String name) {
        return categoryRepository.findByName(name);
    };
    public Category create (Category c) {
        return categoryRepository.save(c);
    }
    public void delete (Integer id) {
        categoryRepository.deleteById(id);
    }
}
