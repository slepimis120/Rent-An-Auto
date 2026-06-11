package com.rentauto.paymentprovider.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentauto.paymentprovider.domain.Merchant;
import com.rentauto.paymentprovider.domain.PaymentMethod;
import com.rentauto.paymentprovider.service.MerchantService;

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

    @GetMapping("paymentMethods/{id}")
    public ResponseEntity<List<PaymentMethod>> getMerchantPaymentMethods(@PathVariable UUID id) {
        return ResponseEntity.ok(merchantService.getMerchantPaymentMethods(id));
    }

    @PostMapping()
    public ResponseEntity<Merchant> updateMerchant(@RequestBody Merchant updatedMerchant) {
        return ResponseEntity.ok(merchantService.update(updatedMerchant));
    }
}
