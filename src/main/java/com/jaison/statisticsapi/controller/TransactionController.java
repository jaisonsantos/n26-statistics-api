package com.jaison.statisticsapi.controller;

import com.jaison.statisticsapi.dto.ResponseMessageDto;
import com.jaison.statisticsapi.model.Transaction;
import com.jaison.statisticsapi.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<String> addTransaction(@RequestBody Transaction transaction) {
        LOGGER.debug("post request for transaction {} received", transaction);
        ResponseMessageDto response = transactionService.addTransaction(transaction);
        return ResponseEntity.status(response.getCode()).body(response.getMessage());
    }

    @DeleteMapping("/transactions")
    public ResponseEntity deleteTransactions() {
        LOGGER.debug("request for delete transaction received");
        transactionService.deleteTransactions();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
