package com.costumemania.msbills.repository;

import com.costumemania.msbills.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Integer> {

    @Query(value = "SELECT * FROM status st INNER JOIN sale s ON st.id_status=s.status WHERE s.id_sale = %?1%",nativeQuery = true)
    Optional<Status> findByIdSale(Integer id);
    @Query(value = "SELECT * FROM status st INNER JOIN sale s ON st.id_status=s.status ",nativeQuery = true)
    Optional<List<Status>> findAllBySale();
}
