package com.costumemania.msproduct.repository;

import com.costumemania.msproduct.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findAll();
    Optional<Category> findById (Integer id);
    Optional<Category> findByName (String name);
}
