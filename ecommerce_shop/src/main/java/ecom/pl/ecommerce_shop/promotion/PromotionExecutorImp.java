package ecom.pl.ecommerce_shop.promotion;

import ecom.pl.ecommerce_shop.database.Product;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PromotionExecutorImp implements PromotionExecutor {

    private static final Map<PromotionMode, PromotionStrategy> importStrategyMap = new HashMap<>();

    public static void addPromotionStrategy(PromotionMode promotionMode, PromotionStrategy promotionStrategy) {
        importStrategyMap.put(promotionMode, promotionStrategy);
    }

    @Override
    public double processPromotionMap(PromotionMode promotionMode, Map<Product, Integer> contents, double cartPrice) {
        return importStrategyMap.get(promotionMode).dataPromotionMap(contents,cartPrice);
    }
}
