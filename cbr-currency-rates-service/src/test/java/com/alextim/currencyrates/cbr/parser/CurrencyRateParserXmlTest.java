package com.alextim.currencyrates.cbr.parser;


import com.alextim.currencyrates.cbr.model.CurrencyRate;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CurrencyRateParserXmlTest {

    @Test
    void parseTest() throws IOException, URISyntaxException {
        var parser = new CurrencyRateParserXml();
        var uri = ClassLoader.getSystemResource("cbr_response.xml").toURI();
        var ratesXml = Files.readString(Paths.get(uri), Charset.forName("Windows-1251"));

        var rates = parser.parse(ratesXml);

        assertThat(rates.size()).isEqualTo(34);
        assertThat(rates.contains(getUSDRate())).isTrue();
        assertThat(rates.contains(getEURRate())).isTrue();
        assertThat(rates.contains(getJPYRate())).isTrue();
    }

    CurrencyRate getUSDRate() {
        return CurrencyRate.builder()
                .numCode("840")
                .charCode("USD")
                .nominal("1")
                .name("Доллар США")
                .value("74,0448")
                .build();
    }

    CurrencyRate getEURRate() {
        return CurrencyRate.builder()
                .numCode("978")
                .charCode("EUR")
                .nominal("1")
                .name("Евро")
                .value("89,4461")
                .build();
    }

    CurrencyRate getJPYRate() {
        return CurrencyRate.builder()
                .numCode("392")
                .charCode("JPY")
                .nominal("100")
                .name("Японских иен")
                .value("69,4702")
                .build();
    }
}