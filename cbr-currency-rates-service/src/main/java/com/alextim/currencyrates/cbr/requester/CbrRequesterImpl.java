package com.alextim.currencyrates.cbr.requester;

import com.alextim.currencyrates.cbr.exception.CbrRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@Slf4j
public class CbrRequesterImpl implements CbrRequester {

    private final RestClient restClient;

    public CbrRequesterImpl(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
        log.info("CBR Requester initialized with RestClient");
    }

    @Override
    public String getRatesAsXml(String url) {
        log.info("Sending request to CBR API: {}", url);
        try {
            String response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);
            log.debug("Received response from CBR API, length: {}", response != null ? response.length() : 0);
            return response;
        } catch (RestClientException e) {
            log.error("Error occurred while requesting CBR API: {}. URL: {}", e.getMessage(), url, e);
            throw new CbrRequestException("Failed to retrieve data from CBR API", e);
        }
    }
}