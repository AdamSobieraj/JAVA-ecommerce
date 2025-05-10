package ecom.pl.ecommerce_shop.kafka;

import ecom.pl.ecommerce_shop.database.Product;
import ecom.pl.ecommerce_shop.database.ProductRepository;
import ecom.pl.ecommerce_shop.exception.SaveUnsuccessfulException;
import ecom.pl.ecommerce_shop.product.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MagUpdateService {

    private final ProductRepository productRepository;

    public void updateMag(List<ProductDto> product) {

        product.forEach(productDto -> {

            Product productDb = Product.builder()
                    .name(productDto.getName())
                    .price(productDto.getPrice())
                    .available(productDto.isAvailable())
                    .category(productDto.getCategory())
                    .id(UUID.randomUUID())
                    .build();

            productRepository.save(productDb);

            boolean exists = productRepository.existsById(productDb.getId());

            if (!exists) {
                throw new SaveUnsuccessfulException("Failed to save cart item for product ID: " + productDb.getId());
            }

        });
    }
}
