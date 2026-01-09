package com.rentauto.paymentprovider.plugins.qr;

import com.rentauto.paymentprovider.api.dto.PaymentInitResponse;
import com.rentauto.paymentprovider.domain.PaymentMethod;
import com.rentauto.paymentprovider.domain.PaymentStatus;
import com.rentauto.paymentprovider.domain.Transaction;
import com.rentauto.paymentprovider.plugins.PaymentPlugin;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class QrCodePaymentPlugin implements PaymentPlugin {

    private final QrGenerator qrGenerator;

    @Override
    public PaymentMethod supports() {
        return PaymentMethod.QR_CODE;
    }

    @Override
    public PaymentInitResponse init(Transaction tx) {
        String qrContent = qrGenerator.generate(tx);

        tx.setPaymentStatus(PaymentStatus.INITIATED);

        return new PaymentInitResponse(
                tx.getStan(),
                qrContent,
                tx.getPaymentStatus().name()
        );
    }

    @Override
    public void handleCallback(Map<String, String> payload) {

    }

    @Override
    public PaymentStatus checkStatus(Transaction tx) {
        return tx.getPaymentStatus();
    }
}
