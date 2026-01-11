package com.rentanauto.acquirerbank.api.controller;

import com.rentanauto.acquirerbank.api.dto.TransactionCreateRequest;
import com.rentanauto.acquirerbank.api.dto.TransactionCreateResponse;
import com.rentanauto.acquirerbank.service.PspService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/psp")
public class PspController {
    private final PspService service;

    @PostMapping("/createTransaction")
    public ResponseEntity<TransactionCreateResponse> createTransaction(@RequestBody TransactionCreateRequest request) {
        TransactionCreateResponse transaction = service.createTransaction(request);
        return ResponseEntity.ok(transaction);
    }

}
