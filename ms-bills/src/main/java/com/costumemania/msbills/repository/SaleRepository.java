package com.costumemania.msbills.repository;

import com.costumemania.msbills.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Integer> {
    List<Sale> findByStatus(Integer id);

    List<Sale> findByModel(Integer id);

    List<Sale> findByCatalog(Integer id);

    List<Sale> findBySize(Integer id);
}
