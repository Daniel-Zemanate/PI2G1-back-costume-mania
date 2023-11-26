package com.costumemania.msfavorite.api.controller;

import com.costumemania.msfavorite.DTO.FavDTO;
import com.costumemania.msfavorite.DTO.FavModelDTO;
import com.costumemania.msfavorite.client.IProductClient;
import com.costumemania.msfavorite.model.Fav;
import com.costumemania.msfavorite.api.service.FavService;
import com.costumemania.msfavorite.model.Model;
import jakarta.ws.rs.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/fav")
public class FavController {

     @Autowired
     FavService favService;


     @Autowired
    IProductClient iProductClient;


    // ADMIN
    @GetMapping
    public ResponseEntity<List<Object>> getAll(){
        return ResponseEntity.ok().body(favService.getFav());
    }

    //USER
    @DeleteMapping("/{idFav}")
    public ResponseEntity<String> delete(@PathVariable Integer idFav) {


        Optional <Fav> fav = favService.getFavById(idFav);
        if (fav.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        favService.DeleteById(idFav);
        return ResponseEntity.ok().body("Fav with ID " + idFav + " deleted");

    }

    // USER
    @PostMapping()
    public ResponseEntity<?> addFav(@RequestBody Fav fav){

        if(favService.findUser(fav.getUsers()).equals(true)){

            favService.save(fav);
            return ResponseEntity.status(HttpStatus.CREATED).body(fav);
        }
        else
            return ResponseEntity.status(404).body("Your request could not be processed." + " user id " + fav.getUsers() + " dont exist");


    }


    // USER

    @GetMapping("/user/{idUser}")
    public ResponseEntity<Object> getByUser(@PathVariable Integer idUser) {

        if(favService.findUser(idUser).equals(true)){


                List<Object> f = favService.getByUser(idUser);
                if (f.isEmpty()) {
                    return ResponseEntity.status(404).body("User id " + idUser + " dont have favorites");
                }
                return ResponseEntity.ok().body(favService.getByUser(idUser));
        }

        else return ResponseEntity.badRequest().body("User id " + idUser + " not found");

    }


    //ADMIN
    @GetMapping("/FavModel")
    public ResponseEntity<?> FavOrderModel(){

        try {
            return ResponseEntity.ok().body(favService.FavOrderModel());

        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body("Your request could not be processed. Error ms-product" );
        }


    }

}
