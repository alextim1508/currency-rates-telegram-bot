package com.alextim.currencyrates.telegrambot.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "app.currency-rate-client")
@ToString
@Getter
@Setter
public class CurrencyRateClientConfig {
    private Map<String, String> urls;
}
