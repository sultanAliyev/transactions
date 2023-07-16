package com.earl.bank.controller;

import com.earl.bank.dto.CreateTransactionDTO;
import com.earl.bank.entity.Transaction;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.service.AccountService;
import com.earl.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Transaction createTransaction(@RequestBody CreateTransactionDTO transaction) throws AccountNotFoundException {
        return transactionService.createTransaction(transaction);
    }
}
