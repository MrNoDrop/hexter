package be.hexter.hexter.service.implementation;

import be.hexter.hexter.model.AuthenticationToken;
import be.hexter.hexter.model.Credential;
import be.hexter.hexter.model.CredentialRecovery;
import be.hexter.hexter.model.User;
import be.hexter.hexter.other.RandomHash;
import be.hexter.hexter.repositoryDAO.AuthenticationTokenRepository;
import be.hexter.hexter.repositoryDAO.CredentialRecoveryRepository;
import be.hexter.hexter.repositoryDAO.CredentialRepository;
import be.hexter.hexter.repositoryDAO.UserRepository;
import be.hexter.hexter.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private CredentialRecoveryRepository credentialRecoveryRepository;

    @Mock
    private AuthenticationTokenRepository authenticationTokenRepository;

    @InjectMocks
    private UserServiceImplementation userService;

    private User testUser;
    private Credential testCredential;
    private AuthenticationToken testToken;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setNickname("testuser");
        testUser.setHash(UUID.randomUUID().toString());

        testCredential = new Credential();
        testCredential.setId(1L);
        testCredential.setPassword("hashedPassword");
        testCredential.setUser(testUser);

        testToken = new AuthenticationToken();
        testToken.setId(1L);
        testToken.setToken(UUID.randomUUID().toString());
        testToken.setUser(testUser);
    }

    @Test
    void testRegisterUserSuccess() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(credentialRepository.save(any(Credential.class))).thenReturn(testCredential);

        User result = userService.registerUser("test@example.com", "testuser", "password123");

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getNickname()).isEqualTo("testuser");
        verify(userRepository).save(any(User.class));
        verify(credentialRepository).save(any(Credential.class));
    }

    @Test
    void testRegisterUserWithExistingEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        User result = userService.registerUser("test@example.com", "testuser", "password123");

        assertThat(result).isNull();
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAuthenticateUserSuccess() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(credentialRepository.findByUser(testUser)).thenReturn(Optional.of(testCredential));
        when(authenticationTokenRepository.save(any(AuthenticationToken.class))).thenReturn(testToken);

        AuthenticationToken result = userService.authenticateUser("test@example.com", "password123");

        assertThat(result).isNotNull();
        assertThat(result.getToken()).isNotNull();
        verify(authenticationTokenRepository).save(any(AuthenticationToken.class));
    }

    @Test
    void testAuthenticateUserWithInvalidEmail() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        AuthenticationToken result = userService.authenticateUser("nonexistent@example.com", "password123");

        assertThat(result).isNull();
        verify(authenticationTokenRepository, never()).save(any(AuthenticationToken.class));
    }

    @Test
    void testFindByRecoveryTokenSuccess() {
        CredentialRecovery recovery = new CredentialRecovery();
        recovery.setId(1L);
        recovery.setToken("recovery_token_123");
        recovery.setUser(testUser);

        when(credentialRecoveryRepository.findByToken("recovery_token_123")).thenReturn(Optional.of(recovery));

        CredentialRecovery result = userService.findByRecoveryToken("recovery_token_123");

        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo("recovery_token_123");
    }

    @Test
    void testFindByRecoveryTokenNotFound() {
        when(credentialRecoveryRepository.findByToken("invalid_token")).thenReturn(Optional.empty());

        CredentialRecovery result = userService.findByRecoveryToken("invalid_token");

        assertThat(result).isNull();
    }

    @Test
    void testFindByHashSuccess() {
        when(userRepository.findByHash(testUser.getHash())).thenReturn(Optional.of(testUser));

        User result = userService.findByHash(testUser.getHash());

        assertThat(result).isNotNull();
        assertThat(result.getHash()).isEqualTo(testUser.getHash());
    }

    @Test
    void testFindByHashNotFound() {
        when(userRepository.findByHash("nonexistent_hash")).thenReturn(Optional.empty());

        User result = userService.findByHash("nonexistent_hash");

        assertThat(result).isNull();
    }

    @Test
    void testDeleteRecoveryToken() {
        CredentialRecovery recovery = new CredentialRecovery();
        recovery.setId(1L);

        userService.deleteRecoveryToken(recovery);

        verify(credentialRecoveryRepository).delete(recovery);
    }
}
