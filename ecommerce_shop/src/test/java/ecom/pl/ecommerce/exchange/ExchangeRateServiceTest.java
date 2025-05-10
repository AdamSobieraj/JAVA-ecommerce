package ecom.pl.ecommerce.exchange;

import ecom.pl.ecommerce_shop.exchange.ExchangeRate;
import ecom.pl.ecommerce_shop.exchange.ExchangeRateService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ExchangeRateServiceTest {

    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        exchangeRateService = new ExchangeRateService();
    }

    @ParameterizedTest
    @CsvSource({
            "AUD",
            "CAD",
            "CHF"
    })
    void testGetLatestExchangeRate_ReturnsNullForUnknownCurrency(String currencyCode) {
        // When
        ExchangeRate result = exchangeRateService.getLatestExchangeRate(currencyCode);

        // Then
        assertNull(result, "ExchangeRate should be null for unknown currency code");
    }
}
