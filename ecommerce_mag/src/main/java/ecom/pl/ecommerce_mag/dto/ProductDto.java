package ecom.pl.ecommerce_mag.dto;

import lombok.Data;

@Data
public class ProductDto {

    private String name;
    private double price;
    private String category;
    private boolean available;

}
