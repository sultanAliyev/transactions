package com.earl.bank.service;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.entity.Account;
import com.earl.bank.exception.AccountNotFoundException;
import com.earl.bank.exception.InvalidCurrencyException;

public interface AccountService {
    Account createAccount(CreateAccountDTO account) throws InvalidCurrencyException;

    Account getAccount(Long accountId) throws AccountNotFoundException;
}
