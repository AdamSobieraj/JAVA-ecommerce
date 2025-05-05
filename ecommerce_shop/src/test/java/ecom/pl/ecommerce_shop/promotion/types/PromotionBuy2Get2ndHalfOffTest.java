package ecom.pl.ecommerce_shop.promotion.types;

import ecom.pl.ecommerce_shop.database.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.*;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class PromotionBuy2Get2ndHalfOffTest {

    private PromotionBuy2Get2ndHalfOff promotion;

    @BeforeEach
    void setup() {
        promotion = new PromotionBuy2Get2ndHalfOff();
    }

    static Stream<Arguments> provideCartsForDiscountTest() {
        return Stream.of(
                Arguments.of(
                        Map.of(
                                Product.builder().id(UUID.randomUUID()).name("Shirt").price(50.0).available(true).category("Clothing").build(), 2
                        ),
                        100.0,
                        75.0  // one half-off shirt: 100 - 25
                ),
                Arguments.of(
                        Map.of(
                                Product.builder().id(UUID.randomUUID()).name("Shoes").price(80.0).available(true).category("Footwear").build(), 3
                        ),
                        240.0,
                        240.0 - (3.0 / 2 * 80.0 * 0.5)  // 1.5 * 40 = 60 -> 240 - 60 = 180
                ),
                Arguments.of(
                        Map.of(
                                Product.builder().id(UUID.randomUUID()).name("Hat").price(30.0).available(true).category("Accessories").build(), 1
                        ),
                        30.0,
                        30.0 - (1.0 / 2 * 30.0 * 0.5)  // 0.5 * 15 = 7.5 -> 30 - 7.5 = 22.5
                ),
                Arguments.of(
                        Map.of(
                                Product.builder().id(UUID.randomUUID()).name("Socks").price(10.0).available(true).category("Accessories").build(), 4
                        ),
                        40.0,
                        40.0 - (4.0 / 2 * 10.0 * 0.5)  // 2 * 5 = 10 -> 40 - 10 = 30
                ),
                Arguments.of(
                        Map.of(),  // empty cart
                        0.0,
                        0.0
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideCartsForDiscountTest")
    @DisplayName("Should correctly apply Buy 2 Get 2nd Half Off promotion")
    void testDataPromotionMap(Map<Product, Integer> cartContents, double cartPrice, double expectedPrice) {
        double discountedPrice = promotion.dataPromotionMap(cartContents, cartPrice);

        assertEquals(expectedPrice, discountedPrice, 0.001);
    }
}
