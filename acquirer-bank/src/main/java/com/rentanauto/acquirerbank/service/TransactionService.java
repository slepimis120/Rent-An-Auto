package com.rentanauto.acquirerbank.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.rentanauto.acquirerbank.api.dto.CardPaymentRequest;
import com.rentanauto.acquirerbank.api.dto.CardPaymentResponse;
import com.rentanauto.acquirerbank.api.dto.TransactionCreateRequest;
import com.rentanauto.acquirerbank.api.dto.TransactionCreateResponse;
import com.rentanauto.acquirerbank.domain.CardHolder;
import com.rentanauto.acquirerbank.domain.PaymentStatus;
import com.rentanauto.acquirerbank.domain.Transaction;
import com.rentanauto.acquirerbank.repository.CardHolderRepository;
import com.rentanauto.acquirerbank.repository.TransactionRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CardHolderRepository cardHolderRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String CVK_SECRET = "secret-bank-key";
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    public TransactionCreateResponse createTransaction(TransactionCreateRequest request) {
        log.info(
            "Transaction created. MerchantId={}, Amount={}",
            request.merchantId(),
            request.amount()
        );
        
        Transaction existing = transactionRepository.findByStan(request.stan());
        if (existing != null) {
            return new TransactionCreateResponse(existing.getPaymentUrl(), existing.getId().toString());
        }

        Transaction transaction = new Transaction();
        transaction.setMerchantId(request.merchantId());
        transaction.setAmount(new BigDecimal(request.amount()));
        transaction.setCurrency(request.currency());
        transaction.setStan(request.stan());
        transaction.setPspTimestamp(request.psp_timestamp());
        transaction.setAcquirerTimestamp(Instant.now().toString());
        transaction.setPaymentStatus(PaymentStatus.CREATED);

        transaction = transactionRepository.save(transaction);

        String paymentUrl = "http://localhost:4300/pay/" + transaction.getId();
        
        transaction.setPaymentUrl(paymentUrl);

        transactionRepository.save(transaction);

        return new TransactionCreateResponse(paymentUrl, transaction.getId().toString());
    }

    public Transaction getTransactionById(String id) {
        return transactionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
    }

    public CardPaymentResponse processPayment(CardPaymentRequest request) {

        log.info("Payment attempt started. TransactionId={}",
                request.transactionId());

        Transaction transaction = transactionRepository.findById(
                UUID.fromString(request.transactionId()))
                .orElseThrow(() ->
                        new IllegalArgumentException("Transaction not found"));

        CardHolder cardHolder = cardHolderRepository
                .findByPanEncrypted(hashPan(request.pan()))
                .orElseThrow(() -> {

                    log.warn("Card lookup failed");

                    return new IllegalArgumentException("Card not found");
                });
        
        log.info(
            "Card lookup successful. CardHolderId={}",
            cardHolder.getId()
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");

        YearMonth cardExpiry = YearMonth.parse(request.expiryDate(), formatter);
        YearMonth now = YearMonth.now();

        boolean notExpired = !cardExpiry.isBefore(now);

        String expectedCvv = generateExpectedCvv(
                request.pan(),
                request.expiryDate()
        );

        boolean validCvv = expectedCvv.equals(request.securityCode());

        boolean validCard =
                cardHolder.isActive()
                && validCvv
                && cardHolder.getExpiryDate().equals(request.expiryDate())
                && cardHolder.getFullName().equalsIgnoreCase(request.cardHolderName())
                && notExpired;

        boolean hasFunds =
                cardHolder.getBalance()
                        .compareTo(transaction.getAmount()) >= 0;

        String status;

        if (validCard && hasFunds) {
            cardHolder.setBalance(
                    cardHolder.getBalance()
                            .subtract(transaction.getAmount()));

            cardHolderRepository.save(cardHolder);

            transaction.setPaymentStatus(PaymentStatus.SUCCESS);

            status = "SUCCESS";

            log.info(
                "Payment SUCCESS. TransactionId={}, Amount={}",
                transaction.getId(),
                transaction.getAmount()
            );

        } else {
            transaction.setPaymentStatus(PaymentStatus.FAILED);

            status = "FAILED";

            log.warn(
                "Payment FAILED. TransactionId={}, ValidCard={}, HasFunds={}",
                transaction.getId(),
                validCard,
                hasFunds
            );
        }

        transactionRepository.save(transaction);

        Map<String, String> pspPayload = new HashMap<>();
        pspPayload.put("stan", transaction.getStan());
        pspPayload.put("status", status);
        pspPayload.put("globalTransactionId", transaction.getId().toString());
        pspPayload.put("acquirerTimestamp", transaction.getAcquirerTimestamp());

        String finalRedirectUrlFromPsp = sendNotification(pspPayload);

        return new CardPaymentResponse(
                status,
                transaction.getId().toString(),
                transaction.getAcquirerTimestamp(),
                transaction.getStan(),
                finalRedirectUrlFromPsp
        );
    }

    private String hashPan(String pan) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pan.replaceAll("\\s+", "").getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    public String sendNotification(Map<String, String> payload) {
        String pspUrl = "http://payment-provider-backend:8080/payments/card/bank-card";
        return restTemplate.postForObject(pspUrl, payload, String.class);
    }

    private String generateExpectedCvv(String pan, String expiry) {
        try {
            String data = pan.replaceAll("\\s+", "") + "|" + expiry;

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(
                    CVK_SECRET.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );

            mac.init(keySpec);

            byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            int value = ((rawHmac[0] & 0xFF) << 24)
                    | ((rawHmac[1] & 0xFF) << 16)
                    | ((rawHmac[2] & 0xFF) << 8)
                    | (rawHmac[3] & 0xFF);

            value = Math.abs(value);

            return String.format("%03d", value % 1000);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String generateQrCode(Transaction tx) {
        try{
            String qrText = "PAYMENT:" + tx.getId() + ":" + tx.getStan() + ":" + tx.getAmount();

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.MARGIN, 1);

            BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 250, 250, hints);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "PNG", baos);

            return "data:image/png;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());

        } catch (WriterException | IOException e){
            throw new RuntimeException("Failed to generate QR Code", e);
        }
    }
}
