package ecom.pl.ecommerce_shop.promotion.types;


import ecom.pl.ecommerce_shop.database.Product;
import ecom.pl.ecommerce_shop.promotion.PromotionMode;
import ecom.pl.ecommerce_shop.promotion.PromotionStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Map;

import static ecom.pl.ecommerce_shop.promotion.PromotionExecutorImp.addPromotionStrategy;

@Component
public class PromotionTenPercentOff implements PromotionStrategy {

    @PostConstruct
    @Override
    public void register() {
        addPromotionStrategy(PromotionMode.GET_10_PERCENT_OFF, this);
    }

    @Override
    public double dataPromotionMap(Map<Product, Integer> contents, double cartPrice) {
        return cartPrice * 0.9;
    }

}
