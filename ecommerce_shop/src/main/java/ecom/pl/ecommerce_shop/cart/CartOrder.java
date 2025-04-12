package ecom.pl.ecommerce_shop.cart;

import ecom.pl.ecommerce_shop.database.Product;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class CartOrder {
    Map<Product, Integer> orderMap;
    Double totalPrice;
    Double totalPriceUSD;
}
