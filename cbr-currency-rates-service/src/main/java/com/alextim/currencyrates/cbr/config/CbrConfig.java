package com.alextim.currencyrates.cbr.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.cbr")
@Getter
@Setter
@ToString
public class CbrConfig {
    private String url;
}