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

    @Query(value="select * from catalog c inner join model m on c.model=m.id_model inner join size s on c.size=s.id_size inner join category cat on m.category=cat.id_category inner join statuscomponent st on c.status_Catalog=st.id_status order by c.model", nativeQuery = true)
    List<Catalog> findAll ();
    @Query(value="select * from catalog c inner join model m on c.model=m.id_model inner join size s on c.size=s.id_size inner join category cat on m.category=cat.id_category WHERE c.model =?1", nativeQuery = true)
    Optional<List<Catalog>> findByModel (Integer idModel);
    @Query(value="select * from catalog c inner join model m on c.model=m.id_model inner join size s on c.size=s.id_size inner join category cat on m.category=cat.id_category WHERE c.model =?1 AND c.status_catalog=1", nativeQuery = true)
    Optional<List<Catalog>> findActiveByModel (Integer idModel);
    Optional<Catalog> findById (Integer id);
    List<Catalog> findBySize (Size size);
    @Query(value="select * from catalog c inner join model m on c.model=m.id_model inner join size s on c.size=s.id_size inner join category cat on m.category=cat.id_category WHERE s.adult =?1 AND c.model =?2 AND c.status_catalog=1", nativeQuery = true)
    List<Catalog> findActiveBySize (Integer bollean, Integer idModel);

    @Query(value="select * from catalog c inner join model m on c.model=m.id_model inner join size s on c.size=s.id_size inner join category cat on m.category=cat.id_category WHERE m.category =?1 AND c.model =?2 AND c.status_catalog=1", nativeQuery = true)
    List<Catalog> findActiveByCategory (Integer category, Integer idModel);

    @Query(value="select * from catalog c inner join model m on c.model=m.id_model inner join size s on c.size=s.id_size inner join category cat on m.category=cat.id_category WHERE c.model=?1 and s.adult=?2 and s.no_size=?3", nativeQuery = true)
    Optional<Catalog> validateCreate (Integer idModel, Integer booleanAdult, String size);

    @Query(value="SELECT * FROM catalog c INNER JOIN model m ON c.model=m.id_model WHERE c.model =?1 AND c.size =?2", nativeQuery = true)
    Optional<List<Catalog>> findByModelAndSize (Integer idModel, Integer size);

    @Transactional
    @Modifying
    @Query(value="UPDATE catalog c inner join model m on c.model=m.id_model inner join category ca on m.category=ca.id_category SET c.status_catalog = 2 WHERE c.model=?1", nativeQuery = true)
    void inactiveByModel (Integer idModel);
    @Transactional
    @Modifying
    @Query(value="UPDATE catalog c inner join model m on c.model=m.id_model inner join category ca on m.category=ca.id_category SET c.status_catalog = 2 WHERE m.category=?1", nativeQuery = true)
    void inactiveByCategory (Integer idCategory);
}
