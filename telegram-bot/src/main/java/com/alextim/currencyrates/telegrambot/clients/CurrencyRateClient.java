package com.alextim.currencyrates.telegrambot.clients;

import com.alextim.currencyrates.telegrambot.model.CurrencyRate;

import java.time.LocalDate;

public interface CurrencyRateClient {

    CurrencyRate getCurrencyRate(String rateType, String currency, LocalDate date);
}
