package com.alextim.currencyrates.telegrambot.services.processors;

import com.alextim.currencyrates.telegrambot.model.CurrencyRate;
import com.alextim.currencyrates.telegrambot.clients.CurrencyRateClient;
import com.alextim.currencyrates.telegrambot.services.DateTimeProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.alextim.currencyrates.telegrambot.model.MessageTextProcessorResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Slf4j
@AllArgsConstructor
@Service("messageTextProcessorRate")
public class MessageTextProcessorRate implements MessageTextProcessor {

    private static final String CBR_RATE_CONST = "CBR";
    private static final String DATE_FORMAT_ZERO = "dd-MM-yyyy";
    private static final String DATE_FORMAT = "d-MM-yyyy";
    private static final DateTimeFormatter DATE_FORMATTER_ZERO = DateTimeFormatter.ofPattern(DATE_FORMAT_ZERO);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private final CurrencyRateClient currencyRateClient;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public MessageTextProcessorResult process(String msgText) {
        log.info("Processing message text for currency rate: '{}'", msgText);

        String[] textParts = msgText.split(" ", -1); // Используем -1 для сохранения пустых строк, если они есть
        log.debug("Message parts: {}", java.util.Arrays.toString(textParts));

        if (textParts.length < 1 || textParts.length > 3) {
            log.warn("Invalid number of parts in message: '{}'. Expected 1-3 parts.", msgText);
            return new MessageTextProcessorResult(null, Messages.EXPECTED_FORMAT_MESSAGE.getText());
        }

        String rateType = null;
        String currency = null;
        String dateAsString = null;
        LocalDate date = null;

        if (textParts.length == 3) {
            rateType = textParts[0];
            currency = textParts[1];
            dateAsString = textParts[2];
            log.debug("Parsed as 3 parts: rateType='{}', currency='{}', date='{}'", rateType, currency, dateAsString);
        } else if (textParts.length == 2) {
            rateType = CBR_RATE_CONST;
            currency = textParts[0];
            dateAsString = textParts[1];
            log.debug("Parsed as 2 parts, using default rateType: rateType='{}', currency='{}', date='{}'", rateType, currency, dateAsString);
        } else  {
            rateType = CBR_RATE_CONST;
            currency = textParts[0];
            date = dateTimeProvider.get().toLocalDate();
            log.debug("Parsed as 1 part, using default rateType and current date: rateType='{}', currency='{}', date='{}'", rateType, currency, date);
        }

        if (textParts.length == 3 || textParts.length == 2) {
            try {
                date = parseDate(dateAsString);
                log.debug("Successfully parsed date string '{}' to date: {}", dateAsString, date);
            } catch (Exception ex) {
                log.error("Date parsing error for string: '{}'. Expected format dd-MM-yyyy or d-MM-yyyy.", dateAsString, ex);
                return new MessageTextProcessorResult(null, Messages.DATA_FORMAT_MESSAGE.getText());
            }
        }

        if (rateType == null || currency == null) {
            log.error("Critical error: rateType or currency is null after parsing. rateType='{}', currency='{}'", rateType, currency);
            throw new IllegalArgumentException("rateType:" + rateType + " or currency:" + currency + " is null");
        }

        log.info("Fetching currency rate. rateType: '{}', currency: '{}', date: '{}'", rateType, currency, date);

        try {
            CurrencyRate rate = currencyRateClient.getCurrencyRate(
                    rateType.toUpperCase(),
                    currency.toUpperCase(),
                    date);

            log.info("Successfully retrieved currency rate for {} on {}: {}", currency.toUpperCase(), date, rate.getValue());
            return new MessageTextProcessorResult(rate.getValue(), null);

        } catch (RuntimeException e) {
            log.error("Error retrieving currency rate. rateType: '{}', currency: '{}', date: '{}'", rateType, currency, date, e);
            return new MessageTextProcessorResult(null, Messages.INNER_ERROR_MESSAGE.getText());
        }
    }

    private LocalDate parseDate(String dateAsString) {
        log.debug("Attempting to parse date string: '{}'", dateAsString);
        try {
            LocalDate result = LocalDate.parse(dateAsString, DATE_FORMATTER_ZERO);
            log.debug("Parsed with format '{}': {}", DATE_FORMATTER_ZERO, result);
            return result;
        } catch (RuntimeException ex) {
            log.debug("Parsing with format '{}' failed for '{}', trying format '{}'", DATE_FORMATTER_ZERO, dateAsString, DATE_FORMATTER);
            LocalDate result = LocalDate.parse(dateAsString, DATE_FORMATTER);
            log.debug("Parsed with format '{}': {}", DATE_FORMATTER, result);
            return result;
        }
    }
}