package com.rentanauto.acquirerbank.api.controller;

import com.rentanauto.acquirerbank.api.dto.MerchantCreateRequest;
import com.rentanauto.acquirerbank.api.dto.MerchantCreateResponse;
import com.rentanauto.acquirerbank.domain.Merchant;
import com.rentanauto.acquirerbank.service.MerchantService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/merchants")
public class MerchantController {
    private final MerchantService service;

    @PostMapping
    public ResponseEntity<MerchantCreateResponse> create(@RequestBody MerchantCreateRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping()
    public ResponseEntity<List<Merchant>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantCreateResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
