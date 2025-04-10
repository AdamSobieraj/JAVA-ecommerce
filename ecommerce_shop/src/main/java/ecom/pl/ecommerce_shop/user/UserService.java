package ecom.pl.ecommerce_shop.user;

import ecom.pl.ecommerce_shop.database.Address;
import ecom.pl.ecommerce_shop.database.AddressRepository;
import ecom.pl.ecommerce_shop.database.User;
import ecom.pl.ecommerce_shop.database.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(String username, String rawPassword, String email, String phoneNumber, String role) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(UUID.randomUUID(), username, encodedPassword, email, phoneNumber, role, true, List.of());
        userRepository.save(user);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<Address> getUserAddresses(UUID userId) {
        return addressRepository.findByUserId(userId);
    }

    public void addUserAddress(Long userId, Address address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        address.setUser(user);
        addressRepository.save(address);
    }

    public String getUserFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
