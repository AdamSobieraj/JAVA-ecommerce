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

class PlnToUsdStrategyTest {

    public static final String MID = "3.5";
    public static final String AMOUNT = "100";
    public static final String RESULT_AMOUNT = "350.0";

    @Mock
    private ExchangeRateService exchangeRateService;

    private PlnToUsdStrategy plnToUsdStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        plnToUsdStrategy = new PlnToUsdStrategy(exchangeRateService);
    }

    @Test
    @SneakyThrows
    void testExchangePlnToUsd() {
        // Given
        BigDecimal amount = new BigDecimal(AMOUNT);
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setMid(Double.parseDouble(MID));
        when(exchangeRateService.getLatestExchangeRate(Currency.USD.getCode())).thenReturn(exchangeRate);

        // When
        ExchangeResult result = plnToUsdStrategy.execute(amount);

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
}