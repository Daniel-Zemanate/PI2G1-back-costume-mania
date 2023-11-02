package com.costumemania.msproduct.repository;

import com.costumemania.msproduct.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository<Model,Integer> {
    @Query( value ="SELECT * FROM model m WHERE m.name_model = ?1",nativeQuery = true)
    Optional<Model> findByName(String nameModel);
    @Query( value ="SELECT * FROM model m INNER JOIN category c ON m.category=c.id_category WHERE c.name LIKE %?1%",nativeQuery = true)
    List<Model> findByCategory(String category);
    @Query(value= "SELECT * FROM model m INNER JOIN category c ON m.category=c.id_category WHERE (m.name_model LIKE %?1%) AND (c.name LIKE %?2%)",nativeQuery = true)
    List<Model> findByNameAndCategory(String name, String category);

}

