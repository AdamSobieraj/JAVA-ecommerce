package ecom.pl.ecommerce.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.pl.ecommerce_shop.exchange.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ExchangeControllerTest {

    public static final String AMOUNT = "100";

    private static final String BASE_URL = "/api/v1/exchange";

    @Mock
    private CurrencyExchangeService currencyExchangeService;

    @InjectMocks
    private ExchangeController exchangeController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    ExchangeResult expectedResult;
    ExchangeRequest requestUSDtoPLN;
    ExchangeRequest requestPLNtoUSD;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(exchangeController).build();

        expectedResult = new ExchangeResult();

        expectedResult.setFromCurrency(Currency.USD.getName());
        expectedResult.setToCurrency(Currency.PLN.getName());
        expectedResult.setAmount(new BigDecimal("100"));
        expectedResult.setResultAmount(new BigDecimal("87.32"));
        expectedResult.setExchangeRate(new BigDecimal("0.8732"));

        requestUSDtoPLN = new ExchangeRequest();
        requestUSDtoPLN.setAmount(new BigDecimal(AMOUNT));
        requestUSDtoPLN.setCurrency(Currency.PLN);

        requestPLNtoUSD = new ExchangeRequest();
        requestPLNtoUSD.setAmount(new BigDecimal(AMOUNT));
        requestPLNtoUSD.setCurrency(Currency.USD);
    }

    @Test
    @SneakyThrows
    void testExchangePlnToUsd() {
        // Given
        when(currencyExchangeService.exchange(any())).thenReturn(expectedResult);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestPLNtoUSD)))
                .andExpect(status().isOk())
                .andReturn();


        String responseContent = result.getResponse().getContentAsString();
        ExchangeResult actualResult = objectMapper.readValue(responseContent, ExchangeResult.class);

        // Then
        assertEquals(BigDecimal.valueOf(87.32), actualResult.getResultAmount());
        assertEquals(BigDecimal.valueOf(0.8732), actualResult.getExchangeRate());
        assertEquals(Currency.USD.getName(), actualResult.getFromCurrency());
        assertEquals(Currency.PLN.getName(), actualResult.getToCurrency());
    }

    @Test
    @SneakyThrows
    void testExchangeUsdToPln() {
        // Given
        when(currencyExchangeService.exchange(any())).thenReturn(expectedResult);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUSDtoPLN))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String responseContent = result.getResponse().getContentAsString();
        ExchangeResult actualResult = objectMapper.readValue(responseContent, ExchangeResult.class);

        assertEquals(BigDecimal.valueOf(87.32), actualResult.getResultAmount());
        assertEquals(BigDecimal.valueOf(0.8732), actualResult.getExchangeRate());
        assertEquals(Currency.USD.getName(), actualResult.getFromCurrency());
        assertEquals(Currency.PLN.getName(), actualResult.getToCurrency());
    }
}