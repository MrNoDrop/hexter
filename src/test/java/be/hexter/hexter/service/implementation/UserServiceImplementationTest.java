package be.hexter.hexter.service.implementation;

import be.hexter.hexter.model.CredentialRecovery;
import be.hexter.hexter.model.User;
import be.hexter.hexter.repositoryDAO.AuthenticationTokenRepository;
import be.hexter.hexter.repositoryDAO.CredentialRecoveryRepository;
import be.hexter.hexter.repositoryDAO.UserRepository;
import be.hexter.hexter.service.exception.UserNotFoundException;
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
class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CredentialRecoveryRepository credentialRecoveryRepository;

    @Mock
    private AuthenticationTokenRepository authenticationTokenRepository;

    @InjectMocks
    private UserServiceImplementation userService;

    @BeforeEach
    void setUp() {
        assertThat(userService).isNotNull();
    }

    @Test
    void testServiceInstantiation() {
        assertThat(userService).isNotNull();
        System.out.println("✅ UserServiceImplementation instantiated successfully");
    }

    @Test
    void testFindUserByEmailSuccess() throws Exception {
        User testUser = User.builder().build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);

        User result = userService.findUserByEmail("test@example.com");

        assertThat(result).isNotNull();
        verify(userRepository).findByEmail("test@example.com");
        System.out.println("✅ findUserByEmail works correctly");
    }

    @Test
    void testFindByRecoveryTokenSuccess() {
        User expectedUser = User.builder().build();
        CredentialRecovery recovery = CredentialRecovery.builder()
                .credential(expectedUser.getCredential())
                .build();

        when(credentialRecoveryRepository.findByRecoveryToken("recovery_token_123")).thenReturn(recovery);

        User result = userService.findByRecoveryToken("recovery_token_123");

        assertThat(result).isEqualTo(expectedUser);
        System.out.println("✅ findByRecoveryToken returns the associated user");
    }

    @Test
    void testFindByRecoveryTokenNotFound() {
        when(credentialRecoveryRepository.findByRecoveryToken("invalid_token")).thenReturn(null);

        assertThatThrownBy(() -> userService.findByRecoveryToken("invalid_token"))
                .isInstanceOf(UserNotFoundException.class);
        System.out.println("✅ findByRecoveryToken throws when recovery not found");
    }

    @Test
    void testStoreCredentialRecoveryToken() {
        User testUser = User.builder().build();

        userService.storeCredentialRecoveryToken(testUser, "recovery_token_123");

        verify(credentialRecoveryRepository).save(any(CredentialRecovery.class));
        System.out.println("✅ storeCredentialRecoveryToken saves to repository");
    }
}
