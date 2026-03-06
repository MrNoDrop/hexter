package be.hexter.hexter.controller;

import be.hexter.hexter.model.AuthenticationToken;
import be.hexter.hexter.model.CredentialRecovery;
import be.hexter.hexter.model.User;
import be.hexter.hexter.service.AuthenticationTokenService;
import be.hexter.hexter.service.RecoveryService;
import be.hexter.hexter.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RecoveryService recoveryService;

    @MockBean
    private AuthenticationTokenService authenticationTokenService;

    private User testUser;
    private AuthenticationToken testToken;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setNickname("testuser");
        testUser.setHash(UUID.randomUUID().toString());

        testToken = new AuthenticationToken();
        testToken.setId(1L);
        testToken.setToken(UUID.randomUUID().toString());
        testToken.setUser(testUser);
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        when(userService.registerUser(anyString(), anyString(), anyString())).thenReturn(testUser);

        String jsonRequest = """
                {
                    "email": "test@example.com",
                    "nick_name": "testuser",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/user/register")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginSuccess() throws Exception {
        when(userService.authenticateUser(anyString(), anyString())).thenReturn(testToken);

        String jsonRequest = """
                {
                    "email": "test@example.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/user/login")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void testValidateAuthenticationTokenSuccess() throws Exception {
        when(authenticationTokenService.validateAuthenticationToken(anyString())).thenReturn(true);

        String jsonRequest = """
                {
                    "fingerprint": "test_fingerprint",
                    "authenticationToken": "valid_token_123"
                }
                """;

        mockMvc.perform(post("/api/user/validate-authentication-token")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void testValidateAuthenticationTokenFailure() throws Exception {
        when(authenticationTokenService.validateAuthenticationToken(anyString())).thenReturn(false);

        String jsonRequest = """
                {
                    "fingerprint": "test_fingerprint",
                    "authenticationToken": "invalid_token"
                }
                """;

        mockMvc.perform(post("/api/user/validate-authentication-token")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void testRequestRecoverPassword() throws Exception {
        CredentialRecovery recovery = new CredentialRecovery();
        recovery.setId(1L);
        recovery.setToken("recovery_token_123");
        recovery.setUser(testUser);

        when(userService.findByHash(anyString())).thenReturn(testUser);

        String jsonRequest = """
                {
                    "hash": "user_hash_123"
                }
                """;

        mockMvc.perform(post("/api/user/request-recover-password")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void testRecoverPassword() throws Exception {
        CredentialRecovery recovery = new CredentialRecovery();
        recovery.setId(1L);
        recovery.setToken("recovery_token_123");
        recovery.setUser(testUser);

        when(userService.findByRecoveryToken(anyString())).thenReturn(recovery);

        String jsonRequest = """
                {
                    "recoveryToken": "recovery_token_123",
                    "password": "newpassword123"
                }
                """;

        mockMvc.perform(post("/api/user/recover-password")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterUserWithEmptyEmail() throws Exception {
        when(userService.registerUser("", "testuser", "password123")).thenReturn(null);

        String jsonRequest = """
                {
                    "email": "",
                    "nick_name": "testuser",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/user/register")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk());
    }
}
