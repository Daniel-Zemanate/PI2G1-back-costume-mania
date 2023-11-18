package com.costumemania.msbills.repository;

import com.costumemania.msbills.model.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping,Integer> {
    @Query(value ="SELECT * FROM shipping s WHERE s.destination LIKE %?1%",nativeQuery = true)
    Optional<List<Shipping>> findByDestination(String destination);
}