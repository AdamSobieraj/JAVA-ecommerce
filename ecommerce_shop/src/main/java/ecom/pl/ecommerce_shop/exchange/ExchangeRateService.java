package ecom.pl.ecommerce_shop.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ExchangeRateService {

    @Setter
    @Value("${exchanges.url}")
    private String nbpUrl;

    private final WebClient webClient;
    private final ConcurrentHashMap<String, ExchangeRate> exchangeRates = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler;

    private final ObjectMapper objectMapper;

    public ExchangeRateService() {
        this.webClient = WebClient.create();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.objectMapper = new ObjectMapper();
        updateExchangeRates();
        scheduleUpdate();
    }

    private void updateExchangeRates() {
        log.info("Updating exchange rates");

        webClient.get()
                .uri(nbpUrl)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::removeJsonArrayBrackets)
                .flatMap(this::parseExchangeRateTable)
                .doOnNext(exchangeRateTable -> {
                    exchangeRates.clear();
                    exchangeRateTable.getRates().forEach(entry -> {
                        ExchangeRate currency = createExchangeRate(entry);
                        exchangeRates.put(currency.getCode(), currency);
                    });
                    log.info("Exchange rates updated successfully");
                })
                .doOnError(error -> log.error("Failed to update exchange rates", error))
                .subscribe();  // trigger subscription (non-blocking)
    }

    private String removeJsonArrayBrackets(String jsonString) {
        return jsonString.replaceAll("^\\[|\\]$", "");
    }

    private Mono<ExchangeRateTable> parseExchangeRateTable(String jsonString) {
        try {
            ExchangeRateTable table = objectMapper.readValue(jsonString, ExchangeRateTable.class);
            return Mono.just(table);
        } catch (IOException e) {
            return Mono.error(new RuntimeException("Failed to parse exchange rate table", e));
        }
    }

    private ExchangeRate createExchangeRate(ExchangeRate currency) {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setCode(currency.getCode());
        exchangeRate.setCurrency(currency.getCode());
        exchangeRate.setMid(currency.getMid());
        return exchangeRate;
    }

    public ExchangeRate getLatestExchangeRate(String currencyCode) {
        return exchangeRates.getOrDefault(currencyCode, null);
    }

    private void scheduleUpdate() {
        scheduler.scheduleAtFixedRate(this::updateExchangeRates, 24, 24, TimeUnit.HOURS);
    }
}
