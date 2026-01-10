package com.rentauto.paymentprovider.api.controller;

import com.rentauto.paymentprovider.domain.Merchant;
import com.rentauto.paymentprovider.service.MerchantService;
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
    public List<Merchant> getAll() {
        return merchantService.getAll();
    }

    @GetMapping("/{id}")
    public Merchant get(@PathVariable UUID id) {
        return merchantService.getMerchant(id);
    }

    @GetMapping("/email/{email}")
    public Merchant getByEmail(@PathVariable String email) {
        return merchantService.getByEmail(email);
    }

    @PostMapping()
    public Merchant updateMerchant(@RequestBody Merchant updatedMerchant) {
        return merchantService.update(updatedMerchant);
    }
}
