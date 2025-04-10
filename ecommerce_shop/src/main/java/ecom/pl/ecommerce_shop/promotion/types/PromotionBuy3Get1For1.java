package ecom.pl.ecommerce_shop.promotion.types;


import ecom.pl.ecommerce_shop.database.Product;
import ecom.pl.ecommerce_shop.promotion.PromotionMode;
import ecom.pl.ecommerce_shop.promotion.PromotionStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ecom.pl.ecommerce_shop.promotion.PromotionExecutorImp.addPromotionStrategy;

@Component
public class PromotionBuy3Get1For1 implements PromotionStrategy {

    @PostConstruct
    @Override
    public void register() {
        addPromotionStrategy(PromotionMode.BUY_3_GET_1_FOR_1, this);
    }

    @Override
    public double dataPromotionMap(Map<Product, Integer> contents, double cartPrice) {

        List<Double> prices = new ArrayList<>();
        contents.forEach((product, quantity) -> {
            for (int i = 0; i < quantity; i++) prices.add(product.getPrice());
        });
        Collections.sort(prices);
        for (int i = 2; i < prices.size(); i += 3) {
            cartPrice -= prices.get(i) - 1;
        }
        return cartPrice;
    }

}
