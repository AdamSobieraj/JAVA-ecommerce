//package ecom.pl.ecommerce.exchange;
//import ecom.pl.ecommerce_shop.exchange.ExchangeRate;
//import ecom.pl.ecommerce_shop.exchange.ExchangeRateService;
//import ecom.pl.ecommerce_shop.exchange.ExchangeRateTable;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Value;
//
//import static org.mockito.Mockito.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.util.List;
//
//@ExtendWith(MockitoExtension.class)
//class ExchangeRateServiceTest {
//
//    @InjectMocks
//    private ExchangeRateService exchangeRateService;
//
//    @Mock
//    private HttpURLConnection mockConnection;
//
//    @Mock
//    private ExchangeRateTable mockExchangeRateTable;
//
//    @Mock
//    private BufferedReader mockBufferedReader;
//
//    @BeforeEach
//    void setUp() {
//        // Mocking any initialization required for the service
//        exchangeRateService = new ExchangeRateService();
//        exchangeRateService.setNbpUrl("http://example.com/api/rates");
//    }
//
//    @Test
//    void testFetchExchangeRateResponse_Success() throws IOException {
//        // Setup
//        String expectedResponse = "{\"rates\":[{\"code\":\"USD\",\"mid\":4.45},{\"code\":\"EUR\",\"mid\":4.91}]}";
//
//        // Mocking the HTTP connection behavior
//        when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(expectedResponse.getBytes()));
//
//        // Test the fetch method directly
//        String response = exchangeRateService.fetchExchangeRateResponse();
//        assertNotNull(response);
//        assertEquals(expectedResponse, response);
//    }
//
//    @Test
//    void testFetchExchangeRateResponse_Failure() throws IOException {
//        // Setup failure scenario
//        when(mockConnection.getInputStream()).thenThrow(new IOException("Failed to connect"));
//
//        // Test for failure
//        String response = exchangeRateService.fetchExchangeRateResponse();
//        assertNull(response);
//    }
//
//    @Test
//    void testRemoveJsonArrayBrackets() {
//        // Test with different inputs
//        assertEquals("test", exchangeRateService.removeJsonArrayBrackets("[test]"));
//        assertEquals("test", exchangeRateService.removeJsonArrayBrackets("test"));
//    }
//
//    @Test
//    void testProcessResponse_InvalidJson() {
//        // Test invalid JSON response
//        String invalidResponse = "[invalid JSON]";
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            exchangeRateService.processResponse(invalidResponse);
//        });
//
//        assertTrue(exception.getMessage().contains("Failed to process response"));
//    }
//}
