package com.costumemania.msbills.controller;

import com.costumemania.msbills.model.Shipping;
import com.costumemania.msbills.service.ShippingService;
import jakarta.ws.rs.Path;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/shipping")
public class ShippingController {

    private final ShippingService shippingService;
    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    // public
    @GetMapping
    public ResponseEntity<List<Shipping>> getAllShipping(){
        return ResponseEntity.ok(shippingService.getAllShipping());
    }

    // public
    @GetMapping("/{id}")
    public ResponseEntity<Shipping> getByShipping(@PathVariable Integer id){
        //verify Shipping empty
        Optional<Shipping> shipping=shippingService.getByIdShipping(id);
        if(shipping.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(shipping.get());
    }

    // public
    @GetMapping("/destination/{destination}")
    public ResponseEntity<List<Shipping>> getByDestinationShipping(@PathVariable String destination){
        //verify list by destination
        Optional<List<Shipping>> shipping = shippingService.getByDestinationShipping(destination);
        if(shipping.get().isEmpty()){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(shipping.get());
    }

    // adm
    @PostMapping("/create")
    public ResponseEntity<Shipping> createShipping(@RequestBody Shipping shipping){
        // verify if already exist
        Optional<List<Shipping>> searchShipping = shippingService.getByDestinationShipping(shipping.getDestination());
        if (!searchShipping.get().isEmpty()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        // verify cost isn´t empty
        if (shipping.getCost()==null) {
            shipping.setCost(0.0f);
        }
        // else 202
       return ResponseEntity.accepted().body(shippingService.saveShipping(shipping));
    }

    // adm
    @PutMapping("/{id}")
    public ResponseEntity<Shipping> updateShipping (@PathVariable Integer id, @RequestBody Shipping s) {
        // first verify if the ID exist
        Optional<Shipping> shippingProof = shippingService.getByIdShipping(id);
        if (shippingProof.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // verify if the new destination already exists
        Optional<List<Shipping>> newShipping = shippingService.getByDestinationShipping(s.getDestination());
        if (!newShipping.get().isEmpty()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        // cost
        if (s.getCost()==null) {
            s.setCost(0.0f);
        }
        // else
        s.setIdShippping(id);
        return ResponseEntity.ok().body(shippingService.saveShipping(s));
    }
}