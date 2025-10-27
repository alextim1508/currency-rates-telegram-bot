package com.alextim.currencyrates.cbr.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CurrencyRate {
    String numCode;
    String charCode;
    String nominal;
    String name;
    String value;
}
