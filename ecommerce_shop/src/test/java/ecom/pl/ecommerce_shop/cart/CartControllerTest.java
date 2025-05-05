package ecom.pl.ecommerce_shop.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.pl.ecommerce_shop.database.Product;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    private static final String ID = "660e8400-e29b-41d4-a716-556655440037";

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private CartItem testCartItem;
    private CartOrder testCartOrder;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

        testCartItem = new CartItem();
        testCartItem.setProductId(ID);
        testCartItem.setQuantity("2");

        Product testProduct = new Product();
        testProduct.setId(UUID.fromString(ID));
        testProduct.setName("Test Product");

        Map<Product, Integer> orderMap = new HashMap<>();
        orderMap.put(testProduct, 2);

        testCartOrder = CartOrder.builder()
                .orderMap(orderMap)
                .totalPrice(200.0)
                .totalPriceUSD(50.0)
                .build();
    }

    @Test
    @SneakyThrows
    void testGetCart() {
        // Given
        when(cartService.displayCart("cart123")).thenReturn(testCartOrder);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/getcart/cart123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String responseContent = result.getResponse().getContentAsString();

        assertThat(responseContent).contains("\"totalPrice\":200.0");
        assertThat(responseContent).contains("\"totalPriceUSD\":50.0");
    }


    @Test
    @SneakyThrows
    void testAddToCart() {
        // Given
        doNothing().when(cartService).addProduct(any(CartItem.class));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/cart/addtocart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCartItem)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @SneakyThrows
    void testRemoveFromCart() {
        // Given
        doNothing().when(cartService).removeProduct(any(String.class));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("item123")))
                .andExpect(status().isOk())
                .andReturn();
    }
}
