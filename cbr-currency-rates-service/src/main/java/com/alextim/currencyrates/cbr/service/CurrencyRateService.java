package com.alextim.currencyrates.cbr.service;


import com.alextim.currencyrates.cbr.config.CbrConfig;
import com.alextim.currencyrates.cbr.exception.CurrencyRateNotFoundException;
import com.alextim.currencyrates.cbr.requester.CbrRequester;
import com.alextim.currencyrates.cbr.model.CurrencyRate;
import com.alextim.currencyrates.cbr.parser.CurrencyRateParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyRateService {

    public static final String DATE_FORMAT = "dd/MM/yyyy";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private final CbrRequester cbrRequester;
    private final CurrencyRateParser currencyRateParser;
    private final CbrConfig cbrConfig;

    @Cacheable(value = "currencyRates", key = "#date")
    public List<CurrencyRate> getAllCurrencyRatesForDate(LocalDate date) {
        log.info("getAllCurrencyRatesForDate. date:{}", date);
        var urlWithParams = String.format("%s?date_req=%s", cbrConfig.getUrl(), DATE_FORMATTER.format(date));
        var ratesAsXml = cbrRequester.getRatesAsXml(urlWithParams);
        return currencyRateParser.parse(ratesAsXml);
    }

    public CurrencyRate getCurrencyRate(String currency, LocalDate date) {
        log.info("getCurrencyRate. currency:{}, date:{}", currency, date);
        List<CurrencyRate> rates = getAllCurrencyRatesForDate(date);

        return rates.stream()
                .filter(rate -> currency.equals(rate.getCharCode()))
                .findFirst()
                .orElseThrow(() ->
                        new CurrencyRateNotFoundException("Currency Rate not found. Currency:" + currency + ", date:" + date));
    }
}