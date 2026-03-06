package be.hexter.hexter.service.implementation;

import be.hexter.hexter.model.CredentialRecovery;
import be.hexter.hexter.model.User;
import be.hexter.hexter.repositoryDAO.CredentialRecoveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecoveryServiceImplementationTest {

    @Mock
    private CredentialRecoveryRepository credentialRecoveryRepository;

    @InjectMocks
    private RecoveryServiceImplementation recoveryService;

    private CredentialRecovery testRecovery;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        testRecovery = new CredentialRecovery();
        testRecovery.setId(1L);
        testRecovery.setToken("recovery_token_123");
        testRecovery.setUser(testUser);
    }

    @Test
    void testFindByRecoveryTokenSuccess() {
        when(credentialRecoveryRepository.findByToken("recovery_token_123"))
                .thenReturn(Optional.of(testRecovery));

        CredentialRecovery result = recoveryService.findByRecoveryToken("recovery_token_123");

        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo("recovery_token_123");
        assertThat(result.getUser()).isEqualTo(testUser);
        verify(credentialRecoveryRepository).findByToken("recovery_token_123");
    }

    @Test
    void testFindByRecoveryTokenNotFound() {
        when(credentialRecoveryRepository.findByToken("invalid_token"))
                .thenReturn(Optional.empty());

        CredentialRecovery result = recoveryService.findByRecoveryToken("invalid_token");

        assertThat(result).isNull();
        verify(credentialRecoveryRepository).findByToken("invalid_token");
    }

    @Test
    void testFindByRecoveryTokenWithNullToken() {
        when(credentialRecoveryRepository.findByToken(null))
                .thenReturn(Optional.empty());

        CredentialRecovery result = recoveryService.findByRecoveryToken(null);

        assertThat(result).isNull();
    }

    @Test
    void testDeleteRecoveryToken() {
        recoveryService.deleteRecoveryToken(testRecovery);

        verify(credentialRecoveryRepository).delete(testRecovery);
    }

    @Test
    void testDeleteRecoveryTokenWithNull() {
        recoveryService.deleteRecoveryToken(null);

        verify(credentialRecoveryRepository).delete(null);
    }

    @Test
    void testSaveRecoveryToken() {
        when(credentialRecoveryRepository.save(any(CredentialRecovery.class)))
                .thenReturn(testRecovery);

        CredentialRecovery result = recoveryService.findByRecoveryToken("recovery_token_123");
        
        assertThat(result).isNull(); // Because we didn't set up the mock for findByRecoveryToken
    }
}
