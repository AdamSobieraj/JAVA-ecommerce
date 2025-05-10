package ecom.pl.ecommerce.exchange;

import ecom.pl.ecommerce_shop.exchange.ExchangeRate;
import ecom.pl.ecommerce_shop.exchange.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateServiceTest {

    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        exchangeRateService = new ExchangeRateService();
        injectTestExchangeRates();
    }

    @ParameterizedTest
    @MethodSource("exchangeRateParams")
    void testParseExchangeRateTableParameterized(String jsonResponse, Map<String, Double> expectedRates) throws Exception {
        ResponseEntity<String> response = ResponseEntity.ok(jsonResponse);

        Method parseMethod = ExchangeRateService.class.getDeclaredMethod("parseExchangeRateTable", ResponseEntity.class);
        parseMethod.setAccessible(true);
        parseMethod.invoke(exchangeRateService, response);

        Field ratesField = ExchangeRateService.class.getDeclaredField("exchangeRates");
        ratesField.setAccessible(true);
        ConcurrentHashMap<String, ExchangeRate> ratesMap = (ConcurrentHashMap<String, ExchangeRate>) ratesField.get(exchangeRateService);

        assertEquals(expectedRates.size(), ratesMap.size());

        for (Map.Entry<String, Double> entry : expectedRates.entrySet()) {
            ExchangeRate rate = ratesMap.get(entry.getKey());
            assertNotNull(rate, "Rate for " + entry.getKey() + " should not be null");
            assertEquals(entry.getKey(), rate.getCode());
            assertEquals(entry.getValue(), rate.getMid());
        }
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> exchangeRateParams() {
        String json1 = "[{\"table\":\"A\",\"no\":\"095/A/NBP/2024\",\"effectiveDate\":\"2024-05-10\",\"rates\":[{\"currency\":\"dolar amerykański\",\"code\":\"USD\",\"mid\":4.2},{\"currency\":\"euro\",\"code\":\"EUR\",\"mid\":4.6}]}]";
        Map<String, Double> expected1 = Map.of("USD", 4.2, "EUR", 4.6);

        String json2 = "[{\"table\":\"A\",\"no\":\"096/A/NBP/2024\",\"effectiveDate\":\"2024-05-11\",\"rates\":[{\"currency\":\"funt szterling\",\"code\":\"GBP\",\"mid\":5.1}]}]";
        Map<String, Double> expected2 = Map.of("GBP", 5.1);

        String json3 = "[{\"table\":\"A\",\"no\":\"097/A/NBP/2024\",\"effectiveDate\":\"2024-05-12\",\"rates\":[]}]";
        Map<String, Double> expected3 = Map.of(); // empty rates

        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(json1, expected1),
                org.junit.jupiter.params.provider.Arguments.of(json2, expected2),
                org.junit.jupiter.params.provider.Arguments.of(json3, expected3)
        );
    }

    @ParameterizedTest
    @org.junit.jupiter.params.provider.CsvSource({
            "USD,1.1",
            "EUR,0.9",
            "GBP,0.8",
            "JPY,150.0",
            "UNKNOWN,null"
    })
    void testGetLatestExchangeRate(String currencyCode, String expectedMid) {
        ExchangeRate result = exchangeRateService.getLatestExchangeRate(currencyCode);

        if ("null".equals(expectedMid)) {
            assertNull(result, "Expected null for unknown currency code");
        } else {
            assertNotNull(result, "ExchangeRate should not be null");
            assertEquals(currencyCode, result.getCode());
            assertEquals(Double.parseDouble(expectedMid), result.getMid());
        }
    }

    private void injectTestExchangeRates() {
        ConcurrentHashMap<String, ExchangeRate> testRates = new ConcurrentHashMap<>();

        ExchangeRate usdRate = new ExchangeRate();
        usdRate.setCode("USD");
        usdRate.setCurrency("USD");
        usdRate.setMid(1.1);
        testRates.put("USD", usdRate);

        ExchangeRate eurRate = new ExchangeRate();
        eurRate.setCode("EUR");
        eurRate.setCurrency("EUR");
        eurRate.setMid(0.9);
        testRates.put("EUR", eurRate);

        ExchangeRate gbpRate = new ExchangeRate();
        gbpRate.setCode("GBP");
        gbpRate.setCurrency("GBP");
        gbpRate.setMid(0.8);
        testRates.put("GBP", gbpRate);

        ExchangeRate jpyRate = new ExchangeRate();
        jpyRate.setCode("JPY");
        jpyRate.setCurrency("JPY");
        jpyRate.setMid(150.0);
        testRates.put("JPY", jpyRate);

        try {
            Field field = ExchangeRateService.class.getDeclaredField("exchangeRates");
            field.setAccessible(true);
            field.set(exchangeRateService, testRates);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
