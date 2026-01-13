package com.rentauto.paymentprovider.plugins.card;

import com.rentauto.paymentprovider.api.dto.PaymentInitRequest;
import com.rentauto.paymentprovider.api.dto.PaymentInitResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payments/card")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:9090")
public class CardPaymentController {

    private final CardPaymentPlugin cardPlugin;

    @PostMapping("/process")
    public ResponseEntity<PaymentInitResponse> processCardPayment(@RequestBody PaymentInitRequest request) {
        PaymentInitResponse tx = cardPlugin.processPayment(request);
        return ResponseEntity.ok(tx);
    }

    @PostMapping("/bank-card")
    public ResponseEntity<String> bankCardCallback(@RequestBody Map<String, String> payload) {
        String finalRedirect = cardPlugin.handleCallback(payload);
        return ResponseEntity.ok(finalRedirect);
    }
}
