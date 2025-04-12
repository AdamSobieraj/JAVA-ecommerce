//package ecom.pl.ecommerce.exchange;
//
//import ecom.pl.ecommerce_shop.exchange.Currency;
//import ecom.pl.ecommerce_shop.exchange.ExchangeRate;
//import ecom.pl.ecommerce_shop.exchange.ExchangeRateService;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.ApplicationContext;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.web.client.RestTemplate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(classes = ExchangeRateServiceTest.class)
//class ExchangeRateServiceTest {
//
//    @Mock
//    private RestTemplate restTemplate;
//
//    @InjectMocks
//    private ApplicationContext applicationContext;
//
//    @Mock
//    private ExchangeRateService exchangeRateService;
//
//    @Test
//    @SneakyThrows
//    void getLatestExchangeRate() {
//        // Given
//        ResponseEntity<String> response = new ResponseEntity<>(jsonRes(), HttpStatus.OK);
//        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(response);
//
//        // Get the ExchangeRateService bean from Spring context
//        ExchangeRateService exchangeRateService = applicationContext.getBean(ExchangeRateService.class);
//
//        // When
//        ExchangeRate result = exchangeRateService.getLatestExchangeRate(Currency.USD.getCode());
//
//        // Then
//        assertNotNull(result);
//        assertEquals("USD", result.getCode());
////        assertEquals(4.0738, result.getMid());
//    }
//
//    private String jsonRes() {
//        return "[{\n" +
//                "  \"table\": \"A\",\n" +
//                "  \"no\": \"229/A/NBP/2024\",\n" +
//                "  \"effectiveDate\": \"2024-11-26\",\n" +
//                "  \"rates\": [\n" +
//                "    {\n" +
//                "      \"currency\": \"bat (Tajlandia)\",\n" +
//                "      \"code\": \"THB\",\n" +
//                "      \"mid\": 0.1184\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"dolar amerykański\",\n" +
//                "      \"code\": \"USD\",\n" +
//                "      \"mid\": 4.1073\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"dolar australijski\",\n" +
//                "      \"code\": \"AUD\",\n" +
//                "      \"mid\": 2.6637\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"dolar Hongkongu\",\n" +
//                "      \"code\": \"HKD\",\n" +
//                "      \"mid\": 0.5278\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"dolar kanadyjski\",\n" +
//                "      \"code\": \"CAD\",\n" +
//                "      \"mid\": 2.914\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"dolar nowozelandzki\",\n" +
//                "      \"code\": \"NZD\",\n" +
//                "      \"mid\": 2.3998\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"dolar singapurski\",\n" +
//                "      \"code\": \"SGD\",\n" +
//                "      \"mid\": 3.0478\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"euro\",\n" +
//                "      \"code\": \"EUR\",\n" +
//                "      \"mid\": 4.3157\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"lew (Bułgaria)\",\n" +
//                "      \"code\": \"BGN\",\n" +
//                "      \"mid\": 2.2066\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"lira turecka\",\n" +
//                "      \"code\": \"TRY\",\n" +
//                "      \"mid\": 0.1187\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"nowy izraelski szekel\",\n" +
//                "      \"code\": \"ILS\",\n" +
//                "      \"mid\": 1.1249\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"peso chilijskie\",\n" +
//                "      \"code\": \"CLP\",\n" +
//                "      \"mid\": 0.00421\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"peso filipińskie\",\n" +
//                "      \"code\": \"PHP\",\n" +
//                "      \"mid\": 0.0696\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"peso meksykańskie\",\n" +
//                "      \"code\": \"MXN\",\n" +
//                "      \"mid\": 0.2002\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"rand (Republika Południowej Afryki)\",\n" +
//                "      \"code\": \"ZAR\",\n" +
//                "      \"mid\": 0.227\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"real (Brazylia)\",\n" +
//                "      \"code\": \"BRL\",\n" +
//                "      \"mid\": 0.7083\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"yuan renminbi (Chiny)\",\n" +
//                "      \"code\": \"CNY\",\n" +
//                "      \"mid\": 0.5662\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"currency\": \"SDR (MFW)\",\n" +
//                "      \"code\": \"XDR\",\n" +
//                "      \"mid\": 5.3877\n" +
//                "    }\n" +
//                "  ]\n" +
//                "}]";
//    }
//
//}