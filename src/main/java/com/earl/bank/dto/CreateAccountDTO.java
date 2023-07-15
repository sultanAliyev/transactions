package com.earl.bank.dto;

import com.earl.bank.entity.Currency;

import java.math.BigDecimal;
import java.util.Set;

public class CreateAccountDTO {
    String customerId;
    String Country;
    Set<Currency> currency;
    BigDecimal baseBalance;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public Set<Currency> getCurrency() {
        return currency;
    }

    public void setCurrency(Set<Currency> currency) {
        this.currency = currency;
    }

    public BigDecimal getBaseBalance() {
        return baseBalance;
    }

    public void setBaseBalance(BigDecimal baseBalance) {
        this.baseBalance = baseBalance;
    }
}
