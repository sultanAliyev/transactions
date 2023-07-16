package com.earl.bank.integration;

import com.earl.bank.dto.CreateAccountDTO;
import com.earl.bank.dto.CreateTransactionDTO;
import com.earl.bank.entity.Currency;
import com.earl.bank.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransactionTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    AccountService accountService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void loadContext() {
        assertNotNull(mvc);
        assertNotNull(objectMapper);
    }

    @Test
    void createTransaction() throws Exception {
        var accountDTO = new CreateAccountDTO();
        accountDTO.setCountry("Iran");
        accountDTO.setCurrency(Set.of(Currency.EUR));
        accountDTO.setCustomerId("1234");
        accountDTO.setBaseBalance(BigDecimal.valueOf(2500));
        var account = accountService.createAccount(accountDTO);

        var differentAccountDTO = new CreateAccountDTO();
        differentAccountDTO.setCountry("Kazakhstan");
        differentAccountDTO.setCurrency(Set.of(Currency.EUR));
        differentAccountDTO.setCustomerId("4321");
        differentAccountDTO.setBaseBalance(BigDecimal.valueOf(500));
        var accountReceiver = accountService.createAccount(differentAccountDTO);

        var transactionDTO = new CreateTransactionDTO();
        transactionDTO.setSenderId(account.getAccountId());
        transactionDTO.setReceiverId(accountReceiver.getAccountId());
        transactionDTO.setAmount(new BigDecimal("1000.00"));
        transactionDTO.setCurrency(Currency.EUR);
        transactionDTO.setDescription("test");

        mvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO))
                )
                .andExpect(status().isCreated());
    }
}
