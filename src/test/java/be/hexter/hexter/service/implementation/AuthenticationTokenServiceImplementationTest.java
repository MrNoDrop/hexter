package be.hexter.hexter.service.implementation;

import be.hexter.hexter.model.AuthenticationToken;
import be.hexter.hexter.model.User;
import be.hexter.hexter.repositoryDAO.AuthenticationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationTokenServiceImplementationTest {

    @Mock
    private AuthenticationTokenRepository authenticationTokenRepository;

    @InjectMocks
    private AuthenticationTokenServiceImplementation authenticationTokenService;

    private AuthenticationToken testToken;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        testToken = new AuthenticationToken();
        testToken.setId(1L);
        testToken.setToken(UUID.randomUUID().toString());
        testToken.setUser(testUser);
    }

    @Test
    void testValidateAuthenticationTokenSuccess() {
        List<AuthenticationToken> tokens = Collections.singletonList(testToken);
        when(authenticationTokenRepository.findAll()).thenReturn(tokens);

        Boolean result = authenticationTokenService.validateAuthenticationToken(testToken.getToken());

        assertThat(result).isTrue();
        verify(authenticationTokenRepository).findAll();
    }

    @Test
    void testValidateAuthenticationTokenNotFound() {
        when(authenticationTokenRepository.findAll()).thenReturn(Collections.emptyList());

        Boolean result = authenticationTokenService.validateAuthenticationToken("invalid_token");

        assertThat(result).isFalse();
        verify(authenticationTokenRepository).findAll();
    }

    @Test
    void testValidateAuthenticationTokenWithNull() {
        when(authenticationTokenRepository.findAll()).thenReturn(Collections.emptyList());

        Boolean result = authenticationTokenService.validateAuthenticationToken(null);

        assertThat(result).isFalse();
    }

    @Test
    void testValidateAuthenticationTokenWithMultipleTokens() {
        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("test2@example.com");

        AuthenticationToken token2 = new AuthenticationToken();
        token2.setId(2L);
        token2.setToken(UUID.randomUUID().toString());
        token2.setUser(user2);

        List<AuthenticationToken> tokens = List.of(testToken, token2);
        when(authenticationTokenRepository.findAll()).thenReturn(tokens);

        Boolean result = authenticationTokenService.validateAuthenticationToken(token2.getToken());

        assertThat(result).isTrue();
    }

    @Test
    void testValidateAuthenticationTokenEmptyRepository() {
        when(authenticationTokenRepository.findAll()).thenReturn(Collections.emptyList());

        Boolean result = authenticationTokenService.validateAuthenticationToken(testToken.getToken());

        assertThat(result).isFalse();
    }
}
