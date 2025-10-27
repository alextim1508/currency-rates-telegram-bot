package com.alextim.currencyrates.telegrambot.services;

import com.alextim.currencyrates.telegrambot.clients.TelegramClient;
import com.alextim.currencyrates.telegrambot.model.GetUpdatesRequest;
import com.alextim.currencyrates.telegrambot.model.GetUpdatesResponse;
import com.alextim.currencyrates.telegrambot.model.GetUpdatesResponse.Response;
import com.alextim.currencyrates.telegrambot.model.MessageTextProcessorResult;
import com.alextim.currencyrates.telegrambot.model.SendMessageRequest;
import com.alextim.currencyrates.telegrambot.services.processors.MessageTextProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {

    private final TelegramClient telegramClient;
    private final MessageTextProcessor processorGeneral;
    private final LastUpdateIdKeeper lastUpdateIdKeeper;

    @Override
    public void getUpdates() {
        try {
            log.info("Starting getUpdates process");

            long offset = lastUpdateIdKeeper.get();
            log.debug("Retrieved offset: {}", offset);

            GetUpdatesRequest request = new GetUpdatesRequest(offset);
            log.debug("Prepared GetUpdatesRequest: {}", request);

            GetUpdatesResponse response = telegramClient.getUpdates(request);
            log.debug("Received GetUpdatesResponse with {} results", response != null && response.getResult() != null ? response.getResult().size() : 0);

            long lastUpdateId = processResponse(response);

            // Calculate next offset
            lastUpdateId = lastUpdateId == 0 ? offset : lastUpdateId + 1;
            log.debug("Calculated next offset: {}", lastUpdateId);

            lastUpdateIdKeeper.set(lastUpdateId);
            log.info("Successfully updated lastUpdateId to: {}", lastUpdateId);
        } catch (RuntimeException ex) {
            log.error("Unhandled exception in getUpdates process", ex);
        }
    }

    private long processResponse(GetUpdatesResponse response) {
        if (response == null || response.getResult() == null) {
            log.warn("Received null or empty response from Telegram API");
            return 0;
        }

        log.info("Processing {} updates from Telegram API", response.getResult().size());

        long lastUpdateId = 0;
        for (Response responseMsg : response.getResult()) {
            if (responseMsg != null) {
                lastUpdateId = Math.max(lastUpdateId, responseMsg.getUpdateId());
                log.debug("Processing update ID: {}", responseMsg.getUpdateId());
                processMessage(responseMsg.getMessage());
            }
        }
        log.info("Processed updates, highest update ID found: {}", lastUpdateId);
        return lastUpdateId;
    }

    private void processMessage(GetUpdatesResponse.Message message) {
        if (message == null) {
            log.warn("Received null message, skipping processing");
            return;
        }

        log.info("Processing message ID: {} from chat ID: {}", message.getMessageId(), message.getChat().getId());
        log.debug("Full message object: {}", message);

        long chatId = message.getChat().getId();
        long messageId = message.getMessageId();
        String messageText = message.getText();

        log.debug("Processing message text: '{}'", messageText);
        MessageTextProcessorResult result = processorGeneral.process(messageText);

        if (result == null) {
            log.warn("Processor returned null result for message: {}", messageText);
            return;
        }

        String reply = result.getFailReply() != null ? result.getFailReply() : result.getOkReply();
        log.debug("Prepared reply: '{}'", reply);

        SendMessageRequest sendMessageRequest = new SendMessageRequest(chatId, reply, messageId);
        log.debug("Sending reply with SendMessageRequest: {}", sendMessageRequest);

        telegramClient.sendMessage(sendMessageRequest);
        log.info("Reply sent successfully for message ID: {} in chat ID: {}", messageId, chatId);
    }
}
