package ecom.pl.ecommerce_shop.cart;

import ecom.pl.ecommerce_shop.database.*;
import ecom.pl.ecommerce_shop.exception.EntityNotFoundInDatabaseException;
import ecom.pl.ecommerce_shop.exception.SaveUnsuccessfulException;
import ecom.pl.ecommerce_shop.exchange.Currency;
import ecom.pl.ecommerce_shop.exchange.CurrencyExchangeService;
import ecom.pl.ecommerce_shop.exchange.ExchangeRequest;
import ecom.pl.ecommerce_shop.exchange.ExchangeResult;
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

        boolean exists = cartRepository.existsById(cart.getUuid());

        if (!exists) {
            throw new SaveUnsuccessfulException("Failed to save cart item for product ID: " + cartItem.getProductId());
        }
    }

    public void removeProduct(String cartItemId) {

        Cart cartItem = cartRepository.findById(UUID.fromString(cartItemId))
                .orElseThrow(() -> new EntityNotFoundInDatabaseException("Cart item with ID " + cartItemId + " not found"));

        cartRepository.delete(cartItem);

    }

    public CartOrder displayCart(String code) {

        Map<Product, Integer> orderMap = new HashMap<>();

        List<Cart> cart =
                cartRepository.findAllByUserId(getUserId())
                        .orElseThrow(() -> new EntityNotFoundInDatabaseException("No cart found for user with ID " + getUserId()));

        for (Cart item : cart) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new EntityNotFoundInDatabaseException("No cart found for user with ID " + getUserId()));
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
        int productAmount = 0;
        double totalPrice = 0.0;

        for (Map.Entry<Product, Integer> entry : orderMap.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
            productAmount += entry.getValue();
        }

        Optional<PromotionCode> promotionCode = promotionCodeRepository.findById(UUID.fromString(code));
        boolean activePromotion = promotionCode.isPresent() && promotionCode.get().getActive();

        totalPrice = (activePromotion && totalPrice > promotionLevel)?
                promotionExecutorImp.processPromotionMap(PromotionMode.GET_10_PERCENT_OFF, orderMap, totalPrice) :
                totalPrice - (totalPrice * promotionCode.get().getDiscountPercentage()/100);

        totalPrice = (productAmount % 2 == 0)?
                promotionExecutorImp.processPromotionMap(PromotionMode.BUY_2_GET_SEC_HALF, orderMap, productAmount) :
                promotionExecutorImp.processPromotionMap(PromotionMode.BUY_3_GET_1_FOR_1, orderMap, productAmount);

        return totalPrice;
    }

}
