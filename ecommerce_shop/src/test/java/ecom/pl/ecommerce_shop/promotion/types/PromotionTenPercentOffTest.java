package ecom.pl.ecommerce_shop.promotion.types;

import ecom.pl.ecommerce_shop.database.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.*;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class PromotionTenPercentOffTest {

    private PromotionTenPercentOff promotion;

    @BeforeEach
    void setup() {
        promotion = new PromotionTenPercentOff();
    }

    static Stream<Arguments> provideCartsForDiscountTest() {
        return Stream.of(
                Arguments.of(
                        Map.of(
                                Product.builder().id(UUID.randomUUID()).name("Item A").price(100.0).available(true).category("General").build(), 1
                        ),
                        100.0,
                        90.0  // 100 * 0.9
                ),
                Arguments.of(
                        Map.of(
                                Product.builder().id(UUID.randomUUID()).name("Item B").price(50.0).available(true).category("General").build(), 2
                        ),
                        100.0,
                        90.0  // 100 * 0.9
                ),
                Arguments.of(
                        Map.of(
                                Product.builder().id(UUID.randomUUID()).name("Item C").price(30.0).available(true).category("General").build(), 3
                        ),
                        90.0,
                        81.0  // 90 * 0.9
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
    @DisplayName("Should correctly apply 10% off promotion to cart price")
    void testDataPromotionMap(Map<Product, Integer> cartContents, double cartPrice, double expectedPrice) {
        double discountedPrice = promotion.dataPromotionMap(cartContents, cartPrice);

        assertEquals(expectedPrice, discountedPrice, 0.001);
    }
}
