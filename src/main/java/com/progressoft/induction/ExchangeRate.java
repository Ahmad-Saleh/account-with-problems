package com.progressoft.induction;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ExchangeRate {

    private Map<Exchange, BigDecimal> exchangeRates = new HashMap<>();
    private final Currency fromCurrency;
    private final Currency toCurrency;

    public ExchangeRate(Currency fromCurrency, Currency toCurrency) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        addRate("JOD", "USD", "1.43");
        addRate("USD", "JOD", "0.71");
        addRate("EUR", "USD", "1.1");
        addRate("USD", "EUR", "0.91");
        addRate("USD", "THB", "0.5");
        addRate("THB", "USD", "0.02");
    }

    private BigDecimal addRate(String fromCurrency, String toCurrency, String exchangeRate) {
        return this.exchangeRates.put(new Exchange(Currency.getInstance(fromCurrency), Currency.getInstance(toCurrency)), new BigDecimal(exchangeRate));
    }

    public BigDecimal getRate() {
        if (fromCurrency.getCurrencyCode().equals(toCurrency.getCurrencyCode())) {
            return BigDecimal.ONE;
        }

        // usually exchange rate is retrieved from an external system
        // this is a simulation for network delay
        networkDelay();
        return exchangeRates.get(new Exchange(fromCurrency, toCurrency));
    }

    private void networkDelay() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(50, 1000));
        } catch (InterruptedException e) {
            // ignore
        }
    }

    class Exchange {
        Currency fromCurrency;
        Currency toCurrency;

        public Exchange(Currency fromCurrency, Currency toCurrency) {
            this.fromCurrency = fromCurrency;
            this.toCurrency = toCurrency;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Exchange exchange = (Exchange) o;

            if (fromCurrency != null ? !fromCurrency.equals(exchange.fromCurrency) : exchange.fromCurrency != null)
                return false;
            return toCurrency != null ? toCurrency.equals(exchange.toCurrency) : exchange.toCurrency == null;
        }

        @Override
        public int hashCode() {
            int result = fromCurrency != null ? fromCurrency.hashCode() : 0;
            result = 31 * result + (toCurrency != null ? toCurrency.hashCode() : 0);
            return result;
        }
    }
}
