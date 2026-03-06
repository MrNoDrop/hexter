package be.hexter.hexter;

import be.hexter.hexter.model.User;
import be.hexter.hexter.repositoryDAO.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testRegistrationEndToEnd() throws Exception {
        String jsonRequest = """
                {
                    "email": "integration@example.com",
                    "nick_name": "integrationuser",
                    "password": "integrationpass123"
                }
                """;

        mockMvc.perform(post("/api/user/register")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk());

        Optional<User> savedUser = userRepository.findByEmail("integration@example.com");
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getNickname()).isEqualTo("integrationuser");
    }

    @Test
    void testLoginAfterRegistration() throws Exception {
        String registerRequest = """
                {
                    "email": "login@example.com",
                    "nick_name": "loginuser",
                    "password": "loginpass123"
                }
                """;

        mockMvc.perform(post("/api/user/register")
                .contentType("application/json")
                .content(registerRequest))
                .andExpect(status().isOk());

        String loginRequest = """
                {
                    "email": "login@example.com",
                    "password": "loginpass123"
                }
                """;

        mockMvc.perform(post("/api/user/login")
                .contentType("application/json")
                .content(loginRequest))
                .andExpect(status().isOk());
    }

    @Test
    void testApplicationContextLoads() {
        assertThat(userRepository).isNotNull();
    }

    @Test
    void testValidateTokenEndpoint() throws Exception {
        String jsonRequest = """
                {
                    "fingerprint": "test_fingerprint",
                    "authenticationToken": "test_token"
                }
                """;

        mockMvc.perform(post("/api/user/validate-authentication-token")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isOk());
    }
}
