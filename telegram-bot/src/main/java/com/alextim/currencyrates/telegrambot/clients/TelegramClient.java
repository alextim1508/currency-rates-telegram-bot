package com.alextim.currencyrates.telegrambot.clients;


import com.alextim.currencyrates.telegrambot.model.GetUpdatesRequest;
import com.alextim.currencyrates.telegrambot.model.GetUpdatesResponse;
import com.alextim.currencyrates.telegrambot.model.SendMessageRequest;

public interface TelegramClient {

    GetUpdatesResponse getUpdates(GetUpdatesRequest request);

    void sendMessage(SendMessageRequest request);
}
