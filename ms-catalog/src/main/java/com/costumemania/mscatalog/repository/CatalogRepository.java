package com.costumemania.mscatalog.repository;

import com.costumemania.mscatalog.model.Catalog;
import com.costumemania.mscatalog.model.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Integer> {

    @Query(value="select * from catalog c inner join model m on c.model=m.id_model inner join size s on c.size=s.id_size inner join category cat on m.category=cat.id_category order by c.model", nativeQuery = true)
    List<Catalog> findAll ();
    @Query(value="select * from catalog c inner join model m on c.model=m.id_model inner join size s on c.size=s.id_size inner join category cat on m.category=cat.id_category WHERE c.model =?1", nativeQuery = true)
    Optional<List<Catalog>> findByModel (Integer idModel);
    @Query(value="select * from catalog c inner join model m on c.model=m.id_model inner join size s on c.size=s.id_size inner join category cat on m.category=cat.id_category WHERE c.model =?1 AND c.status_catalog=1", nativeQuery = true)
    Optional<List<Catalog>> findActiveByModel (Integer idModel);

    Optional<Catalog> findById (Integer id);
    @Query(value="SELECT * FROM catalog c WHERE c.id_catalog =?1", nativeQuery = true)
    Catalog findByIdSEC (Integer id);

    List<Catalog> findBySize (Size size);
    @Query(value="select * from catalog c inner join model m on c.model=m.id_model inner join size s on c.size=s.id_size inner join category cat on m.category=cat.id_category WHERE s.adult =?1 AND c.model =?2", nativeQuery = true)
    List<Catalog> findBySize (Integer bollean, Integer idModel);
    @Query(value="select * from catalog c inner join model m on c.model=m.id_model inner join size s on c.size=s.id_size inner join category cat on m.category=cat.id_category WHERE s.adult =?1 AND c.model =?2 AND c.status_catalog=1", nativeQuery = true)
    List<Catalog> findActiveBySize (Integer bollean, Integer idModel);

    @Query(value="select * from catalog c inner join model m on c.model=m.id_model inner join size s on c.size=s.id_size inner join category cat on m.category=cat.id_category WHERE m.category =?1 AND c.model =?2", nativeQuery = true)
    List<Catalog> findByCategory (Integer category, Integer idModel);
    @Query(value="select * from catalog c inner join model m on c.model=m.id_model inner join size s on c.size=s.id_size inner join category cat on m.category=cat.id_category WHERE m.category =?1 AND c.model =?2 AND c.status_catalog=1", nativeQuery = true)
    List<Catalog> findActiveByCategory (Integer category, Integer idModel);

    @Query(value="select * from catalog c inner join model m on c.model=m.id_model inner join size s on c.size=s.id_size inner join category cat on m.category=cat.id_category WHERE c.model=?1 and s.adult=?2 and s.no_size=?3", nativeQuery = true)
    Optional<Catalog> validateCreate (Integer idModel, Integer booleanAdult, String size);

    @Query(value="SELECT * FROM catalog c INNER JOIN model m ON c.model=m.id_model WHERE c.model =?1", nativeQuery = true)
    Optional<Page<Catalog>> findByModelPageable (Integer idModel, Pageable pageable);
    @Query(value="SELECT * FROM catalog c INNER JOIN model m ON c.model=m.id_model WHERE c.model =?1 AND c.size =?2", nativeQuery = true)
    Optional<Catalog> findByModelAndSize (Integer idModel, Integer size);
    @Query(value="SELECT * FROM catalog c WHERE c.status_catalog=1 ORDER BY c.id_catalog DESC LIMIT 8", nativeQuery = true)
    List<Catalog> findNews ();
    @Transactional
    @Modifying
    @Query(value="DELETE FROM catalog c WHERE c.model =?1", nativeQuery = true)
    void deleteByModel (Integer idModel);
}
