package ecom.pl.ecommerce_shop.exchange;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class CurrencyExchangeService {

    private final ExchangeRateService exchangeRateService;

    private final Map<String, ExchangeStrategy> strategies = new HashMap<>();

    public CurrencyExchangeService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
        initializeStrategies();
    }

    private void initializeStrategies() {
        strategies.put("USD", new PlnToUsdStrategy(exchangeRateService));
        strategies.put("PLN", new UsdToPlnStrategy(exchangeRateService));
    }

    public ExchangeResult exchange(ExchangeRequest request) {
        return executeStrategy(request.getCurrency().getCode(), request.getAmount());
    }

    private ExchangeResult executeStrategy(String strategyKey, BigDecimal amount) {
        ExchangeStrategy strategy = strategies.get(strategyKey);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown strategy: " + strategyKey);
        }
        return strategy.execute(amount);
    }

}
