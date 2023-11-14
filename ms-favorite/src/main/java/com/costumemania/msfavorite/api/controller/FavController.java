package com.costumemania.msfavorite.api.controller;

import com.costumemania.msfavorite.client.IProductClient;
import com.costumemania.msfavorite.model.Fav;
import com.costumemania.msfavorite.api.service.FavService;
import com.costumemania.msfavorite.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/fav")
public class FavController {

     @Autowired
     FavService favService;


     @Autowired
    IProductClient iProductClient;

    @GetMapping
    public ResponseEntity<List<Fav>> getAll(){
        return ResponseEntity.ok().body(favService.getFav());
    }

    @DeleteMapping("/{idFav}")
    public ResponseEntity<String> delete(@PathVariable Integer idFav) {


        Optional <Fav> fav = favService.getFavById(idFav);
        if (fav.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        favService.DeleteById(idFav);
        return ResponseEntity.ok().body("Fav with ID " + idFav + " deleted");
    }

    @PostMapping()
    public ResponseEntity<Fav> addFav(@RequestBody Fav fav){

        favService.save(fav);

        return ResponseEntity.status(HttpStatus.CREATED).body(fav);
    }


    @GetMapping("/{idUser}")
    public ResponseEntity<List<Object>> getByUser(@PathVariable Integer idUser) {

        List<Object> f = favService.getByUser(idUser);
        if(f.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(favService.getByUser(idUser));
    }

}
