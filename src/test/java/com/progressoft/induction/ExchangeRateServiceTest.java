package com.progressoft.induction;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertTrue;

public class ExchangeRateServiceTest {

    @Test
    public void givenSameCurrency_WhenRequestingExchange_ThenOneShouldBeReturned() {
        BigDecimal fxRate = createExchangeRate("USD", "USD").getRate();
        assertTrue(BigDecimal.ONE.compareTo(fxRate) == 0);
    }

    @Test
    public void givenDifferentCurrencies_WhenRequestingExchange_ThenNumberOtherThanOneShouldBeReturned() {
        BigDecimal fxRate = createExchangeRate("EUR", "USD").getRate();
        assertTrue(BigDecimal.ONE.compareTo(fxRate) != 0);
    }

    @Test
    public void givenDifferentCurrencies_WhenExchangingForthAndBack_ThenMultiplyingBothExchangesShouldBeOne() {
        BigDecimal fxRate = createExchangeRate("THB", "USD").getRate();
        BigDecimal reverseFxRate = createExchangeRate("USD", "THB").getRate();
        assertTrue(fxRate.multiply(reverseFxRate).compareTo(BigDecimal.ONE) != 0);
    }

    private ExchangeRate createExchangeRate(String fromCurrency, String toCurrency) {
        return new ExchangeRate(Currency.getInstance(fromCurrency), Currency.getInstance(toCurrency));
    }
}
