package com.progressoft.induction;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.util.Currency;

public class ExchangeRate {

    private final Currency fromCurrency;
    private final Currency toCurrency;

    public ExchangeRate(Currency fromCurrency, Currency toCurrency) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
    }

    public BigDecimal getRate() {
        if(fromCurrency.getCurrencyCode().equals(toCurrency.getCurrencyCode())){
            return BigDecimal.ONE;
        }

        CurrencyExchangeInfo exchangeInfo = getExchangeInfo();
        if(toCurrency.getCurrencyCode().equals(exchangeInfo.getBase())){
            return BigDecimal.ONE.divide(getCurrencyRate(exchangeInfo, fromCurrency), 5, RoundingMode.CEILING);
        }else{
            return getCurrencyRate(exchangeInfo, toCurrency);
        }
    }

    private String buildExchangeUrl(Currency fromCurrency, Currency toCurrency) {
        StringBuilder builder = new StringBuilder();
        builder.append("http://api.fixer.io/latest?symbols=");
        builder.append(fromCurrency.getCurrencyCode());
        builder.append(",");
        builder.append(toCurrency.getCurrencyCode());
        return builder.toString();
    }

    private CurrencyExchangeInfo getExchangeInfo(){
        try {
            URL url = new URL(buildExchangeUrl(fromCurrency, toCurrency));
            URLConnection connection = url.openConnection();
            String response = readInStreamAsString(connection);
            Gson gson = new Gson();
            return gson.fromJson(response, CurrencyExchangeInfo.class);
        } catch (IOException e) {
            throw new IllegalStateException("Error while retrieving currency exchange!", e);
        }
    }

    private String readInStreamAsString(URLConnection connection) throws IOException {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = reader.readLine()) != null){
                builder.append(line);
                builder.append(System.lineSeparator());
            }
            return builder.toString();
        }
    }

    private BigDecimal getCurrencyRate(CurrencyExchangeInfo exchangeInfo, Currency currency) {
        BigDecimal fxRate = exchangeInfo.getRates().get(currency.getCurrencyCode());
        if(fxRate == null){
            throw new IllegalArgumentException("currency " + currency.getCurrencyCode() + " is not supported!");
        }
        return fxRate;
    }
}
