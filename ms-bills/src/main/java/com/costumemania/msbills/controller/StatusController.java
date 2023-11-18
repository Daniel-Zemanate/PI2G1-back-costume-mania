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

    // user + adm
    @GetMapping
    public ResponseEntity<List<Status>>getAllStatus(){
        return ResponseEntity.ok(statusService.getAllStatus());
    }

    // adm
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Status>> getByIdSale(@PathVariable Integer id){
        Optional<Status> status = statusService.getById(id);
        if(status.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(status);
    }
}