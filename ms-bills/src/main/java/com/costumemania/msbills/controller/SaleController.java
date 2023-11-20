package com.costumemania.msbills.controller;

import com.costumemania.msbills.model.Sale;
import com.costumemania.msbills.model.Shipping;
import com.costumemania.msbills.model.Status;
import com.costumemania.msbills.model.requiredEntity.Catalog;
import com.costumemania.msbills.model.requiredEntity.User;
import com.costumemania.msbills.service.*;
import feign.FeignException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/sale")
public class SaleController {

    private final SaleService saleService;
    private final StatusService statusService;
    private final CatalogService catalogService;
    private final ShippingService shippingService;
    private final UserService userService;
    public SaleController(SaleService saleService, StatusService statusService, CatalogService catalogService, ShippingService shippingService, UserService userService) {
        this.saleService = saleService;
        this.statusService = statusService;
        this.catalogService = catalogService;
        this.shippingService = shippingService;
        this.userService = userService;
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
//

    // user + adm
    @GetMapping("/user/{idUser}")
    public ResponseEntity<List<Sale>> getByUser (@PathVariable Integer idUser){
        try {
            ResponseEntity<?> userProof = userService.userById(idUser);
            if (userProof.getStatusCode()==HttpStatus.NOT_FOUND) {
                return ResponseEntity.notFound().build();
            }
        } catch (FeignException e) {
            return ResponseEntity.internalServerError().build();
        }
        Optional<List<Sale>> saleList = saleService.getByUser(idUser);
        if (saleList.get().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(saleList.get());
    }

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

    // adm
    @GetMapping("/dates/{date1}/{date2}")
    public ResponseEntity<List<Sale>> getByDates (@PathVariable String date1, @PathVariable String date2) {
        Optional<List<Sale>> list = saleService.getInvoiceInDates(date1, date2);
        if (list.get().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list.get());
    }

    ////////////////////////// GET INVOICES /////////////////////////

    // class to read an invoice
    private class Invoice {
        private Integer no_invoice;
        private String status;
        private List<ItemInvoice> items;
        private String shippingCity;
        private float shippingcost;
        private float total;

        private static class ItemInvoice {
            private Integer catalog;
            private String model;
            private Integer quantity;
            private float price;
            private float PxQ;

            public Integer getCatalog() {
                return catalog;
            }
            public void setCatalog(Integer catalog) {
                this.catalog = catalog;
            }
            public String getModel() {
                return model;
            }
            public void setModel(String model) {
                this.model = model;
            }
            public Integer getQuantity() {
                return quantity;
            }
            public void setQuantity(Integer quantity) {
                this.quantity = quantity;
            }
            public float getPrice() {
                return price;
            }
            public void setPrice(float price) {
                this.price = price;
            }
            public float getPxQ() {
                return PxQ;
            }
            public void setPxQ(float pxQ) {
                PxQ = pxQ;
            }
        }

        public void setNo_invoice(Integer no_invoice) {
            this.no_invoice = no_invoice;
        }
        public void setItems(List<ItemInvoice> items) {
            this.items = items;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public void setShippingCity(String shippingCity) {
            this.shippingCity = shippingCity;
        }
        public void setShippingcost(float shippingcost) {
            this.shippingcost = shippingcost;
        }
        public void setTotal(float total) {
            this.total = total;
        }
        public Integer getNo_invoice() {
            return no_invoice;
        }
        public String getStatus() {
            return status;
        }
        public List<ItemInvoice> getItems() {
            return items;
        }
        public String getShippingCity() {
            return shippingCity;
        }
        public float getShippingcost() {
            return shippingcost;
        }
        public float getTotal() {
            return total;
        }
    }

    // user + adm
    @GetMapping ("/invoice/{noInvoice}")
    public ResponseEntity<Invoice> getInvoiceByNo (@PathVariable Integer noInvoice) {
        Optional<List<Sale>> saleList = saleService.getByInvoice(noInvoice);
        if (saleList.get().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Invoice invoice = new Invoice();
        invoice.setNo_invoice(noInvoice);
        invoice.setStatus(saleList.get().get(0).getStatus().getStatus());
        invoice.setShippingCity(saleList.get().get(0).getCity().getDestination());
        invoice.setShippingcost(saleList.get().get(0).getCity().getCost());
        List<Invoice.ItemInvoice> items = new ArrayList<>();
        float finalCost = 0.0f;
        for (Sale sale : saleList.get()) {
            Invoice.ItemInvoice item = new Invoice.ItemInvoice();
            item.setCatalog(sale.getCatalog().getIdCatalog());
            item.setModel(sale.getCatalog().getModel().getNameModel());
            item.setQuantity(sale.getQuantity());
            item.setPrice(sale.getCatalog().getPrice());
            item.setPxQ(item.getQuantity()*item.getPrice());
            items.add(item);
            finalCost += item.getQuantity()*item.getPrice();
        }
        invoice.setItems(items);
        invoice.setTotal(finalCost += saleList.get().get(0).getCity().getCost());
        return ResponseEntity.ok(invoice);
    }

    // adm  -- todo: agregar a la base de datos de factura, el valor estático por unidad y por costo de envío para que no se actualice todo el tiempo.
    @GetMapping("/invoice")
    public ResponseEntity<List<Invoice>> getAllInvoices () {
        Integer lastInvoice = saleService.getLastInvoice();
        Integer firstInvoice = saleService.getFirstInvoice();
        List<Invoice> result = new ArrayList<>();
        for (int i=lastInvoice; i>firstInvoice; i--) {
            // ejecuta la busqueda por invoice
            if (getInvoiceByNo(i).getStatusCode()==HttpStatus.OK) {
                result.add(getInvoiceByNo(i).getBody());
            }
        }
        return ResponseEntity.ok(result);
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

    // body to use to validate sale
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
        String address;
        Integer user;

        public List<ItemSold> getItemSoldList() {
            return itemSoldList;
        }
        public void setItemSoldList(List<ItemSold> itemSoldList) {
            this.itemSoldList = itemSoldList;
        }
        public Integer getCity() {
            return city;
        }
        public String getAddress() {
            return address;
        }
        public Integer getUser() {
            return user;
        }
    }

    // response to validate sale
    @Getter
    @Setter
    public static class SaleResponse {
        List<ItemSoldWithCost> itemSoldList;
        float shippingCost;
        float total;
        String errorMessage;

        @Getter
        @Setter
        public static class ItemSoldWithCost {
            private Integer catalog;
            private Integer quantitySold;
            private float partialTotal;

            public ItemSoldWithCost(Integer catalog, Integer quantitySold, float partialTotal) {
                this.catalog = catalog;
                this.quantitySold = quantitySold;
                this.partialTotal = partialTotal;
            }
        }
    }

    // user - To verify if purchase is possible
    @PostMapping("/startSale")
    public ResponseEntity<SaleResponse> startSale (@RequestBody SaleRequired body){
        float semiTotal=0.0f;
        SaleResponse saleResponse = new SaleResponse();
        List<SaleResponse.ItemSoldWithCost> list = new ArrayList<>();

        Response resp = shippingCost(body.getCity());
        if (resp.getStatus()!=200) {
            saleResponse.setErrorMessage("We can´t process your purchase because of error with cost shipping");
            return ResponseEntity.unprocessableEntity().body(saleResponse);
        }
        saleResponse.setShippingCost(resp.getQuantity());

        for (ItemSold itemSold : body.getItemSoldList()) {
            Response resp2 = quantityEnough(itemSold.getCatalog(), itemSold.getQuantitySold());
            if (resp2.getStatus()==400) {
                saleResponse.setErrorMessage(resp2.getMessage());
                return ResponseEntity.unprocessableEntity().body(saleResponse);
            }
            if (resp2.getStatus()!=200) {
                saleResponse.setErrorMessage("We can´t process your purchase");
                return ResponseEntity.unprocessableEntity().body(saleResponse);
            }
            Response resp3 = PxQ(itemSold.getCatalog(), itemSold.getQuantitySold());
            if (resp3.getStatus()!=200) {
                saleResponse.setErrorMessage("We can´t process your purchase");
                return ResponseEntity.unprocessableEntity().body(saleResponse);
            }
            SaleResponse.ItemSoldWithCost item = new SaleResponse.ItemSoldWithCost(itemSold.getCatalog(), itemSold.getQuantitySold(), resp3.getQuantity());
            list.add(item);
            semiTotal += resp3.getQuantity();
        }

        saleResponse.setItemSoldList(list);
        saleResponse.setTotal(semiTotal+resp.getQuantity());
        return ResponseEntity.ok(saleResponse);
    }

    // user - To create bill
    @PostMapping("/create")
    public ResponseEntity<List<Sale>> createBill (@RequestBody SaleRequired body){
        // validate user
        try {
            ResponseEntity<?> userProof = userService.userById(body.getUser());
            if (userProof.getStatusCode()==HttpStatus.NOT_FOUND) {
                return ResponseEntity.badRequest().build();
            }
        } catch (FeignException e) {
            return ResponseEntity.internalServerError().build();
        }
        // validate every data in body
        ResponseEntity<SaleResponse> billValidate = startSale(body);
        if (billValidate.getStatusCode()== HttpStatus.OK && !Objects.equals(body.getAddress(), "") && body.getAddress()!=null) {
            List<Sale> results = new ArrayList<>();
            Integer newInvoice = saleService.getLastInvoice()+1;
            for (ItemSold itemSold : body.getItemSoldList()) {
                Catalog catalogProof;
                // validate catalog
                try {
                    catalogProof = catalogService.getById(itemSold.getCatalog()).getBody().get();
                } catch (FeignException e) {
                    return ResponseEntity.internalServerError().build();
                }
                // register sold stock
                try {
                    catalogService.catalogSold(itemSold.getCatalog(), itemSold.getQuantitySold());
                } catch (FeignException e) {
                    return ResponseEntity.unprocessableEntity().build();
                }
                // create bill
                Sale s = new Sale(newInvoice,
                        new User(body.getUser()),
                        catalogProof,
                        itemSold.getQuantitySold(),
                        body.getAddress(),
                        shippingService.getByIdShipping(body.getCity()).get(),
                        LocalDateTime.now(),
                        new Status(1,"In progress"));
                results.add(saleService.create(s));
            }
            return ResponseEntity.ok(results);
        }
        return ResponseEntity.badRequest().build();
    }

    //////////////////////////////////////////////////////////////////

    ///////////////////------- BILLS UPDATE -------///////////////////

    // user
    @PutMapping("/{noInvoice}")
    public ResponseEntity<List<Sale>> canceledByUser (@PathVariable Integer noInvoice) {
        Optional<List<Sale>> invoiceProof = saleService.getByInvoice(noInvoice);
        if (invoiceProof.get().isEmpty()) {
            return ResponseEntity.notFound().build();
        } //
        if (!Objects.equals(invoiceProof.get().get(0).getStatus().getIdStatus(), 1)) {
            return ResponseEntity.unprocessableEntity().build();
        }
        for (Sale sale : invoiceProof.get()) {
            try {
                catalogService.catalogSold(sale.getCatalog().getIdCatalog(),-(sale.getQuantity()));
            } catch (FeignException e) {
                return ResponseEntity.internalServerError().build();
            }
            sale.setStatus(new Status(4, "Cancelled by the customer"));
            saleService.create(sale);
        }
        return ResponseEntity.accepted().body(invoiceProof.get());
    }

    // adm
    @PutMapping("/modify/{noInvoice}")
    public ResponseEntity<List<Sale>> modifyByAdm (@PathVariable Integer noInvoice, @RequestBody ModifyBill body) {
        Optional<List<Sale>> invoiceProof = saleService.getByInvoice(noInvoice);
        if (invoiceProof.get().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (Objects.equals(invoiceProof.get().get(0).getStatus().getIdStatus(), 1) || Objects.equals(invoiceProof.get().get(0).getStatus().getIdStatus(), 2) || Objects.equals(invoiceProof.get().get(0).getStatus().getIdStatus(), 3)) {

            if (body.getNewStatus() == 5 || body.getNewStatus() == 6) {
                for (Sale sale : invoiceProof.get()) {
                    try {
                        catalogService.catalogSold(sale.getCatalog().getIdCatalog(), -(sale.getQuantity()));
                    } catch (FeignException e) {
                        return ResponseEntity.internalServerError().build();
                    }
                    String statusString;
                    if (body.getNewStatus() == 5) {
                        statusString = "Cancelled - Wrong addess";
                    } else {
                        statusString = "Cancelled by admin";
                    }
                    sale.setStatus(new Status(body.getNewStatus(), statusString));
                    saleService.create(sale);
                }
            } else if (body.getNewStatus() == 1 || body.getNewStatus() == 2 || body.getNewStatus() == 3) {
                if (body.getShippingDate() == null) {
                    for (Sale sale : invoiceProof.get()) {
                        String statusString;
                        if (body.getNewStatus() == 1) {
                            statusString = "In progress";
                        } else if (body.getNewStatus() == 2) {
                            statusString = "On the way";
                        } else {
                            return ResponseEntity.badRequest().build();
                        }
                        sale.setStatus(new Status(body.getNewStatus(), statusString));
                        saleService.create(sale);
                    }
                } else {
                    for (Sale sale : invoiceProof.get()) {
                        String statusString;
                        if (body.getNewStatus() == 1) {
                            statusString = "In progress";
                        } else if (body.getNewStatus() == 2) {
                            statusString = "On the way";
                        } else {
                            statusString = "Delivered";
                        }
                        sale.setStatus(new Status(body.getNewStatus(), statusString));
                        sale.setShippingDate(body.getShippingDate());
                        saleService.create(sale);
                    }
                }
            } else {
                return ResponseEntity.badRequest().build();
            }
        }

        if (Objects.equals(invoiceProof.get().get(0).getStatus().getIdStatus(), 4) || Objects.equals(invoiceProof.get().get(0).getStatus().getIdStatus(), 5) || Objects.equals(invoiceProof.get().get(0).getStatus().getIdStatus(), 6)) {

            if (body.getNewStatus() == 5 || body.getNewStatus() == 6) {
                for (Sale sale : invoiceProof.get()) {
                    String statusString;
                    if (body.getNewStatus() == 5) {
                        statusString = "Cancelled - Wrong addess";
                    } else {
                        statusString = "Cancelled by admin";
                    }
                    sale.setStatus(new Status(body.getNewStatus(), statusString));
                    saleService.create(sale);
                }
            } else if (body.getNewStatus() == 1 || body.getNewStatus() == 2 || body.getNewStatus() == 3) {

                String statusString;
                if (body.getNewStatus() == 1) {
                    statusString = "In progress";
                } else if (body.getNewStatus() == 2) {
                    statusString = "On the way";
                } else {
                    statusString = "Delivered";
                }

                if (body.getShippingDate() == null) {
                    for (Sale sale : invoiceProof.get()) {
                        try {
                            catalogService.catalogSold(sale.getCatalog().getIdCatalog(), sale.getQuantity());
                        } catch (FeignException e) {
                            return ResponseEntity.internalServerError().build();
                        }
                        sale.setStatus(new Status(body.getNewStatus(), statusString));
                        saleService.create(sale);
                    }
                } else {
                    for (Sale sale : invoiceProof.get()) {
                        try {
                            catalogService.catalogSold(sale.getCatalog().getIdCatalog(), sale.getQuantity());
                        } catch (FeignException e) {
                            return ResponseEntity.internalServerError().build();
                        }
                        sale.setStatus(new Status(body.getNewStatus(), statusString));
                        sale.setShippingDate(body.getShippingDate());
                        saleService.create(sale);
                    }
                }
            } else {
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.accepted().body(invoiceProof.get());
    }

    // class (JSON) to modify bills
    private static class ModifyBill {
        private Integer newStatus;
        private LocalDateTime shippingDate;

        public Integer getNewStatus() {
            return newStatus;
        }
        public LocalDateTime getShippingDate() {
            return shippingDate;
        }
    }

    //////////////////////////////////////////////////////////////////////

}