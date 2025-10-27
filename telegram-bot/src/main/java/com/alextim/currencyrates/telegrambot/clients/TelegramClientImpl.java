package com.alextim.currencyrates.telegrambot.clients;

import com.alextim.currencyrates.telegrambot.config.TelegramClientConfig;
import com.alextim.currencyrates.telegrambot.exception.TelegramException;
import com.alextim.currencyrates.telegrambot.model.GetUpdatesRequest;
import com.alextim.currencyrates.telegrambot.model.GetUpdatesResponse;
import com.alextim.currencyrates.telegrambot.model.SendMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@Slf4j
public class TelegramClientImpl implements TelegramClient {

    private final RestClient restClient;
    private final TelegramClientConfig clientConfig;

    public TelegramClientImpl(RestClient.Builder restClientBuilder,
                              TelegramClientConfig clientConfig) {
        this.restClient = restClientBuilder.build();
        this.clientConfig = clientConfig;
    }

    @Override
    public GetUpdatesResponse getUpdates(GetUpdatesRequest request) {
        log.info("Initiating getUpdates call to Telegram API. Request: {}", request);
        String url = makeUrl("getUpdates");
        log.debug("Full request URL for getUpdates: {}", url);

        try {
            GetUpdatesResponse updates = restClient.post()
                    .uri(url)
                    .body(request)
                    .retrieve()
                    .body(GetUpdatesResponse.class);

            log.debug("Raw GetUpdatesResponse object received from Telegram API: {}", updates);

            log.info("Successfully retrieved {} updates from Telegram API.",
                    updates.getResult() != null ? updates.getResult().size() : 0);

            return updates;

        } catch (RestClientException ex) {
            log.error("Error calling Telegram API (getUpdates). URL: {}, Request: {}", url, request, ex);
            throw new TelegramException("Error calling Telegram API for getUpdates", ex);
        }
    }

    @Override
    public void sendMessage(SendMessageRequest request) {
        log.info("Initiating sendMessage call to Telegram API. Request: {}", request);
        String url = makeUrl("sendMessage");
        log.debug("Full request URL for sendMessage: {}", url);

        try {
            String responseAsString = restClient.post()
                    .uri(url)
                    .body(request)
                    .retrieve()
                    .body(String.class);

            log.debug("Raw response from Telegram API sendMessage (first 200 chars): {}",
                    responseAsString.substring(0, Math.min(200, responseAsString.length())));

        } catch (RestClientException ex) {
            log.error("RestClient error during sendMessage to Telegram API. URL: {}, Request: {}", url, request, ex);
            throw new TelegramException("Error calling Telegram API for sendMessage", ex);
        } catch (RuntimeException ex) {
            log.error("Unexpected error during sendMessage to Telegram API. URL: {}, Request: {}", url, request, ex);
            throw new TelegramException("Error processing sendMessage request for Telegram API", ex);
        }
    }

    private String makeUrl(String apiRequest) {
        return String.format("%s/bot%s/%s", clientConfig.getUrl(), clientConfig.getBotToken(), apiRequest);
    }
}