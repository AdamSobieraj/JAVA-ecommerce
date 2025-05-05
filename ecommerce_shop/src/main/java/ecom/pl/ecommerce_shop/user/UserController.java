package ecom.pl.ecommerce_shop.user;

import ecom.pl.ecommerce_shop.database.Address;
import ecom.pl.ecommerce_shop.database.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register user", description = "Add user")
    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        userService.registerUser(user.getUsername(), user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getRole());
        return ResponseEntity.ok("User registered successfully");
    }

    @Operation(summary = "Get user", description = "Get user by name")
    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        Optional<User> user = userService.findUserByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get user addresses", description = "Get user addresses")
    @GetMapping("/{userId}/addresses")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Address>> getUserAddresses(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserAddresses(userId));
    }

    @Operation(summary = "Get user address", description = "Get user address")
    @PostMapping("/{userId}/addresses")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addUserAddress(@PathVariable Long userId, @RequestBody Address address) {
        userService.addUserAddress(userId, address);
        return ResponseEntity.ok("Address added successfully");
    }
}
