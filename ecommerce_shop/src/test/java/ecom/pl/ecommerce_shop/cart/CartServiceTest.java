package ecom.pl.ecommerce_shop.cart;


import ecom.pl.ecommerce_shop.database.*;
import ecom.pl.ecommerce_shop.promotion.PromotionExecutorImp;
import ecom.pl.ecommerce_shop.promotion.PromotionMode;
import ecom.pl.ecommerce_shop.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private PromotionExecutorImp promotionExecutorImp;

    @Mock
    private PromotionCodeRepository promotionCodeRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService.setPromotionLevel(2);  // Manually setting the promotion level
    }

    @ParameterizedTest
    @CsvSource({
            "'f47ac10b-58cc-4372-a567-0e02b2c3d479', 150.0, 2, 135.0",   // 10% discount as price is over 100
            "'c9b3f7b0-66b1-469f-9a67-0bbedfb4b745', 50.0, 4, 50.0",     // No discount as price is under 100
            "'758e93b1-b413-45f7-b4d4-b65d6398b23a', 200.0, 3, 180.0",  // 10% discount as price is over 100
            "'3428d53f-92e7-400e-8db7-f4b5b40e7c27', 25.0, 6, 20.0",   // Apply BUY_2_GET_SEC_HALF promotion
            "'29e3b2c4-e6fd-49b4-bd65-3b7de6be8992', 60.0, 5, 48.0"    // Apply BUY_3_GET_1_FOR_1 promotion
    })
    void testGetTotalPrice(String code, double expectedTotalPrice, int productAmount, double expectedPrice) {
        // Given
        Map<Product, Integer> orderMap = new HashMap<>();
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .price(30.0)
                .available(true)
                .name("TestProduct")
                .build();

        orderMap.put(product, productAmount);

        UUID promotionCodeUUID = UUID.fromString(code);
        PromotionCode promotionCode = PromotionCode.builder()
                .code(code)
                .active(true)
                .discountPercentage(10.0)
                .expirationDate(LocalDate.now().plusDays(1))
                .build();

        when(promotionCodeRepository.findById(promotionCodeUUID)).thenReturn(Optional.of(promotionCode));
        // Lenient stubbing: Allow mismatches in stubbing arguments
        lenient().when(promotionExecutorImp.processPromotionMap(PromotionMode.GET_10_PERCENT_OFF, orderMap, productAmount))
                .thenReturn(expectedTotalPrice);
        lenient().when(promotionExecutorImp.processPromotionMap(PromotionMode.BUY_2_GET_SEC_HALF, orderMap, productAmount))
                .thenReturn(expectedPrice);
        lenient().when(promotionExecutorImp.processPromotionMap(PromotionMode.BUY_3_GET_1_FOR_1, orderMap, productAmount))
                .thenReturn(expectedPrice);

        // When
        double totalPrice = cartService.getTotalPrice(orderMap, code);

        // Then
        assertEquals(expectedPrice, totalPrice);
    }

    @ParameterizedTest
    @CsvSource({
            "'f47ac10b-58cc-4372-a567-0e02b2c3d479', 54.0, 2", // Using a 10% discount for 2 products
            "'c9b3f7b0-66b1-469f-9a67-0bbedfb4b745', 50.0, 4"  // No discount for 4 products under 100
    })
    void testDisplayCart(String code, double expectedTotalPrice, int productAmount) {
        // Given
        UUID promotionCodeUUID = UUID.fromString(code);
        PromotionCode promotionCode = PromotionCode.builder()
                .code(code)
                .active(true)
                .discountPercentage(10.0)
                .expirationDate(LocalDate.now().plusDays(1))
                .build();

        Cart cartItem = Cart.builder()
                .productId(UUID.randomUUID())
                .quantity(productAmount)
                .build();

        List<Cart> cartList = Collections.singletonList(cartItem);

        Product product = Product.builder()
                .id(cartItem.getProductId())
                .price(30.0)
                .available(true)
                .name("TestProduct")
                .build();

        Map<Product, Integer> orderMap = new HashMap<>();
        orderMap.put(product, productAmount);

        User user = User.builder()
                .id(UUID.randomUUID())
                .role("ROLE_USER")
                .username("testuser")
                .password("password")
                .build();

        when(userService.findUserByUsername(any())).thenReturn(Optional.of(user));
        when(userService.getUserFromSecurityContext()).thenReturn(String.valueOf(user));
        when(cartRepository.findAllByUserId(any(UUID.class))).thenReturn(Optional.of(cartList));
        when(productRepository.findById(cartItem.getProductId())).thenReturn(product);
        when(promotionCodeRepository.findById(promotionCodeUUID)).thenReturn(Optional.of(promotionCode));
        lenient().when(promotionExecutorImp.processPromotionMap(PromotionMode.GET_10_PERCENT_OFF, orderMap, productAmount))
                .thenReturn(expectedTotalPrice);
        lenient().when(promotionExecutorImp.processPromotionMap(PromotionMode.BUY_2_GET_SEC_HALF, orderMap, productAmount))
                .thenReturn(expectedTotalPrice);
        lenient().when(promotionExecutorImp.processPromotionMap(PromotionMode.BUY_3_GET_1_FOR_1, orderMap, productAmount))
                .thenReturn(expectedTotalPrice);

        // When
        CartOrder cartOrder = cartService.displayCart(code);

        // Then
        assertNotNull(cartOrder);
        assertEquals(expectedTotalPrice, cartOrder.getTotalPrice());
        assertTrue(cartOrder.getOrderMap().containsKey(product));
        assertEquals(productAmount, cartOrder.getOrderMap().get(product));
    }


}