package com.alextim.currencyrates.cbr;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class CbrRatesApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(CbrRatesApp.class).run(args);
    }
}