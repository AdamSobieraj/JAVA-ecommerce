package ecom.pl.ecommerce_shop.promotion;

import ecom.pl.ecommerce_shop.database.Product;

import java.util.Map;

public interface PromotionStrategy {

  void register();

  double dataPromotionMap(Map<Product, Integer> contents, double cartPrice);
}
