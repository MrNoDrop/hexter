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

    @Test
    void testServiceInstantiation() {
        assertThat(authenticationTokenService).isNotNull();
        System.out.println("✅ AuthenticationTokenServiceImplementation instantiated successfully");
    }

    @Test
    void testValidateAuthenticationTokenSuccess() {
        AuthenticationToken testToken = AuthenticationToken.builder()
                .fingerprint("test_fingerprint_123")
                .authenticationToken(UUID.randomUUID())
                .build();

        List<AuthenticationToken> tokens = Collections.singletonList(testToken);
        when(authenticationTokenRepository.findByFingerprint(testToken.getFingerprint()))
                .thenReturn(tokens);

        boolean result = authenticationTokenService.validateAuthenticationToken(testToken);

        assertThat(result).isTrue();
        verify(authenticationTokenRepository).findByFingerprint(testToken.getFingerprint());
        System.out.println("✅ validateAuthenticationToken returns true for valid token");
    }

    @Test
    void testValidateAuthenticationTokenNotFound() {
        AuthenticationToken invalidToken = AuthenticationToken.builder()
                .fingerprint("invalid_fingerprint")
                .authenticationToken(UUID.randomUUID())
                .build();

        when(authenticationTokenRepository.findByFingerprint("invalid_fingerprint"))
                .thenReturn(Collections.emptyList());

        boolean result = authenticationTokenService.validateAuthenticationToken(invalidToken);

        assertThat(result).isFalse();
        verify(authenticationTokenRepository).findByFingerprint("invalid_fingerprint");
        System.out.println("✅ validateAuthenticationToken returns false for invalid token");
    }

    @Test
    void testValidateAuthenticationTokenWithMultipleTokens() {
        AuthenticationToken token1 = AuthenticationToken.builder()
                .fingerprint("test_fingerprint_123")
                .authenticationToken(UUID.randomUUID())
                .build();

        AuthenticationToken token2 = AuthenticationToken.builder()
                .fingerprint("test_fingerprint_123")
                .authenticationToken(UUID.randomUUID())
                .build();

        List<AuthenticationToken> tokens = List.of(token1, token2);
        when(authenticationTokenRepository.findByFingerprint(token1.getFingerprint()))
                .thenReturn(tokens);

        boolean result = authenticationTokenService.validateAuthenticationToken(token1);

        assertThat(result).isTrue();
        System.out.println("✅ validateAuthenticationToken finds token from multiple options");
    }

    @Test
    void testValidateAuthenticationTokenEmptyRepository() {
        AuthenticationToken testToken = AuthenticationToken.builder()
                .fingerprint("test_fingerprint")
                .authenticationToken(UUID.randomUUID())
                .build();

        when(authenticationTokenRepository.findByFingerprint(testToken.getFingerprint()))
                .thenReturn(Collections.emptyList());

        boolean result = authenticationTokenService.validateAuthenticationToken(testToken);

        assertThat(result).isFalse();
        System.out.println("✅ validateAuthenticationToken returns false for empty repository");
    }
}
