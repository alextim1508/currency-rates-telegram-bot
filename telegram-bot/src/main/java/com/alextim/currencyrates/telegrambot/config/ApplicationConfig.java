package com.alextim.currencyrates.telegrambot.config;

import com.alextim.currencyrates.telegrambot.clients.TelegramClient;
import com.alextim.currencyrates.telegrambot.services.LastUpdateIdKeeper;
import com.alextim.currencyrates.telegrambot.services.TelegramService;
import com.alextim.currencyrates.telegrambot.services.TelegramServiceImpl;
import com.alextim.currencyrates.telegrambot.services.processors.MessageTextProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(CurrencyRateClientConfig.class)
public class ApplicationConfig {

    @Bean
    public TelegramImporterScheduled telegramImporterScheduled(TelegramClient telegramClient,
                                                               TelegramClientConfig telegramClientConfig,
                                                               @Qualifier("messageTextProcessorGeneral") MessageTextProcessor processorGeneral,
                                                               LastUpdateIdKeeper lastUpdateIdKeeper) {
        var telegramService = new TelegramServiceImpl(telegramClient, processorGeneral, lastUpdateIdKeeper);
        return new TelegramImporterScheduled(telegramService, telegramClientConfig);
    }

    public static class TelegramImporterScheduled {
        public TelegramImporterScheduled(TelegramService telegramService, TelegramClientConfig telegramClientConfig) {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(telegramService::getUpdates, 1000, telegramClientConfig.getRefreshRateMs(), TimeUnit.MILLISECONDS);
        }
    }
}
