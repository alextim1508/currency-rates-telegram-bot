package com.alextim.currencyrates.telegrambot.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.telegram")
@Getter
@Setter
@ToString(exclude = "botToken")
public class TelegramClientConfig {
    private String url;
    private String botToken;
    private int refreshRateMs;
}
