package ecom.pl.ecommerce_shop.catalog;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @Operation(summary = "Get categories", description = "Get all data for categories")
    @GetMapping("/getproducts/{category}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getCatalog(@PathVariable String category) {
        return ResponseEntity.ok(catalogService.displayProductsByCategory(category));
    }
}
