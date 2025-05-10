package ecom.pl.ecommerce_shop.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ExchangeRateService {

    @Setter
    @Value("${exchanges.url}")
    private String nbpUrl;

    private final RestTemplate restTemplate;
    private final ConcurrentHashMap<String, ExchangeRate> exchangeRates = new ConcurrentHashMap<>();

    public ExchangeRateService() {
        this.restTemplate = new RestTemplate();
    }

    @Scheduled(initialDelay = 0, fixedRate = 5 * 60 * 1000) // 0ms delay, runs every 5 minutes
    public void updateExchangeRates() {
        try {
            log.info("Updating exchange rates");

            // Fetching the response from the API
            ResponseEntity<String> response = restTemplate.getForEntity(nbpUrl, String.class);
            ObjectMapper objectMapper = new ObjectMapper();

            // Clean the JSON response by removing array brackets if necessary
            String jsonString = response.getBody().replaceAll("^\\[|\\]$", "");
            ExchangeRateTable exchangeRateTable = objectMapper.readValue(jsonString, ExchangeRateTable.class);

            // Clear the existing rates and update with new ones
            exchangeRates.clear();
            exchangeRateTable.getRates().forEach(entry -> {
                ExchangeRate currency = createExchangeRate(entry);
                exchangeRates.put(currency.getCode(), currency);
            });

        } catch (IOException e) {
            log.error("Failed to update exchange rates", e);
        }
    }

    private ExchangeRate createExchangeRate(ExchangeRate currency) {
        return ExchangeRate.builder()
                .code(currency.getCode())
                .currency(currency.getCode())
                .mid(currency.getMid())
                .build();
    }

    public ExchangeRate getLatestExchangeRate(String currencyCode) {
        return exchangeRates.getOrDefault(currencyCode, null);
    }
}
