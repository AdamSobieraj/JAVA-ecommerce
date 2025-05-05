package ecom.pl.ecommerce_shop.cart;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Koszyk", description = "Koszyk")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Get user cart", description = "Get all user cart products")
    @GetMapping("/getcart/{code}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getCart(@PathVariable String code) {
         CartOrder cartOrder = cartService.displayCart(code);
        return ResponseEntity.ok(cartOrder);
    }

    @Operation(summary = "Add product to cart", description = "Adding product to cart")
    @PutMapping("/addtocart")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addToCart(@RequestBody CartItem cart) {
        cartService.addProduct(cart);
        return ResponseEntity.ok(cart);
    }

    @Operation(summary = "Remove product from cart", description = "Removing product from cart")
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> removeFromCart(@RequestBody String cartItemId) {
        cartService.removeProduct(cartItemId);
        return ResponseEntity.ok().build();
    }
}


// exceptiony controllera
// zmienić exchamge update