package com.rentauto.paymentprovider.plugins.qr;

import java.math.RoundingMode;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rentauto.paymentprovider.api.dto.PaymentInitRequest;
import com.rentauto.paymentprovider.api.dto.PaymentInitResponse;
import com.rentauto.paymentprovider.domain.PaymentMethod;
import com.rentauto.paymentprovider.domain.Transaction;
import com.rentauto.paymentprovider.plugins.PaymentPlugin;
import com.rentauto.paymentprovider.service.TransactionService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class QrCodePaymentPlugin implements PaymentPlugin {

    private final TransactionService transactionService;
    private final QrGenerator qrGenerator;

    @Override
    public PaymentInitResponse processPayment(PaymentInitRequest request) {

        Transaction tx =
            transactionService.initPaymentProcess(
                request,
                PaymentMethod.QR_CODE
            );

        return new PaymentInitResponse(
            tx.getStan(),
            tx.getPaymentUrl(),
            tx.getPaymentStatus().name()
        );
    }
    
    @Override
    public String handleCallback(Map<String, String> params) {
        return "";
    }

    public String generateQr(String stan) {

        Transaction pspTx = transactionService.getTransaction(stan);

        String qrPayload = buildIpsPayload(pspTx);

        return qrGenerator.generate(qrPayload);
    }

    public String buildIpsPayload(Transaction tx) {

        String amount = tx.getAmount()
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString()
                .replace(".", ",");

        return "K:PR" +
                "|V:01" +
                "|C:1" +
                "|R:" + tx.getMerchant().getAccountNumber() +
                "|N:" + tx.getMerchant().getName() +
                "|I:RSD" + amount +
                "|SF:221" +
                "|S:Placanje rentiranja vozila" +
                "|RO:00" + tx.getStan().replace("STAN-", "");
    }
}
