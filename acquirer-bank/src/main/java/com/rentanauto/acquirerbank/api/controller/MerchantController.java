package com.rentanauto.acquirerbank.api.controller;

import com.rentanauto.acquirerbank.api.dto.MerchantCreateRequest;
import com.rentanauto.acquirerbank.api.dto.MerchantCreateResponse;
import com.rentanauto.acquirerbank.domain.Merchant;
import com.rentanauto.acquirerbank.service.MerchantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/merchants")
public class MerchantController {
    private final MerchantService service;

    @PostMapping
    public MerchantCreateResponse create(@RequestBody MerchantCreateRequest request) {
        return service.create(request);
    }

    @GetMapping()
    public Iterable<Merchant> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public MerchantCreateResponse getById(@PathVariable String id) {
        return service.getById(id);
    }
}
