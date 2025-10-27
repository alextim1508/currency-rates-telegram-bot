package com.alextim.currencyrates.cbr.parser;

import com.alextim.currencyrates.cbr.model.CurrencyRate;

import java.util.List;

public interface CurrencyRateParser {

    List<CurrencyRate> parse(String ratesAsString);
}
