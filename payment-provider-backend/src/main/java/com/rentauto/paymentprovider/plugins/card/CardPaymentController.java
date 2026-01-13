package com.rentauto.paymentprovider.plugins.card;

import com.rentauto.paymentprovider.api.dto.PaymentInitRequest;
import com.rentauto.paymentprovider.api.dto.PaymentInitResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments/card")
@AllArgsConstructor
public class CardPaymentController {

    private final CardPaymentPlugin cardPlugin;

    @PostMapping("/process")
    public ResponseEntity<PaymentInitResponse> processCardPayment(@RequestBody PaymentInitRequest request) {
        PaymentInitResponse tx = cardPlugin.processPayment(request);
        return ResponseEntity.ok(tx);
    }
}
