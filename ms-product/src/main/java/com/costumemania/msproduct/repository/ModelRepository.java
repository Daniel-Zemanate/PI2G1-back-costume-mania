package com.costumemania.msproduct.repository;

import com.costumemania.msproduct.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository<Model,Integer> {
    @Query(value= "SELECT * FROM model m  WHERE m.status_model=1",nativeQuery = true)
    List<Model> findAll();
    @Query(value= "SELECT * FROM model m",nativeQuery = true)
    List<Model> findAllComplete();
    @Query( value ="SELECT * FROM model m WHERE m.name_model LIKE %?1% AND m.status_model=1",nativeQuery = true)
    Optional<List<Model>> findByName(String nameModel);
    @Query( value ="SELECT * FROM model m WHERE m.name_model =?1",nativeQuery = true)
    Optional<Model> admFindByName(String nameModel);
    @Query( value ="SELECT * FROM model m INNER JOIN category c ON m.category=c.id_category WHERE c.name LIKE %?1%",nativeQuery = true)
    List<Model> findByCategory(String category);
    @Query(value= "SELECT * FROM model m INNER JOIN category c ON m.category=c.id_category WHERE (m.name_model LIKE %?1%) AND (m.category =?2) AND m.status_model=1",nativeQuery = true)
    List<Model> findByNameAndCategory(String name, Integer category);
    @Query(value= "SELECT * FROM model m INNER JOIN category c ON m.category=c.id_category WHERE (m.name_model =?1) AND (m.category =?2)",nativeQuery = true)
    Optional<Model> admFindByNameAndCategory(String name, Integer category);
    @Query(value= "SELECT * FROM model m  WHERE m.category =?1 AND m.status_model=1",nativeQuery = true)
    List<Model> findByIdCategory(Integer idCategory);
    @Transactional
    @Modifying
    @Query(value="DELETE FROM model m WHERE m.category =?1", nativeQuery = true)
    void deleteByCategory (Integer idCategory);
}

