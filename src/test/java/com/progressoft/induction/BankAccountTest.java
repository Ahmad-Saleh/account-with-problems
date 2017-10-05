package com.progressoft.induction;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class BankAccountTest {

    private BankAccount bankAccount;

    @Before
    public void setUp() {
        this.bankAccount = new BankAccount(Currency.getInstance("EUR"));
    }

    @Test(expected = BankAccount.InvalidAmountException.class)
    public void givenBankAccount_WhenDepositingNull_ExceptionShouldBeThrown() {
        bankAccount.deposit(null);
    }

    @Test(expected = BankAccount.InvalidAmountException.class)
    public void givenBankAccount_WhenDepositingNegativeAmounts_ExceptionShouldBeThrown() {
        bankAccount.deposit(new BigDecimal(-40));
    }

    @Test(expected = BankAccount.InvalidAmountException.class)
    public void givenBankAccount_WhenDepositingZeroAmounts_ExceptionShouldBeThrown() {
        bankAccount.deposit(new BigDecimal(0));
    }

    @Test
    public void givenNewlyCreatedBankAccount_BalanceShouldBeZero() {
        assertTrue(BigDecimal.ZERO.compareTo(bankAccount.getBalance()) == 0);
    }

    @Test
    public void givenNewlyCreatedBankAccount_WhenDepositAnAmount_BalanceShouldEqualThatAmount() {
        bankAccount.deposit(new BigDecimal(10));
        assertTrue(new BigDecimal(10).compareTo(bankAccount.getBalance()) == 0);
    }

    @Test(expected = BankAccount.InvalidAmountException.class)
    public void givenBankAccount_WhenWithdrawingNull_ExceptionShouldBeThrown() {
        bankAccount.withdraw(null);
    }

    @Test(expected = BankAccount.InvalidAmountException.class)
    public void givenBankAccount_WhenWithdrawingNegativeAmounts_ExceptionShouldBeThrown() {
        bankAccount.withdraw(new BigDecimal(-40));
    }

    @Test(expected = BankAccount.InvalidAmountException.class)
    public void givenBankAccount_WhenWithdrawingZeroAmounts_ExceptionShouldBeThrown() {
        bankAccount.withdraw(new BigDecimal(0));
    }

    @Test
    public void givenNewlyCreatedAccount_WhenWithdrawingAnAmount_BalanceShouldEqualNegativeThatAmount() {
        bankAccount.withdraw(new BigDecimal(10));
        assertTrue(new BigDecimal(-10).compareTo(bankAccount.getBalance()) == 0);
    }

    @Test
    public void givenNewlyCreatedAccount_WhenDepositingMultipleTimes_ThenBalanceShouldBeTotalDeposited() {
        bankAccount.deposit(new BigDecimal(10));
        bankAccount.deposit(new BigDecimal(10));
        bankAccount.deposit(new BigDecimal(5));
        bankAccount.deposit(new BigDecimal(12));
        assertTrue(new BigDecimal(37).compareTo(bankAccount.getBalance()) == 0);
    }

    @Test
    public void givenNewlyCreatedAccount_WhenWithdrawingMultipleTimes_ThenBalanceShouldBeNegativeTheTotal() throws Exception {
        bankAccount.withdraw(new BigDecimal(4));
        bankAccount.withdraw(new BigDecimal(45));
        bankAccount.withdraw(new BigDecimal(5));
        bankAccount.withdraw(new BigDecimal(1));
        assertTrue(new BigDecimal(-55).compareTo(bankAccount.getBalance()) == 0);
    }

    @Test
    public void givenNewlyCreatedAccount_WhenDepositingAndWithdrawing_ThenBalanceShouldBeTheTotalDepositedMinusTheTotalWithdrawn() {
        bankAccount.deposit(new BigDecimal(1000));
        bankAccount.withdraw(new BigDecimal(100));
        bankAccount.withdraw(new BigDecimal(350));
        bankAccount.withdraw(new BigDecimal(400));
        bankAccount.withdraw(new BigDecimal(15));
        bankAccount.deposit(new BigDecimal(152));
        bankAccount.deposit(new BigDecimal(50));
        bankAccount.withdraw(new BigDecimal(140));

        assertTrue(new BigDecimal(197).compareTo(bankAccount.getBalance()) == 0);
    }

    @Test
    public void givenNewlyCreatedAccount_WhenDepositingInDifferentCurrency_ThenAmountShouldBeConvertedAndDeposited() {
        bankAccount.deposit(new BigDecimal(10), Currency.getInstance("USD"));
        BigDecimal exchangeRate = new ExchangeRate(Currency.getInstance("USD"), Currency.getInstance("EUR")).getRate();
        assertTrue(new BigDecimal(10).multiply(exchangeRate).compareTo(bankAccount.getBalance()) == 0);
    }

    @Test
    public void givenNewlyCreatedAccount_WhenMultipleThreadsWorkOnIt_ThenAccountShouldHaveCorrectBalance() throws InterruptedException {
        List<BigDecimal> transactions = asBigDecimals("200", "300", "50", "250", "120", "80",
                "200", "300", "50", "250", "120", "80",
                "200", "300", "50", "250", "120", "80",
                "200", "300", "50", "250", "120", "80",
                "200", "300", "50", "250", "120", "80",
                "200", "300", "50", "250", "120", "80",
                "200", "300", "50", "250", "120", "80",
                "200", "300", "50", "250", "120", "80",
                "200", "300", "50", "250", "120", "80",
                "200", "300", "50", "250", "120", "80");

        for (BigDecimal amount : transactions) {
            new Thread(() -> {
                bankAccount.deposit(amount);
            }).start();
        }

        assertTrue(new BigDecimal(10000).compareTo(bankAccount.getBalance()) == 0);
    }

    private List<BigDecimal> asBigDecimals(String... amountStringList) {
        List<BigDecimal> amountList = new ArrayList<>();
        for (String amountString : amountStringList) {
            amountList.add(new BigDecimal(amountString));
        }
        return amountList;
    }


}
