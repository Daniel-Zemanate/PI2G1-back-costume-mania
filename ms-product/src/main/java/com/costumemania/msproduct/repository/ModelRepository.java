package com.costumemania.msproduct.repository;

import com.costumemania.msproduct.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends JpaRepository<Model,Integer> {
    @Query( value ="SELECT * FROM model m WHERE m.name_model = ?1",nativeQuery = true)
    Model findByName(String nameModel);
    @Query( value ="SELECT * FROM model m WHERE m.category = ?1",nativeQuery = true)
    Model findByCategory(String category);
}

