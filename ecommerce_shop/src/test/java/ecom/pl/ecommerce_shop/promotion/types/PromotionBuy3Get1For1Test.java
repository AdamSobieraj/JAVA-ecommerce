package ecom.pl.ecommerce_shop.promotion.types;

import ecom.pl.ecommerce_shop.database.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.*;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class PromotionBuy3Get1For1Test {

    private PromotionBuy3Get1For1 promotion;

    @BeforeEach
    void setup() {
        promotion = new PromotionBuy3Get1For1();
    }

    static Stream<Arguments> provideCartsForDiscountTest() {
        return Stream.of(
                Arguments.of(
                        Map.of(
                                Product.builder().id(UUID.randomUUID()).name("T-shirt").price(20.0).available(true).category("Clothing").build(), 3
                        ),
                        60.0,
                        60.0 - (20.0 - 1)  // one third item discounted to 1: 60 - 19 = 41
                ),
                Arguments.of(
                        Map.of(
                                Product.builder().id(UUID.randomUUID()).name("Shoes").price(50.0).available(true).category("Footwear").build(), 4
                        ),
                        200.0,
                        200.0 - (50.0 - 1)  // one third item discounted to 1: 200 - 49 = 151
                ),
                Arguments.of(
                        Map.of(
                                Product.builder().id(UUID.randomUUID()).name("Hat").price(30.0).available(true).category("Accessories").build(), 2
                        ),
                        60.0,
                        60.0  // no third item, no discount
                ),
                Arguments.of(
                        Map.of(
                                Product.builder().id(UUID.randomUUID()).name("Bag").price(100.0).available(true).category("Accessories").build(), 6
                        ),
                        600.0,
                        600.0 - (100.0 - 1) - (100.0 - 1)  // two discounted: 600 - 99 - 99 = 402
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
    @DisplayName("Should correctly apply Buy 3 Get 1 For 1 promotion")
    void testDataPromotionMap(Map<Product, Integer> cartContents, double cartPrice, double expectedPrice) {
        double discountedPrice = promotion.dataPromotionMap(cartContents, cartPrice);

        assertEquals(expectedPrice, discountedPrice, 0.001);
    }
}
