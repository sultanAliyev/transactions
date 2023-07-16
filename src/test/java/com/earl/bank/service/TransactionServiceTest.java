package com.earl.bank.service;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.dto.CreateTransactionDTO;
import com.earl.bank.entity.Currency;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.exception.InsufficientFundException;
import com.earl.bank.exception.InvalidAmountException;
import com.earl.bank.mapper.BalanceMapper;
import com.earl.bank.mapper.TransactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TransactionServiceTest {

    @MockBean
    TransactionService transactionService;

    @Autowired
    TransactionMapper transactionMapper;

    @Autowired
    BalanceMapper balanceMapper;
    @Autowired
    AccountService accountService;

    @BeforeEach
    void setUp() {
        transactionService =
                new TransactionServiceImpl(transactionMapper, balanceMapper, accountService);
    }

    @Test
    @DisplayName("Transaction deposits money into user account")
    void createTransactionDeposit() {
        var accountDTO = new CreateAccountDTO();
        accountDTO.setCountry("Iran");
        accountDTO.setCurrency(Set.of(Currency.EUR));
        accountDTO.setCustomerId("1234");
        accountDTO.setBaseBalance(BigDecimal.valueOf(2000));
        var accountSender = accountService.createAccount(accountDTO);

        var differentAccountDTO = new CreateAccountDTO();
        differentAccountDTO.setCountry("Kazakhstan");
        differentAccountDTO.setCurrency(Set.of(Currency.EUR));
        differentAccountDTO.setCustomerId("4321");
        differentAccountDTO.setBaseBalance(BigDecimal.valueOf(2000));
        var accountReceiver = accountService.createAccount(differentAccountDTO);

        assertNotNull(accountSender);
        assertNotNull(accountReceiver);

        var transactionDTO = new CreateTransactionDTO();
        transactionDTO.setSenderId(accountSender.getAccountId());
        transactionDTO.setReceiverId(accountReceiver.getAccountId());
        transactionDTO.setAmount(new BigDecimal("1000.00"));
        transactionDTO.setCurrency(Currency.EUR);
        transactionDTO.setDescription("test");
        var transaction = transactionService.createTransaction(transactionDTO);

        assertNotNull(transaction);
        assertNotNull(transaction.getBalance());
        assertEquals(transaction.getBalance().getAmount().compareTo(new BigDecimal("1000.00")), 0);
        assertEquals(transaction.getSenderId(), accountSender.getAccountId());
        assertEquals(transaction.getReceiverId(), accountReceiver.getAccountId());
        assertEquals(transaction.getBalance().getCurrency(), Currency.EUR);
    }

    @Test
    @DisplayName("creating a transaction for non existence bank account throws AccountNotFoundException")
    void createTransactionThrowsAccountNotFoundException() {
        var transactionDTO = new CreateTransactionDTO();
        transactionDTO.setAmount(new BigDecimal("1000.00"));
        transactionDTO.setCurrency(Currency.EUR);
        transactionDTO.setDescription("test");
        transactionDTO.setSenderId(1000000L);
        transactionDTO.setReceiverId(1000000L);
        assertNotNull(transactionDTO);
        assertThrows(
                AccountNotFoundException.class,
                () -> transactionService.createTransaction(transactionDTO)
        );

    }

    @Test
    @DisplayName("creating a withdraw transaction more than currency balance throws InsufficientFundException")
    void createTransactionThrowsInsufficientFundException() {
        var accountDTO = new CreateAccountDTO();
        accountDTO.setCountry("Iran");
        accountDTO.setCurrency(Set.of(Currency.EUR));
        accountDTO.setCustomerId("1234");
        accountDTO.setBaseBalance(BigDecimal.valueOf(2000));
        var accountSender = accountService.createAccount(accountDTO);

        var differentAccountDTO = new CreateAccountDTO();
        differentAccountDTO.setCountry("Kazakhstan");
        differentAccountDTO.setCurrency(Set.of(Currency.EUR));
        differentAccountDTO.setCustomerId("4321");
        differentAccountDTO.setBaseBalance(BigDecimal.valueOf(2000));
        var accountReceiver = accountService.createAccount(differentAccountDTO);

        assertNotNull(accountSender);
        assertNotNull(accountReceiver);

        var transactionDTO = new CreateTransactionDTO();
        transactionDTO.setCurrency(Currency.EUR);
        transactionDTO.setDescription("test");
        transactionDTO.setSenderId(accountSender.getAccountId());
        transactionDTO.setReceiverId(accountReceiver.getAccountId());
        transactionDTO.setAmount(new BigDecimal("10000.00"));

        assertNotNull(transactionDTO);
        assertThrows(
                InsufficientFundException.class,
                () -> transactionService.createTransaction(transactionDTO)
        );

    }

    @Test
    @DisplayName("creating a transaction with negative amount throws InvalidAmountException")
    void createTransactionThrowsInvalidAmountException() {
        var accountDTO = new CreateAccountDTO();
        accountDTO.setCountry("Iran");
        accountDTO.setCurrency(Set.of(Currency.EUR));
        accountDTO.setCustomerId("1234");
        accountDTO.setBaseBalance(BigDecimal.valueOf(2000));
        var accountSender = accountService.createAccount(accountDTO);

        var differentAccountDTO = new CreateAccountDTO();
        differentAccountDTO.setCountry("Kazakhstan");
        differentAccountDTO.setCurrency(Set.of(Currency.EUR));
        differentAccountDTO.setCustomerId("4321");
        differentAccountDTO.setBaseBalance(BigDecimal.valueOf(2000));
        var accountReceiver = accountService.createAccount(differentAccountDTO);

        assertNotNull(accountSender);
        assertNotNull(accountReceiver);

        var transactionDTO = new CreateTransactionDTO();
        transactionDTO.setCurrency(Currency.EUR);
        transactionDTO.setDescription("test");
        transactionDTO.setSenderId(accountSender.getAccountId());
        transactionDTO.setReceiverId(accountReceiver.getAccountId());
        transactionDTO.setAmount(new BigDecimal("10000.00").negate());

        assertNotNull(transactionDTO);
        assertThrows(
                InvalidAmountException.class,
                () -> transactionService.createTransaction(transactionDTO)
        );
    }


}