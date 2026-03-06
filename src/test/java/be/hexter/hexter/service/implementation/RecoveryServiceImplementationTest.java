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

    @Test
    void testServiceInstantiation() {
        assertThat(recoveryService).isNotNull();
        System.out.println("✅ RecoveryServiceImplementation instantiated successfully");
    }

    @Test
    void testFindByRecoveryTokenInvocation() {
        CredentialRecovery recovery = CredentialRecovery.builder().build();

        // repository returns object (not Optional)
        when(credentialRecoveryRepository.findByRecoveryToken("recovery_token_123"))
                .thenReturn(recovery);

        CredentialRecovery result = recoveryService.findByRecoveryToken("recovery_token_123");
        assertThat(result).isEqualTo(recovery);
    }

    @Test
    void testFindByRecoveryTokenNull() {
        when(credentialRecoveryRepository.findByRecoveryToken("invalid_token"))
                .thenReturn(null);

        CredentialRecovery result = recoveryService.findByRecoveryToken("invalid_token");
        assertThat(result).isNull();
    }
}
