package com.alextim.currencyrates.telegrambot.services.processors;

import com.alextim.currencyrates.telegrambot.model.MessageTextProcessorResult;

public interface MessageTextProcessor {
    MessageTextProcessorResult process(String msgText);
}
