package com.alextim.currencyrates.telegrambot.exception;

public class CurrencyRateClientException extends RuntimeException {

    public CurrencyRateClientException(String msg) {
        super(msg);
    }

    public CurrencyRateClientException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
