package com.earl.bank.service;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.entity.Account;
import com.earl.bank.entity.Balance;
import com.earl.bank.entity.Currency;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.exception.InvalidAmountException;
import com.earl.bank.exception.InvalidCurrencyException;
import com.earl.bank.mapper.AccountMapper;
import com.earl.bank.mapper.BalanceMapper;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;
    private final BalanceMapper balanceMapper;
    Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    public AccountServiceImpl(AccountMapper accountMapper, BalanceMapper balanceMapper) {
        this.accountMapper = accountMapper;
        this.balanceMapper = balanceMapper;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Account createAccount(CreateAccountDTO account) throws InvalidCurrencyException {
        var tempCurrency = new HashSet<>(Set.copyOf(account.getCurrency()));
        tempCurrency.removeAll(Arrays.stream(Currency.values()).collect(Collectors.toSet()));
        if (account.getCurrency().size() == 0 || tempCurrency.size() != 0) {
            logger.error("No currencies has been specified!");
            throw new InvalidCurrencyException();
        }
        if (account.getBaseBalance().compareTo(BigDecimal.ZERO) < 0){
            logger.error("Base amount can not be negative!");
            throw new InvalidAmountException();
        }
        var newAccount = new Account(account.getCustomerId(), account.getCountry());
        accountMapper.createAccount(newAccount);
        account.getCurrency().forEach(currency -> {
            var balance = new Balance(newAccount.getAccountId(), account.getBaseBalance() != null ? account.getBaseBalance() : BigDecimal.ZERO, currency);
            balanceMapper.createBalance(balance);
        });
        logger.info("New account has been created SUCCESSFULLY!");
        return getAccount(newAccount.getAccountId());
    }

    @Override
    public Account getAccount(Long accountId) throws AccountNotFoundException {
        var account = accountMapper.getAccount(accountId);
        if (account == null) throw new AccountNotFoundException();
        return account;
    }
}
