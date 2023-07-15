package com.earl.bank.service;

import com.earl.bank.dto.CreateTransactionDTO;
import com.earl.bank.entity.Transaction;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.exception.InsufficientFundException;
import com.earl.bank.exception.InvalidAmountException;
import com.earl.bank.mapper.BalanceMapper;
import com.earl.bank.mapper.TransactionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionMapper transactionMapper;
    private final BalanceMapper balanceMapper;
    private final AccountService accountService;

    @Autowired
    public TransactionServiceImpl(TransactionMapper transactionMapper, BalanceMapper balanceMapper, AccountService accountService) {
        this.transactionMapper = transactionMapper;
        this.balanceMapper = balanceMapper;
        this.accountService = accountService;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Transaction createTransaction(CreateTransactionDTO transaction)
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundException {
        var accountSender = accountService.getAccount(transaction.getSenderId());
        var accountReceiver = accountService.getAccount(transaction.getReceiverId());
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.error("Base amount can not be negative!");
            throw new InvalidAmountException();
        }
        var balanceComparison = balanceMapper.getBalance(
                transaction.getSenderId(),
                transaction.getCurrency()
        ).getAmount().subtract(transaction.getAmount()).compareTo(BigDecimal.ZERO);
            if (balanceComparison < 0) {
                log.error("Insufficient funds on the account!");
                throw new InsufficientFundException();
            }
            else {
                balanceMapper.decreaseBalance(
                        accountSender.getAccountId(),
                        transaction.getCurrency(),
                        transaction.getAmount()
                );
                balanceMapper.increaseBalance(
                        accountReceiver.getAccountId(),
                        transaction.getCurrency(),
                        transaction.getAmount()
                );
            }
        var balance = balanceMapper.getBalance(accountSender.getAccountId(), transaction.getCurrency());
        var newTransaction = new Transaction(
                transaction.getSenderId(),
                transaction.getReceiverId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getDescription()
        );
        transactionMapper.createTransaction(newTransaction);
        newTransaction.setBalance(balance);
        log.info("Transaction has been created SUCCESSFULLY!");
        return newTransaction;
    }
}
