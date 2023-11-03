package com.costumemania.mscatalog.repository;

import com.costumemania.mscatalog.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {
    List<Size> findAll();
    Optional<Size> findById (Integer id);
    @Query(value="SELECT * FROM size c WHERE c.id_size =?1", nativeQuery = true)
    Size findByIdSEC (Integer id);
    List<Size> findAllByAdult(Integer adult);
}
