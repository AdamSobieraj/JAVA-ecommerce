package ecom.pl.ecommerce_shop.cart;

import ecom.pl.ecommerce_shop.database.*;
import ecom.pl.ecommerce_shop.exchange.*;
import ecom.pl.ecommerce_shop.exchange.Currency;
import ecom.pl.ecommerce_shop.promotion.PromotionExecutorImp;
import ecom.pl.ecommerce_shop.promotion.PromotionMode;
import ecom.pl.ecommerce_shop.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final PromotionExecutorImp promotionExecutorImp;
    private final PromotionCodeRepository promotionCodeRepository;
    private final CurrencyExchangeService currencyExchangeService;

    @Setter
    @Value("${promotion.level}")
    private Integer promotionLevel;


    public void addProduct(CartItem cartItem) {

        Cart cart = Cart.builder()
                .uuid(UUID.randomUUID())
                .productId(UUID.fromString(cartItem.getProductId()))
                .quantity(Integer.valueOf(cartItem.getQuantity()))
                .userId(getUserId())
                .build();

        cartRepository.save(cart);
    }

    public void removeProduct(String cartItemId) {

        Optional<Cart> cartItem = cartRepository.findById(UUID.fromString(cartItemId));
        cartRepository.delete(cartItem.get());

    }

    public CartOrder displayCart(String code) {

        Map<Product, Integer> orderMap = new HashMap<>();

        List<Cart> cart =
                cartRepository.findAllByUserId(getUserId()).get();

        for (Cart item : cart) {
            Product product = productRepository.findById(item.getProductId());
            orderMap.put(product, item.getQuantity());
        }

        Double totalPrice = getTotalPrice(orderMap, code);

        ExchangeRequest exchangeRequest = ExchangeRequest.builder()
                .amount(BigDecimal.valueOf(totalPrice))
                .currency(Currency.USD)
                .build();

        ExchangeResult totalPriceUSD = currencyExchangeService.exchange(exchangeRequest);

        return CartOrder.builder()
                .orderMap(orderMap)
                .totalPrice(totalPrice)
                .totalPriceUSD(totalPriceUSD.getAmount().doubleValue())
                .build();
    }

    public UUID getUserId() {
        String userName = userService.getUserFromSecurityContext();
        Optional<User> user = userService.findUserByUsername(userName);
        return user.get().getId();
    }

    public Double getTotalPrice(Map<Product, Integer> orderMap, String code) {
        Integer productAmount = 0;
        Double totalPrice = 0.0;

        for (Map.Entry<Product, Integer> entry : orderMap.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
            productAmount += entry.getValue();
        }

        Optional<PromotionCode> promotionCode = promotionCodeRepository.findById(UUID.fromString(code));

        boolean activePromotion = promotionCode.isPresent() && promotionCode.get().getActive();

        if (activePromotion && totalPrice > promotionLevel) {
            totalPrice = promotionExecutorImp.processPromotionMap(PromotionMode.GET_10_PERCENT_OFF, orderMap, totalPrice);
        }
        else {
            totalPrice = totalPrice - (totalPrice * promotionCode.get().getDiscountPercentage()/100);
        }

        if (productAmount % 2 == 0) {
            totalPrice = promotionExecutorImp.processPromotionMap(PromotionMode.BUY_2_GET_SEC_HALF, orderMap, productAmount);
        } else {
            totalPrice = promotionExecutorImp.processPromotionMap(PromotionMode.BUY_3_GET_1_FOR_1, orderMap, productAmount);
        }

        return totalPrice;
    }

}
