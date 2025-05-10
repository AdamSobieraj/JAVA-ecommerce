package ecom.pl.ecommerce.exchange;

import ecom.pl.ecommerce_shop.exchange.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CurrencyExchangeServiceTest {

    public static final String MID = "3.5";
    public static final String AMOUNT = "100";
    public static final String RESULT_AMOUNT_PLN_TO_USD = "350.0";
    public static final String RESULT_AMOUNT_USD_TO_PLN = "28.5714";

    @Mock
    private ExchangeRateService exchangeRateService;

    private CurrencyExchangeService currencyExchangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyExchangeService = new CurrencyExchangeService(exchangeRateService);
    }

    @ParameterizedTest
    @MethodSource("exchangeTestCases")
    @SneakyThrows
    void testExchange(TestCaseData testData) {
        // Given
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setMid(Double.parseDouble(MID));

        when(exchangeRateService.getLatestExchangeRate(any())).thenReturn(exchangeRate);

        ExchangeRequest exchangeRequest = ExchangeRequest.builder()
                .amount(testData.amount)
                .currency(testData.toCurrency)
                .build();

        // When
        ExchangeResult result = currencyExchangeService.exchange(exchangeRequest);

        // Then
        assertNotNull(result);
        assertEquals(testData.fromCurrency.getName(), result.getFromCurrency());
        assertEquals(testData.toCurrency.getName(), result.getToCurrency());
        assertEquals(testData.amount, result.getAmount());
        assertEquals(0, testData.expectedResultAmount.compareTo(result.getResultAmount()));
        assertEquals(testData.expectedExchangeRate, result.getExchangeRate());

        verify(exchangeRateService).getLatestExchangeRate(Currency.USD.getCode());
        verifyNoMoreInteractions(exchangeRateService);
    }

    private Stream<TestCaseData> exchangeTestCases() {
        return Stream.of(
                createTestCase(Currency.USD, Currency.PLN, new BigDecimal(AMOUNT),
                        new BigDecimal(RESULT_AMOUNT_PLN_TO_USD), new BigDecimal(MID)),
                createTestCase(Currency.PLN, Currency.USD, new BigDecimal(AMOUNT),
                        new BigDecimal(RESULT_AMOUNT_USD_TO_PLN), new BigDecimal(MID))
        );
    }

    private TestCaseData createTestCase(Currency toCurrency, Currency fromCurrency,
                                        BigDecimal amount, BigDecimal expectedResultAmount,
                                        BigDecimal expectedExchangeRate) {
        return new TestCaseData(toCurrency, fromCurrency, amount, expectedResultAmount, expectedExchangeRate);
    }

    private class TestCaseData {
        final Currency toCurrency;
        final Currency fromCurrency;
        final BigDecimal amount;
        final BigDecimal expectedResultAmount;
        final BigDecimal expectedExchangeRate;

        TestCaseData(Currency toCurrency, Currency fromCurrency, BigDecimal amount,
                     BigDecimal expectedResultAmount, BigDecimal expectedExchangeRate) {
            this.toCurrency = toCurrency;
            this.fromCurrency = fromCurrency;
            this.amount = amount;
            this.expectedResultAmount = expectedResultAmount;
            this.expectedExchangeRate = expectedExchangeRate;
        }
    }
}
