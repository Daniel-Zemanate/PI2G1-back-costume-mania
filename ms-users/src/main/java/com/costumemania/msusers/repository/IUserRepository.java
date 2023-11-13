package com.costumemania.msusers.repository;

import com.costumemania.msusers.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findOneByEmail(String username);

    Optional<UserEntity> findByDni(String username);
}
