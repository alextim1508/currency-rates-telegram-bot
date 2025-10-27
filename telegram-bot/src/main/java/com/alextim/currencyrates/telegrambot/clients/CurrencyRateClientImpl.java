package com.alextim.currencyrates.telegrambot.clients;

import com.alextim.currencyrates.telegrambot.config.CurrencyRateClientConfig;
import com.alextim.currencyrates.telegrambot.exception.CurrencyRateClientException;
import com.alextim.currencyrates.telegrambot.model.CurrencyRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@Slf4j
public class CurrencyRateClientImpl implements CurrencyRateClient {
    public static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private final RestClient restClient;

    private final CurrencyRateClientConfig currencyRateClientConfig;

    public CurrencyRateClientImpl(CurrencyRateClientConfig currencyRateClientConfig,
                                  RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
        this.currencyRateClientConfig = currencyRateClientConfig;
    }

    @Override
    public CurrencyRate getCurrencyRate(String rateType, String currency, LocalDate date) {
        log.info("Initiating getCurrencyRate call. rateType: {}, currency: {}, date: {}", rateType, currency, date);

        try {
            Map<String, String> urls = currencyRateClientConfig.getUrls();
            if (urls == null || urls.isEmpty()) {
                log.error("Currency rate client URLs configuration is missing or empty");
                throw new CurrencyRateClientException("Currency rate client URLs configuration is missing or empty");
            }

            String url = urls.get(rateType);
            if (url == null) {
                log.error("No URL configured for rateType: {}", rateType);
                throw new CurrencyRateClientException("No URL configured for rateType: " + rateType);
            }

            String urlWithParams = String.format("%s/%s/%s", url, currency, DATE_FORMATTER.format(date));
            log.debug("Constructed request URL: {}", urlWithParams);

            CurrencyRate response = restClient.get()
                    .uri(urlWithParams)
                    .retrieve()
                    .body(CurrencyRate.class);

            log.debug("Received CurrencyRate response: {}", response);
            log.info("Successfully retrieved currency rate for rateType: {}, currency: {}, date: {}", rateType, currency, date);
            return response;

        } catch (RestClientException ex) {
            log.error("RestClient error while fetching currency rate. rateType: {}, currency: {}, date: {}", rateType, currency, date, ex);
            throw new CurrencyRateClientException("Error calling currency rate service for currency: " + currency + ", date: " + date, ex);
        } catch (RuntimeException ex) {
            log.error("Unexpected error while fetching currency rate. rateType: {}, currency: {}, date: {}", rateType, currency, date, ex);
            throw new CurrencyRateClientException("Can't get currencyRate. currency:" + currency + ", date:" + date, ex);
        }
    }
}
