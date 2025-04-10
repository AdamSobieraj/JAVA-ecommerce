package ecom.pl.ecommerce_shop.promotion;

import ecom.pl.ecommerce_shop.database.Product;

import java.util.Map;

public interface PromotionExecutor {

  double processPromotionMap(PromotionMode promotionMode, Map<Product, Integer> contents, double cartPrice);

}
