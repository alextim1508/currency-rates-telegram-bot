package com.alextim.currencyrates.telegrambot.services;

public interface LastUpdateIdKeeper {
    long get();

    void set(long lastUpdateId);
}
