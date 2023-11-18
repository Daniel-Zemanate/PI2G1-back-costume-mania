package com.costumemania.msbills.repository;

import com.costumemania.msbills.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
}