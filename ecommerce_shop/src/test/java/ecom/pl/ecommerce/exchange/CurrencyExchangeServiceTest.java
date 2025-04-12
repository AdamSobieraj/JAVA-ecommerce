package ecom.pl.ecommerce.exchange;

import ecom.pl.ecommerce_shop.exchange.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CurrencyExchangeServiceTest {

    public static final String MID = "3.5";
    public static final String AMOUNT = "100";
    public static final String RESULT_AMOUNT = "350.0";

    @Mock
    private ExchangeRateService exchangeRateService;

    private CurrencyExchangeService currencyExchangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyExchangeService = new CurrencyExchangeService(exchangeRateService);
    }

    @Test
    @SneakyThrows
    void testExchangePlnToUsd() {
        // Given
        BigDecimal amount = new BigDecimal(AMOUNT);
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setMid(Double.parseDouble(MID));
        when(exchangeRateService.getLatestExchangeRate(any())).thenReturn(exchangeRate);

        ExchangeRequest exchangeRequest = ExchangeRequest.builder()
                .amount(amount)
                .currency(Currency.USD)
                .build();

        // When
        ExchangeResult result = currencyExchangeService.exchange(exchangeRequest);

        // Then
        assertNotNull(result);
        assertEquals(Currency.PLN.getName(), result.getFromCurrency());
        assertEquals(Currency.USD.getName(), result.getToCurrency());
        assertEquals(amount, result.getAmount());
        assertEquals(new BigDecimal(RESULT_AMOUNT), result.getResultAmount());
        assertEquals(new BigDecimal(MID), result.getExchangeRate());

        verify(exchangeRateService).getLatestExchangeRate(Currency.USD.getCode());
        verifyNoMoreInteractions(exchangeRateService);
    }

    @Test
    @SneakyThrows
    void testExchangeUsdToPln() {
        // Given
        BigDecimal amount = new BigDecimal(AMOUNT);
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setMid(Double.parseDouble(MID));
        when(exchangeRateService.getLatestExchangeRate(any())).thenReturn(exchangeRate);

        ExchangeRequest exchangeRequest = ExchangeRequest.builder()
                .amount(amount)
                .currency(Currency.PLN)
                .build();

        // When
        ExchangeResult result = currencyExchangeService.exchange(exchangeRequest);

        // Then
        assertNotNull(result);
        assertEquals(Currency.USD.getName(), result.getFromCurrency());
        assertEquals(Currency.PLN.getName(), result.getToCurrency());
        assertEquals(amount, result.getAmount());
        assertEquals(new BigDecimal("28.5714"), result.getResultAmount());
        assertEquals(new BigDecimal(MID), result.getExchangeRate());

        verify(exchangeRateService).getLatestExchangeRate(Currency.USD.getCode());
        verifyNoMoreInteractions(exchangeRateService);
    }
}