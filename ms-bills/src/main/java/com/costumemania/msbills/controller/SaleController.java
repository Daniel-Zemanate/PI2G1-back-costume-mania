package com.costumemania.msbills.controller;

import com.costumemania.msbills.model.Sale;
import com.costumemania.msbills.model.Shipping;
import com.costumemania.msbills.model.Status;
import com.costumemania.msbills.model.requiredEntity.Catalog;
import com.costumemania.msbills.model.requiredEntity.User;
import com.costumemania.msbills.service.CatalogService;
import com.costumemania.msbills.service.SaleService;
import com.costumemania.msbills.service.ShippingService;
import com.costumemania.msbills.service.StatusService;
import feign.FeignException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/sale")
public class SaleController {

    private final SaleService saleService;
    private final StatusService statusService;
    private final CatalogService catalogService;
    private final ShippingService shippingService;
    public SaleController(SaleService saleService, StatusService statusService, CatalogService catalogService, ShippingService shippingService) {
        this.saleService = saleService;
        this.statusService = statusService;
        this.catalogService = catalogService;
        this.shippingService = shippingService;
    }


    ///////////////////------- BILLS GETTERS -------///////////////////

    // adm
    @GetMapping
    public ResponseEntity<List<Sale>>getAllSales(){
        return ResponseEntity.ok(saleService.getAllSales());
    }

    // adm
    @GetMapping("/page/{page}")
    public Page<Sale> getAllSales(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 12);
        return saleService.getAllSales(pageable);
    }

    // adm
    @GetMapping("/{idSale}")
    public ResponseEntity<Optional<Sale>> getById(@PathVariable Integer idSale){
        // first verify if the ID exist
        Optional<Sale> saleProof = saleService.getById(idSale);
        if (saleProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // else...
        return ResponseEntity.ok().body(saleProof);
    }

    // adm
    @GetMapping("/status/{idStatus}")
    public ResponseEntity<List<Sale>> getByStatus (@PathVariable Integer idStatus){
        // first verify if the ID exist
        Optional<Status> statusProof = statusService.getById(idStatus);
        if (statusProof.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // else...
        Optional<List<Sale>> saleList = saleService.getByStatus(statusProof.get());
        if (saleList.get().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(saleList.get());
    }

    // user + adm
    // - AGREGAR EL FEIGN CUANDO ANDE USER!!!!
    /*@GetMapping("/user/{idUser}")
    public ResponseEntity<List<Sale>> getByStatus (@PathVariable Integer idUser){
        // first verify if the ID exist - AGREGAR EL FEIGN CUANDO ANDE USER!!!!
        /* TRY { Optional<User> userProof = userService.getById(idUser);
        } CATCH FEIGN EXCEPTION {
            return ResponseEntity.notFound().build();
        }
        // else...
        Optional<List<Sale>> saleList = saleService.getByUser(userProof);
        if (saleList.get().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(saleList.get());
    }*/

    // adm
    @GetMapping("/model/{idModel}")
    public ResponseEntity<List<Sale>> getByModel (@PathVariable Integer idModel){
        // verify if model exists
        List<Catalog> modelProof;
        try {
            modelProof = catalogService.getByModel2(idModel).getBody().get();
        } catch (FeignException e) {
            return ResponseEntity.notFound().build();
        }
        // else
        Optional<List<Sale>> saleList = saleService.getByModel(idModel);
        if (saleList.get().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(saleList.get());
    }

    // adm
    @GetMapping("/catalog/{idCatalog}")
    public ResponseEntity<List<Sale>> getByCatalog (@PathVariable Integer idCatalog){
        Catalog catalogProof;
        try {
            catalogProof = catalogService.getById(idCatalog).getBody().get();
        } catch (FeignException e) {
            return ResponseEntity.notFound().build();
        }
        Optional<List<Sale>> saleList = saleService.getByCatalog(catalogProof);
        if (saleList.get().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(saleList.get());
    }

    // adm
    @GetMapping("/size/{idSize}")
    public ResponseEntity<List<Sale>> getBySize (@PathVariable Integer idSize){
        // first verify if the boolean exist
        List<Catalog> catalogProof;
        try {
            catalogProof = catalogService.getBySize(idSize).getBody();
        } catch (FeignException e) {
            return ResponseEntity.notFound().build();
        }
        // else...
        Optional<List<Sale>> saleList = saleService.getBySize(idSize);
        if (saleList.get().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(saleList.get());
    }

    //////////////////////////////////////////////////////////////////////

    ///////////////////------- BILLS GENERATORS -------///////////////////

    private class Response {
        private int status;
        private String message;
        private float quantity;

        public int getStatus() {
            return status;
        }
        public void setStatus(int status) {
            this.status = status;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
        public float getQuantity() {
            return quantity;
        }
        public void setQuantity(float quantity) {
            this.quantity = quantity;
        }
    }

    private Response quantityEnough (Integer idCatalog, Integer q) {
        Response response = new Response();
        // verify catalog
        Catalog catalogProof;
        try {
            catalogProof = catalogService.getById(idCatalog).getBody().get();
        } catch (FeignException e) {
            response.setStatus(404);
            response.setMessage("Catalog doesn´t exists");
            return response;
        }
        if (catalogProof.getStatusCatalog().getId()==2) {
            response.setStatus(422);
            response.setMessage("Catalog isn´t active");
            return response;
        }
        if (catalogProof.getStock()-q<0) {
            response.setStatus(400);
            response.setMessage("Quantity in stock for ID Catalog " + idCatalog + " isn´t enough, There are only " + catalogProof.getStock() + " items");
            response.setQuantity(catalogProof.getStock());
            return response;
        }
        response.setStatus(200);
        response.setMessage("Enough stock");
        response.setQuantity(catalogProof.getStock()-q);
        return response;
    }

    private Response PxQ (Integer idCatalog, Integer q) {
        Response response = new Response();
        Catalog catalogProof;
        try {
            catalogProof = catalogService.getById(idCatalog).getBody().get();
        } catch (FeignException e) {
            response.setStatus(500);
            response.setMessage("Server problems");
            return response;
        }
        response.setStatus(200);
        response.setQuantity(catalogProof.getPrice()*q);
        return response;
    }

    private Response shippingCost (Integer idCity) {
        Response response = new Response();
        Optional<Shipping> shippingProof = shippingService.getByIdShipping(idCity);
        if (shippingProof.isEmpty()) {
            response.setStatus(404);
            response.setMessage("CityID doesn´t exists");
            return response;
        }
        response.setStatus(200);
        response.setQuantity(shippingProof.get().getCost());
        return response;
    }

    public static class ItemSold {
        private Integer catalog;
        private Integer quantitySold;

        public Integer getCatalog() {
            return catalog;
        }
        public Integer getQuantitySold() {
            return quantitySold;
        }
    }
    public static class SaleRequired {
        List<ItemSold> itemSoldList;
        Integer city;

        public List<ItemSold> getItemSoldList() {
            return itemSoldList;
        }
        public void setItemSoldList(List<ItemSold> itemSoldList) {
            this.itemSoldList = itemSoldList;
        }
        public Integer getCity() {
            return city;
        }
    }

    // user - To verify if purchase is possible
    @PostMapping("/startSale")
    public ResponseEntity<String> startSale (@RequestBody SaleRequired body){
        float semiTotal=0.0f;
        String messageFinal = "";
        for (ItemSold itemSold : body.getItemSoldList()) {
            Response resp = quantityEnough(itemSold.getCatalog(), itemSold.getQuantitySold());
            if (resp.getStatus()==400) {
                return ResponseEntity.unprocessableEntity().body(resp.getMessage());
            }
            if (resp.getStatus()!=200) {
                return ResponseEntity.unprocessableEntity().body("We can´t process your purchase");
            }
            Response resp2 = PxQ(itemSold.getCatalog(), itemSold.getQuantitySold());
            if (resp2.getStatus()!=200) {
                return ResponseEntity.unprocessableEntity().body("We can´t process your purchase");
            }
            semiTotal += resp2.getQuantity();
            messageFinal += "Catalog ID " + itemSold.getCatalog() + " x " + itemSold.getQuantitySold() + " = $" + resp2.getQuantity() + "\n";
        }
        Response resp =shippingCost(body.getCity());
        if (resp.getStatus()!=200) {
            return ResponseEntity.unprocessableEntity().body("We can´t process your purchase");
        }
        messageFinal += "Shipping Cost: $" + resp.getQuantity() + "\n";
        float total = semiTotal + resp.getQuantity();
        return ResponseEntity.ok(messageFinal + "Total: $" + total);
    }

    // user - To create bill
    @PostMapping("/create")
    public ResponseEntity<List<Sale>> createBill (@RequestBody SaleRequired body){
        ResponseEntity<String> billValidate = startSale(body);
        if (billValidate.getStatusCode()== HttpStatus.OK) {
            List<Sale> results = new ArrayList<>();
            for (ItemSold itemSold : body.getItemSoldList()) {
                Catalog catalogProof;
                try {
                    catalogProof = catalogService.getById(itemSold.getCatalog()).getBody().get();
                } catch (FeignException e) {
                    return ResponseEntity.internalServerError().build();
                }
                try {
                    catalogService.catalogSold(itemSold.getCatalog(), itemSold.getQuantitySold());
                } catch (FeignException e) {
                    return ResponseEntity.unprocessableEntity().build();
                }
                Sale s = new Sale(0005, // todo: recordar buscar el ultimo invoice
                        1, // todo: esto solo funciona porque le saque el "not null" de la bbdd y la foreign key. El user es un integer nada mas
                        catalogProof,
                        itemSold.getQuantitySold(),
                        "domicilio de prueba",
                        shippingService.getByIdShipping(body.getCity()).get(),
                        LocalDateTime.now(),
                        new Status(1,"En proceso"));

                results.add(saleService.create(s));
            }
            return ResponseEntity.ok(results);
        }
        return ResponseEntity.badRequest().build();
    }

    //////////////////////////////////////////////////////////////////////

}