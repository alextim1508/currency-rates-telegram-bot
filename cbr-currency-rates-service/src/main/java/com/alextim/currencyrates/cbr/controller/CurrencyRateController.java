package com.alextim.currencyrates.cbr.controller;

import com.alextim.currencyrates.cbr.model.CurrencyRate;
import com.alextim.currencyrates.cbr.service.CurrencyRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "${app.rest.api.prefix}/v1")
public class CurrencyRateController {

    private final CurrencyRateService currencyRateService;

    @GetMapping("/currencyrate/{currency}/{date}")
    public CurrencyRate getCurrencyRate(@PathVariable("currency") String currency,
                                        @DateTimeFormat(pattern = "dd-MM-yyyy") @PathVariable("date") LocalDate date)  {
        log.info("getCurrencyRate, currency:{}, date:{}", currency, date);

        return currencyRateService.getCurrencyRate(currency, date);
    }
}
