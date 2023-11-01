package com.costumemania.mscatalog.repository;

import com.costumemania.mscatalog.model.Catalog;
import com.costumemania.mscatalog.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Integer> {

    List<Catalog> findAll ();
    Optional<Catalog> findById (Integer id);
    List<Catalog> findBySize (Size size);
    @Query(value="SELECT * FROM catalog c ORDER BY c.id_catalog DESC LIMIT 10", nativeQuery = true)
    List<Catalog> findNews ();
}