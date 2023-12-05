package com.costumemania.msfavorite.api.controller;

import com.costumemania.msfavorite.DTO.FavDTO;
import com.costumemania.msfavorite.DTO.FavModelDTO;
import com.costumemania.msfavorite.client.ICatalogClient;
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

    @Autowired
    ICatalogClient iCatalogClient;


    // ADMIN
    @GetMapping
    public ResponseEntity<?> getAll() {

        try {
            return ResponseEntity.ok().body(favService.getFav());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Your request could not be processed. ms-product not availabe");
        }

    }

    //USER
    @DeleteMapping("/{idFav}")
    public ResponseEntity<String> delete(@PathVariable Integer idFav) {


        Optional<Fav> fav = favService.getFavById(idFav);
        if (fav.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        favService.DeleteById(idFav);
        return ResponseEntity.ok().body("Fav with ID " + idFav + " deleted");

    }

    // USER
    @PostMapping()
    public ResponseEntity<?> addFav(@RequestBody Fav fav) {

        Integer us = favService.findUser(fav.getUsers());
        Integer mod = favService.findModel(fav.getModel());

        System.out.println("us : " + us + " mod: "+mod);
        if (us == 1 && mod == 1) {

            favService.save(fav);
            return ResponseEntity.status(HttpStatus.CREATED).body(fav);
        }
        if (us == 0 && mod == 1) {
            return ResponseEntity.status(404).body("Your request could not be processed." + " user id " + fav.getUsers() + " dont exist");
        }

        if (us == 1 && mod == 0) {
            return ResponseEntity.status(404).body("Your request could not be processed." + " model id " + fav.getModel() + " dont exist");
        }

        if(us == 99 && mod == 99){
            return ResponseEntity.status(404).body("ms-product and ms-users not availabe");
        }


        return ResponseEntity.status(404).body("One of the microservices is not available and/or item dont exist " + '\n'
                                               + "ms-user code: " + us + '\n'
                                               + "ms-product code: " + mod + '\n'
                                               + "Error code:" + '\n'
                                               + "               Code error 99 = service not availabe"+ '\n'
                                               + "               Code error 0 = item dont exist" + '\n'
                                               + "               Code error 1 = item exist");

    }


    // USER

    @GetMapping("/user/{idUser}")
    public ResponseEntity<Object> getByUser(@PathVariable Integer idUser) {


            Integer us = favService.findUser(idUser);
            Integer mod  = favService.findModel(us);
            if (us == 1) {

                if(mod != 99) {

                    List<Object> f = favService.getByUser(idUser);
                    if (f.isEmpty()) {
                        return ResponseEntity.status(404).body("User id " + idUser + " dont have favorites");
                    }
                    return ResponseEntity.ok().body(favService.getByUser(idUser));
                }

                else   return ResponseEntity.internalServerError().body("Your request could not be processed. ms-product not availabe");

            }



            if (us == 99 && mod !=99) {
                return ResponseEntity.internalServerError().body("Your request could not be processed. ms-user not availabe");
            }
            if (mod == 99 && us !=99) {
                return ResponseEntity.internalServerError().body("Your request could not be processed. ms-product not availabe");
            }


            if(us == 99 && mod == 99){
                    return ResponseEntity.internalServerError().body("not availabe ms product and user");
                }
            if (us == 0) {
                return ResponseEntity.badRequest().body("User id " + idUser + " not found");
            }
            return ResponseEntity.badRequest().build();




    }


    //ADMIN
    @GetMapping("/FavModel")
    public ResponseEntity<?> FavOrderModel(){

        try {
            return ResponseEntity.ok().body(favService.FavOrderModel());

        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body("Your request could not be processed. ms-product not availabe" );
        }


    }

    @GetMapping("/FavModelV2")
    public ResponseEntity<?> FavOrderModelv2(){

        Integer cat = favService.findCatalog(1);

        if(cat == 1 || cat == 0){

            try {
                Integer m = favService.findModel(1);
                if(m == 99){
                    return ResponseEntity.internalServerError().body("Your request could not be processed. ms-product not availabe");

                }
                else return ResponseEntity.ok().body(favService.FavOrderModelV2());
            }
            catch (Exception e){
                return  ResponseEntity.internalServerError().body("Your request could not be processed");
            }

        }

        else   return ResponseEntity.internalServerError().body("Your request could not be processed. ms-catalog not availabe");





    }

}
