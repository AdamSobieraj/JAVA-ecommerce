package ecom.pl.ecommerce_shop.catalog;

import ecom.pl.ecommerce_shop.database.Product;
import ecom.pl.ecommerce_shop.database.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final ProductRepository productRepository;

    public List<Product> displayProductsByCategory(String category) {

       return productRepository.findAllByCategory(category);
    }

}
