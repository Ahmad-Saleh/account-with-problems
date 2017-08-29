package com.progressoft.induction;

import java.math.BigDecimal;
import java.util.Currency;

public class BankAccount {

    private BigDecimal balance = BigDecimal.ZERO;
    private Currency accountCurrency;

    public BankAccount(Currency accountCurrency) {
        this.accountCurrency = accountCurrency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void deposit(BigDecimal amount) {
        if (amount == null || BigDecimal.ZERO.compareTo(amount) != -1) {
            throw new InvalidAmountException("invalid amount: " + amount);
        }
        this.balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount == null || BigDecimal.ZERO.compareTo(amount) != -1) {
            throw new InvalidAmountException("invalid amount: " + amount);
        }
        this.balance = balance.add(amount.negate());
    }

    public void deposit(BigDecimal amount, Currency currency) {
        deposit(exchange(amount, currency));
    }

    private BigDecimal exchange(BigDecimal amount, Currency toCurrency) {
        return amount.multiply(getRate(toCurrency));
    }

    private BigDecimal getRate(Currency fromCurrency) {
        return new ExchangeRate(fromCurrency, accountCurrency).getRate();
    }


    static class InvalidAmountException extends RuntimeException {

        public InvalidAmountException(String message) {
            super(message);
        }
    }
}
