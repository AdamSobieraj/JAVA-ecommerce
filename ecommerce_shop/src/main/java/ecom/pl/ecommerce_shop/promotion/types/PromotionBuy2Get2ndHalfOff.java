package ecom.pl.ecommerce_shop.promotion.types;


import ecom.pl.ecommerce_shop.database.Product;
import ecom.pl.ecommerce_shop.promotion.PromotionMode;
import ecom.pl.ecommerce_shop.promotion.PromotionStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Map;

import static ecom.pl.ecommerce_shop.promotion.PromotionExecutorImp.addPromotionStrategy;

@Component
public class PromotionBuy2Get2ndHalfOff implements PromotionStrategy {

    @PostConstruct
    @Override
    public void register() {
        addPromotionStrategy(PromotionMode.BUY_2_GET_SEC_HALF, this);
    }

    @Override
    public double dataPromotionMap(Map<Product, Integer> contents, double cartPrice) {

        for (Map.Entry<Product, Integer> entry : contents.entrySet()) {
            int quantity = entry.getValue();
            double singlePrice = entry.getKey().getPrice();
            cartPrice -= ((double) quantity / 2) * singlePrice * 0.5;
        }
        return cartPrice;
    }

}
