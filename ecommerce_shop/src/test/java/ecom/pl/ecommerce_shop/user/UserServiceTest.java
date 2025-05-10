package ecom.pl.ecommerce_shop.user;

import ecom.pl.ecommerce_shop.database.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

class UserServiceTest {

    private static final String UUID_TEST = "660e8400-e29b-41d4-a716-556655440037";

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @CsvSource({
            "alice, password123, alice@example.com, 123456789, USER",
            "bob, pass456, bob@example.com, 987654321, ADMIN"
    })
    @DisplayName("Should register user with encoded password and correct details")
    void testRegisterUser(String username, String rawPassword, String email, String phone, String role) {
        Mockito.when(passwordEncoder.encode(rawPassword)).thenReturn("encoded_" + rawPassword);
        Mockito.when(userRepository.existsById(any(UUID.class))).thenReturn(Boolean.TRUE);

        userService.registerUser(username, rawPassword, email, phone, role);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(username, savedUser.getUsername());
        assertEquals("encoded_" + rawPassword, savedUser.getPassword());
        assertEquals(email, savedUser.getEmail());
        assertEquals(phone, savedUser.getPhoneNumber());
        assertEquals(role, savedUser.getRole());
        assertTrue(savedUser.isEnabled());
        assertNotNull(savedUser.getId());
        assertTrue(savedUser.getAddresses().isEmpty());
    }

    @Test
    @DisplayName("Should find user by username")
    void testFindUserByUsername() {
        String username = "charlie";
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .password("pass")
                .email("charlie@example.com")
                .phoneNumber("111222333")
                .role("USER")
                .enabled(true)
                .addresses(List.of())
                .build();

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    @DisplayName("Should get user addresses by userId")
    void testGetUserAddresses() {
        UUID userId = UUID.randomUUID();
        List<Address> addresses = List.of(
                new Address(UUID.randomUUID(), "Street 1", "City", "State", "Zip", "Country", null)
        );
        Mockito.when(addressRepository.findByUserId(userId)).thenReturn(addresses);

        List<Address> result = userService.getUserAddresses(userId);

        assertEquals(addresses, result);
    }

    @Test
    @DisplayName("Should add address to user")
    void testAddUserAddress() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .username("david")
                .password("pass")
                .email("david@example.com")
                .phoneNumber("444555666")
                .role("USER")
                .enabled(true)
                .addresses(new ArrayList<>())
                .build();

        Address address = new Address(UUID.randomUUID(), "Main St", "Town", "State", "00000", "Country", null);

        Mockito.when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        Mockito.when(addressRepository.existsById(any(UUID.class))).thenReturn(Boolean.TRUE);

        userService.addUserAddress(UUID_TEST, address);

        assertEquals(user, address.getUser());
        Mockito.verify(addressRepository).save(address);
    }

    @Test
    @DisplayName("Should get username from SecurityContext")
    void testGetUserFromSecurityContext() {
        Authentication auth = Mockito.mock(Authentication.class);
        Mockito.when(auth.getName()).thenReturn("securedUser");

        SecurityContextHolder.getContext().setAuthentication(auth);

        String username = userService.getUserFromSecurityContext();

        assertEquals("securedUser", username);
    }
}
