package com.rentauto.paymentprovider.plugins.qr;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentauto.paymentprovider.api.dto.PaymentInitRequest;
import com.rentauto.paymentprovider.api.dto.PaymentInitResponse;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/payments/qr")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:9090")
public class QrCodePaymentController {

    private final QrCodePaymentPlugin qrPlugin;

    @PostMapping("/process")
    public ResponseEntity<PaymentInitResponse> processQrPayment(@RequestBody PaymentInitRequest request) {
        PaymentInitResponse tx = qrPlugin.processPayment(request);
        return ResponseEntity.ok(tx);
    }

    @GetMapping("/{stan}")
    public ResponseEntity<Map<String, String>> getQr(@PathVariable String stan) {

        String qr = qrPlugin.generateQr(stan);

        return ResponseEntity.ok(Map.of(
                "method", "QR_CODE",
                "qrCode", qr
        ));
    }

    @PostMapping("/callback")
    public ResponseEntity<String> qrCallback(@RequestBody Map<String, String> payload) {
        String redirect = qrPlugin.handleCallback(payload);
        return ResponseEntity.ok(redirect);
    }
}
