package ecom.pl.ecommerce_shop.product;

import lombok.Data;

@Data
public class ProductDto {

    private String name;
    private double price;
    private String category;
    private boolean available;

}
