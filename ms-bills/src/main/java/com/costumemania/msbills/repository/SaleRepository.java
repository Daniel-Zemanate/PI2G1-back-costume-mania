package com.costumemania.msbills.repository;

import com.costumemania.msbills.model.Sale;
import com.costumemania.msbills.model.Status;
import com.costumemania.msbills.model.requiredEntity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
    Optional<List<Sale>> getByStatus (Status status);

    Optional<List<Sale>> getByUser (User user);

    @Query(value = "SELECT s.id_sale, s.no_invoice, s.user, s.catalog, s.quantity, s.shipping_address, s.shipping_city, s.status, s.sale_date, s.shipping_date , c.id_catalog, c.model, c.size, c.price FROM sale s inner join catalog c on s.catalog=c.id_catalog where c.model=?1", nativeQuery = true)
    Optional<List<Sale>> getByModel (Integer idModel);


    @Query(value = "SELECT * FROM costumemania.sale s inner join model m on s.model=m.id_model inner join catalog c on s.model= c.model where c.id_catalog=?1", nativeQuery = true)
    Optional<List<Sale>> getByCatalog (Integer idCatalog);

    @Query(value = "SELECT * FROM costumemania.sale s inner join model m on s.model=m.id_model inner join catalog c on s.model= c.model inner join size si on c.size=si.id_size where si.adult=?1", nativeQuery = true)
    Optional<List<Sale>> getBySize (Integer idSize);
}