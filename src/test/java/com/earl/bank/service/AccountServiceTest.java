package com.earl.bank.service;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.entity.Account;
import com.earl.bank.entity.Currency;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.exception.InvalidCurrencyException;
import com.earl.bank.mapper.AccountMapper;
import com.earl.bank.mapper.BalanceMapper;
import com.earl.bank.mapper.TransactionMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {
    @MockBean
    AccountService accountService;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    BalanceMapper balanceMapper;

    @Autowired
    TransactionMapper transactionMapper;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl(accountMapper, balanceMapper);
    }

    @Test
    void createAccount() throws InvalidCurrencyException {
        var accountDTO = new CreateAccountDTO();
        accountDTO.setCountry("Iran");
        accountDTO.setCurrency(Set.of(Currency.EUR));
        accountDTO.setCustomerId("1234");
        accountDTO.setBaseBalance(BigDecimal.valueOf(0));
        var account = accountService.createAccount(accountDTO);
        assertNotNull(account);
        Assertions.assertEquals(account.getCountry(), "Iran");
        Assertions.assertEquals(account.getCustomerId(), "1234");
        Assertions.assertEquals(account.getBalances().size(), 2);
        Assertions.assertEquals(account.getBalances().stream().mapToDouble(i -> i.getAmount().doubleValue()).sum(), 0d);
    }

    @Test
    void getAccount() throws AccountNotFoundException {
        var account = new Account("1234", "Iran");
        accountMapper.createAccount(account);
        account = accountService.getAccount(account.getAccountId());
        assertNotNull(account);
        assertEquals(account.getCustomerId(), "1234");
        assertEquals(account.getCountry(), "Iran");
        assertNotEquals(account.getAccountId(), 0L);
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(10000L));
    }
}