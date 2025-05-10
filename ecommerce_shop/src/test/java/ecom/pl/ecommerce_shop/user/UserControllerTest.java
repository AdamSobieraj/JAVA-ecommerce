package ecom.pl.ecommerce_shop.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.pl.ecommerce_shop.database.Address;
import ecom.pl.ecommerce_shop.database.User;
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

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User testUser;
    private Address testAddress;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        UUID userId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();

        testUser = User.builder()
                .id(userId)
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .phoneNumber("123456789")
                .role("ROLE_USER")
                .enabled(true)
                .addresses(Collections.emptyList())
                .build();

        testAddress = new Address();
        testAddress.setId(addressId);
        testAddress.setStreet("Test Street");
        testAddress.setCity("Test City");
        testAddress.setState("Test State");
        testAddress.setZipCode("12345");
        testAddress.setCountry("Test Country");
        testAddress.setUser(testUser);
    }

    @Test
    void testRegisterUser() throws Exception {
        // Given
        doNothing().when(userService).registerUser(any(), any(), any(), any(), any());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUser() throws Exception {
        // Given
        when(userService.findUserByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/testuser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).contains("\"username\":\"testuser\"");
        assertThat(responseContent).contains("\"email\":\"test@example.com\"");
    }

    @Test
    void testGetUserAddresses() throws Exception {
        // Given
        when(userService.getUserAddresses(testUser.getId()))
                .thenReturn(Collections.singletonList(testAddress));

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + testUser.getId() + "/addresses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).contains("\"city\":\"Test City\"");
        assertThat(responseContent).contains("\"country\":\"Test Country\"");
    }

    @Test
    void testAddUserAddress() throws Exception {
        // Given
        doNothing().when(userService).addUserAddress(any(String.class), any(Address.class));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAddress)))
                .andExpect(status().isOk());
    }
}
