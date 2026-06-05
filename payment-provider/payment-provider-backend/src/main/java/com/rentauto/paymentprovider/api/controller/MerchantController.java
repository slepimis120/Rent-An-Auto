package com.rentauto.paymentprovider.api.controller;

import com.rentauto.paymentprovider.domain.Merchant;
import com.rentauto.paymentprovider.service.MerchantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/merchants")
public class MerchantController {
    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping
    public ResponseEntity<List<Merchant>> getAll() {
        return ResponseEntity.ok(merchantService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Merchant> get(@PathVariable UUID id) {
        return ResponseEntity.ok(merchantService.getMerchant(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Merchant> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(merchantService.getByEmail(email));
    }

    @PostMapping()
    public ResponseEntity<Merchant> updateMerchant(@RequestBody Merchant updatedMerchant) {
        return ResponseEntity.ok(merchantService.update(updatedMerchant));
    }
}
