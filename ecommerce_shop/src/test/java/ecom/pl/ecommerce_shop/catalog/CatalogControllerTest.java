package ecom.pl.ecommerce_shop.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.pl.ecommerce_shop.database.Product;
import ecom.pl.ecommerce_shop.product.ProductDto;
import ecom.pl.ecommerce_shop.product.ProductService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CatalogControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CatalogController catalogController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Product product;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(catalogController).build();

        product = Product.builder()
                .id(UUID.randomUUID())
                .name("test")
                .price(200)
                .available(true)
                .category("product")
                .build();
    }

    @Test
    @SneakyThrows
    void testGetCategories() {
        // Given
        List<String> categories = Arrays.asList("Electronics", "Books");
        when(productService.findAllAvailableCategories()).thenReturn(categories);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/catalog/getcategories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("Electronics").contains("Books");
    }

    @Test
    @SneakyThrows
    void testGetCatalogByCategory() {
        // Given
        String category = "Electronics";

        List<Product> products = Arrays.asList(product);
        when(productService.displayProductsByCategory(category)).thenReturn(products);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/catalog/getproducts/{category}", category)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("[");
    }

    @Test
    @SneakyThrows
    void testGetProducts() {
        // Given
        List<Product> products = Arrays.asList(product);
        when(productService.getProducts()).thenReturn(products);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/catalog/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("[");
    }

    @Test
    @SneakyThrows
    void testGetProductsAlphabetically() {
        // Given
        List<Product> products = Arrays.asList(product);
        when(productService.displayProductsAlphabetically()).thenReturn(products);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/catalog/productsalpha")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("[");
    }

    @Test
    @SneakyThrows
    void testCreateProduct() {
        // Given
        ProductDto productDto = new ProductDto();
        doNothing().when(productService).addProduct(any(ProductDto.class));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/catalog/addproduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @SneakyThrows
    void testCreateProductList() {
        // Given
        List<ProductDto> productList = Arrays.asList(new ProductDto(), new ProductDto());
        doNothing().when(productService).addProducts(anyList());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/catalog/addproductList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productList)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @SneakyThrows
    void testDeleteProduct() {
        // Given
        String productName = "TestProduct";
        doNothing().when(productService).deleteProduct(productName);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/catalog/delete/{product}", productName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
}
