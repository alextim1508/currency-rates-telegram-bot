package com.alextim.currencyrates.telegrambot;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class CurrencyRatesBot {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(CurrencyRatesBot.class).run(args);
    }
}
