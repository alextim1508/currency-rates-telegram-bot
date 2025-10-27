package com.alextim.currencyrates.telegrambot.services.processors;


import org.springframework.stereotype.Service;
import com.alextim.currencyrates.telegrambot.model.MessageTextProcessorResult;

import static com.alextim.currencyrates.telegrambot.services.processors.Messages.EXPECTED_FORMAT_MESSAGE;

@Service("messageTextProcessorStart")
public class MessageTextProcessorStart implements MessageTextProcessor {
    @Override
    public MessageTextProcessorResult process(String msgText) {
        return new MessageTextProcessorResult(EXPECTED_FORMAT_MESSAGE.getText(), null);
    }
}
