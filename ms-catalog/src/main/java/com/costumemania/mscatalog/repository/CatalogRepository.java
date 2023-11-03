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
    @Query(value="SELECT * FROM catalog c WHERE c.id_catalog =?1", nativeQuery = true)
    Catalog findByIdSEC (Integer id);
    List<Catalog> findBySize (Size size);
    @Query(value="SELECT * FROM catalog c ORDER BY c.id_catalog DESC LIMIT 10", nativeQuery = true)
    List<Catalog> findNews ();
    @Query(value = "SELECT * FROM catalog c WHERE c.model =?1", nativeQuery = true)
    List<Catalog> findByIdModel(Integer idModel);
    @Query(value="SELECT * FROM catalog c LEFT JOIN model m ON c.model = m.id_model WHERE m.name_model like %?1%", nativeQuery = true)
    List<Catalog> findByNameModel(String nameModel);
    @Query(value="SELECT * FROM catalog c Left join model m on c.model = m.id_model where m.category=2 and m.name_model like %?1%", nativeQuery = true)
    List<Catalog> findByIdCategoryNameModel(Integer idCategory, String nameModel);
}
