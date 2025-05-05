package ecom.pl.ecommerce_shop.product;

import ecom.pl.ecommerce_shop.database.Product;
import ecom.pl.ecommerce_shop.database.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    static Stream<Arguments> provideProductsForAlphabeticalTest() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                Product.builder().id(UUID.randomUUID()).name("Banana").price(1.0).available(true).category("Fruit").build(),
                                Product.builder().id(UUID.randomUUID()).name("Apple").price(2.0).available(true).category("Fruit").build(),
                                Product.builder().id(UUID.randomUUID()).name("Carrot").price(0.5).available(true).category("Vegetable").build()
                        ),
                        List.of("Apple", "Banana", "Carrot")
                ),
                Arguments.of(
                        List.of(
                                Product.builder().id(UUID.randomUUID()).name("Zebra").price(10.0).available(true).category("Animal").build(),
                                Product.builder().id(UUID.randomUUID()).name("Antelope").price(15.0).available(true).category("Animal").build()
                        ),
                        List.of("Antelope", "Zebra")
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideProductsForAlphabeticalTest")
    @DisplayName("Should return products sorted alphabetically by name")
    void testDisplayProductsAlphabetically(List<Product> inputProducts, List<String> expectedNames) {
        when(productRepository.findAll()).thenReturn(inputProducts);

        List<Product> sortedProducts = productService.displayProductsAlphabetically();

        List<String> actualNames = sortedProducts.stream()
                .map(Product::getName)
                .toList();

        assertEquals(expectedNames, actualNames);
    }

    static Stream<Arguments> provideCategoriesForFilterTest() {
        return Stream.of(
                Arguments.of("Fruit", List.of(
                        Product.builder().id(UUID.randomUUID()).name("Banana").price(1.0).available(true).category("Fruit").build(),
                        Product.builder().id(UUID.randomUUID()).name("Apple").price(2.0).available(true).category("Fruit").build()
                )),
                Arguments.of("Vegetable", List.of(
                        Product.builder().id(UUID.randomUUID()).name("Carrot").price(0.5).available(true).category("Vegetable").build()
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("provideCategoriesForFilterTest")
    @DisplayName("Should return available products by category")
    void testDisplayProductsByCategory(String category, List<Product> expectedProducts) {
        when(productRepository.findAvailableProductsByCategoryOrderedByPrice(category))
                .thenReturn(expectedProducts);

        List<Product> result = productService.displayProductsByCategory(category);

        assertEquals(expectedProducts, result);
        verify(productRepository, times(1)).findAvailableProductsByCategoryOrderedByPrice(category);
    }

    @ParameterizedTest
    @ValueSource(strings = {"3fa85f64-5717-4562-b3fc-2c963f66afa6", "123e4567-e89b-12d3-a456-426614174000"})
    @DisplayName("Should delete product by UUID string")
    void testDeleteProduct(String productIdStr) {
        UUID productId = UUID.fromString(productIdStr);

        productService.deleteProduct(productIdStr);

        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    @DisplayName("Should return all products")
    void testGetProducts() {
        List<Product> products = List.of(
                Product.builder().id(UUID.randomUUID()).name("Banana").price(1.0).available(true).category("Fruit").build()
        );
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getProducts();

        assertEquals(products, result);
    }

    @Test
    @DisplayName("Should return all available categories")
    void testFindAllAvailableCategories() {
        List<String> categories = List.of("Fruit", "Vegetable");
        when(productRepository.findAllAvailableCategories()).thenReturn(categories);

        List<String> result = productService.findAllAvailableCategories();

        assertEquals(categories, result);
    }
}
