package com.costumemania.mscatalog.repository;

import com.costumemania.mscatalog.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
    List<Size> findAll();
    Optional<Size> findById (Long id);
    List<Size> findAllByAdultOrChild(Integer adult);
}
