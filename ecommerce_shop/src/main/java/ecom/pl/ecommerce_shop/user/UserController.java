package ecom.pl.ecommerce_shop.user;

import ecom.pl.ecommerce_shop.database.Address;
import ecom.pl.ecommerce_shop.database.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        userService.registerUser(user.getUsername(), user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getRole());
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        Optional<User> user = userService.findUserByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<Address>> getUserAddresses(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserAddresses(userId));
    }

    @PostMapping("/{userId}/addresses")
    public ResponseEntity<String> addUserAddress(@PathVariable Long userId, @RequestBody Address address) {
        userService.addUserAddress(userId, address);
        return ResponseEntity.ok("Address added successfully");
    }
}
