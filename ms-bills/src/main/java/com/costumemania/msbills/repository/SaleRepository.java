package com.costumemania.msbills.repository;

import com.costumemania.msbills.model.Sale;
import com.costumemania.msbills.model.Status;
import com.costumemania.msbills.model.requiredEntity.User;
import com.costumemania.msbills.model.requiredEntity.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
    Optional<List<Sale>> getByStatus (Status status);

    Optional<List<Sale>> getByUser (User user);

    @Query(value = "SELECT s.id_sale, s.no_invoice, s.user, s.catalog, s.quantity, s.shipping_address, s.shipping_city, s.status, s.sale_date, s.shipping_date , c.id_catalog, c.model, c.size, c.price FROM sale s inner join catalog c on s.catalog=c.id_catalog where c.model=?1", nativeQuery = true)
    Optional<List<Sale>> getByModel (Integer idModel);
    Optional<List<Sale>> getByCatalog (Catalog catalog);
    @Query(value = "SELECT s.id_sale, s.no_invoice, s.user, s.catalog, s.quantity, s.shipping_address, s.shipping_city, s.status, s.sale_date, s.shipping_date , c.id_catalog, c.model, c.size, c.price FROM sale s inner join catalog c on s.catalog=c.id_catalog inner join size si on c.size=si.id_size where si.adult=?1", nativeQuery = true)
    Optional<List<Sale>> getBySize (Integer idSize);
    @Query(value = "SELECT * FROM sale where (sale_date between ?1 and ?2) order by no_invoice desc", nativeQuery = true)
    Optional<List<Sale>> getInvoiceInDates (String inDate, String finDate);

    @Query(value = "SELECT no_invoice FROM sale order by no_invoice desc limit 1", nativeQuery = true)
    Integer getLastInvoice ();
    @Query(value = "SELECT no_invoice FROM sale order by no_invoice asc limit 1", nativeQuery = true)
    Integer getFirstInvoice ();

    @Query(value = "SELECT * FROM sale order by no_invoice desc", nativeQuery = true)
    List<Sale> getAllByInvoice ();
    @Query(value = "SELECT * FROM sale where no_invoice=?1", nativeQuery = true)
    Optional<List<Sale>> getByInvoice (Integer invoice);
}