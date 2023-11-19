package com.costumemania.msfavorite.repository;

import com.costumemania.msfavorite.DTO.FavDTO;
import com.costumemania.msfavorite.model.Fav;
import com.costumemania.msfavorite.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface IFavRepository extends JpaRepository<Fav, Integer> {

    List<Fav> findAll();
    Optional<Fav> findById(Integer id);

    @Query("SELECT f.model FROM Fav f " +
            "WHERE f.users = :user")
    List<Integer> findByUser(Integer user);

    @Query("SELECT f FROM Fav f " +
            "WHERE f.users = :user")
    List<Fav> findFavByUser(Integer user);






}
