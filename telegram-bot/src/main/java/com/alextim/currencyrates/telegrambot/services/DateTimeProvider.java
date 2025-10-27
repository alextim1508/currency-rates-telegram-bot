package com.alextim.currencyrates.telegrambot.services;

import java.time.LocalDateTime;

public interface DateTimeProvider {
    LocalDateTime get();
}
