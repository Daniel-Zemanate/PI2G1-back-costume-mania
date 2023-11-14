package com.costumemania.msbills.controller;

import com.costumemania.msbills.model.Shipping;
import com.costumemania.msbills.service.ShippingService;
import jakarta.ws.rs.Path;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/shipping")
public class ShippingController {

    private final ShippingService shippingService;


    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    public ResponseEntity<List<Shipping>> getAllShipping(){
        return ResponseEntity.ok(shippingService.getAllShipping());
    }

   @GetMapping("/{id}")
    public ResponseEntity<Optional<Shipping>> getByShipping(@PathVariable Integer id){

        //verify Shipping empty

       Optional<Shipping> shipping=shippingService.getByIdShipping(id);
       if(shipping.isEmpty()){
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok().body(shipping);
   }

   @GetMapping("/destination/{destination}")
    public ResponseEntity<Optional<List<Shipping>>> getByDestinationShipping(@PathVariable String destination){

        //verify list by destination
       Optional<List<Shipping>> shipping = shippingService.getByDestinationShipping(destination);
       if(shipping.isEmpty()){
           return  ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok().body(shipping);
   }

   @DeleteMapping("/delete/{idShipping}")
        public ResponseEntity<String> deleteShipping(@PathVariable Integer id){

            //verify shipping empty
           Optional<Shipping> shipping= shippingService.getByIdShipping(id);
           if(shipping.isEmpty()) {
               return ResponseEntity.noContent().build();
           }
           shippingService.deleteShipping(id);
           return ResponseEntity.ok().body("Shipping item with ID" + id + "deleted.");
   }
}
