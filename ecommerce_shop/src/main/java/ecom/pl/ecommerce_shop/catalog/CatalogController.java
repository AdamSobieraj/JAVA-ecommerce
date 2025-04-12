package ecom.pl.ecommerce_shop.catalog;

import ecom.pl.ecommerce_shop.product.ProductDto;
import ecom.pl.ecommerce_shop.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final ProductService productService;

    @Operation(summary = "Get all available categories", description = "Get all data for categories")
    @GetMapping("/getcategories")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getCataegories() {
        return ResponseEntity.ok(productService.findAllAvailableCategories());
    }

    @Operation(summary = "Get categories", description = "Get all data for categories")
    @GetMapping("/getproducts/{category}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getCatalog(@PathVariable String category) {
        return ResponseEntity.ok(productService.displayProductsByCategory(category));
    }

    @Operation(summary = "Get all available products", description = "Get all products")
    @GetMapping("/products")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @Operation(summary = "Get all available products alphabetically", description = "Get all products alphabetically")
    @GetMapping("/productsalpha")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getProductsAlphabetically() {
        return ResponseEntity.ok(productService.displayProductsAlphabetically());
    }

    @Operation(summary = "Add products", description = "Add products")
    @PostMapping("/addproduct")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        productService.addProduct(productDto);
        return ResponseEntity.ok(new ProductDto());
    }

    @Operation(summary = "Add list products", description = "Add list of products")
    @PostMapping("/addproductList")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ProductDto> createProductList(@RequestBody List<ProductDto> productDto) {
        productService.addProducts(productDto);
        return ResponseEntity.ok(new ProductDto());
    }

    @Operation(summary = "Remove product", description = "Remove product")
    @DeleteMapping("/delete/{product}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable String product) {
        productService.deleteProduct(product);
        return ResponseEntity.ok(new ProductDto());
    }
}
