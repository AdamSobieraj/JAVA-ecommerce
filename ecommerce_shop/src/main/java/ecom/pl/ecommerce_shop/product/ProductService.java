package ecom.pl.ecommerce_shop.product;

import ecom.pl.ecommerce_shop.database.Product;
import ecom.pl.ecommerce_shop.database.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public List<Product> displayProductsAlphabetically() {
        return productRepository.findAll().stream()
                .sorted(Comparator.comparing(Product::getName))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void addProduct(ProductDto productDto) {

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .available(productDto.isAvailable())
                .category(productDto.getCategory())
                .build();

        productRepository.save(product);
    }

    public void addProducts(List<ProductDto> productDtos) {

        List<Product> productsSorted = productDtos.stream()
                .map(productDto -> Product.builder()
                       .id(UUID.randomUUID())
                       .name(productDto.getName())
                       .price(productDto.getPrice())
                       .available(productDto.isAvailable())
                       .category(productDto.getCategory())
                       .build())
                .toList();

        productRepository.saveAll(productsSorted);
    }

    public void deleteProduct(String product) {
        productRepository.deleteById(UUID.fromString(product));
    }

    public List<Product> displayProductsByCategory(String category) {

        return productRepository.findAvailableProductsByCategoryOrderedByPrice(category);
    }

    public List<String> findAllAvailableCategories() {
        return productRepository.findAllAvailableCategories();
    }
}
