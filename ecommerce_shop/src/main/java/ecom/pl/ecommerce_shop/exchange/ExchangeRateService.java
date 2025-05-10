package ecom.pl.ecommerce_shop.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ExchangeRateService {

    @Setter
    @Value("${exchanges.url}")
    private String nbpUrl;

    private final ConcurrentHashMap<String, ExchangeRate> exchangeRates = new ConcurrentHashMap<>();

    @Scheduled(initialDelay = 0, fixedRate = 5 * 60 * 1000)
    public void updateExchangeRates() {
        try {
            log.info("Updating exchange rates");

            WebClient webClient = WebClient.create();
            ResponseEntity<String> response = webClient.get()
                    .uri(nbpUrl)
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            ObjectMapper objectMapper = new ObjectMapper();

            String jsonString = response.getBody().replaceAll("^\\[|\\]$", "");
            ExchangeRateTable exchangeRateTable = objectMapper.readValue(jsonString, ExchangeRateTable.class);

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
