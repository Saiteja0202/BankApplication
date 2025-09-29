package com.bank.controller;

import com.bank.model.Transactions;
import com.bank.service.TransactionsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    private final TransactionsService transactionsService;

    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @GetMapping("/account/{accountId}")
    public List<Transactions> getTransactionsByAccount(@PathVariable Long accountId) {
        return transactionsService.getTransactionsByAccount(accountId);
    }


    @GetMapping("/{transactionId}")
    public Transactions getTransactionById(@PathVariable Long transactionId) {
        return transactionsService.getTransactionById(transactionId);
    }
    
    @GetMapping("/all")
    public List<Transactions> getAllTransactions() {
        return transactionsService.getAllTransactions();
    }

}
