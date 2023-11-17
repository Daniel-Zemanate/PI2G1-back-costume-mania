package com.costumemania.msbills.controller;

import com.costumemania.msbills.model.Status;
import com.costumemania.msbills.service.StatusService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/status")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }
    @GetMapping
    public ResponseEntity<List<Status>>getAllStatus(){
        return ResponseEntity.ok(statusService.getAllStatus());
    }

    @GetMapping("/allSale")
    public ResponseEntity<Optional<List<Status>>> getAllBySale(){
        Optional<List<Status>> status = statusService.getAllStatusBySale();
            if(status.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().body(status);
    }


    @GetMapping("/idSale/{idSale}")
    public ResponseEntity<Optional<Status>> getByIdSale(@PathVariable Integer idSale){
        Optional<Status> status = statusService.getByIdSale(idSale);
        if(status.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(status);
    }

    @GetMapping("/allSale/page/{page}")
    public ResponseEntity<Page<Status>> getAllBySale(@PathVariable Integer page) {
        Optional<List<Status>> result = statusService.getAllStatusBySale();
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        int pageSize = 12;
        int totalItems = result.get().size();
        Pageable pageable = PageRequest.of(page, pageSize);

        if (pageable.getOffset() > totalItems) {
            return ResponseEntity.notFound().build();
        } else {
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageSize), totalItems);
            List<Status> pageResult = result.get().subList(start, end);

            Page<Status> statusPage = new PageImpl<>(pageResult, pageable, totalItems);
            return ResponseEntity.ok().body(statusPage);
        }
    }

}
