package be.hexter.hexter.service.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import be.hexter.hexter.model.AuthenticationToken;
import be.hexter.hexter.model.Credential;
import be.hexter.hexter.model.CredentialRecovery;
import be.hexter.hexter.model.User;
import be.hexter.hexter.other.RandomHash;
import be.hexter.hexter.other.helper.BCryptPasswordEncoder;
import be.hexter.hexter.repositoryDAO.AuthenticationTokenRepository;
import be.hexter.hexter.repositoryDAO.CredentialRecoveryRepository;
import be.hexter.hexter.repositoryDAO.UserRepository;
import be.hexter.hexter.service.UserService;
import be.hexter.hexter.service.exception.EmailRegisteredException;
import be.hexter.hexter.service.exception.EmailUnregisteredException;
import be.hexter.hexter.service.exception.PasswordMismatchException;
import be.hexter.hexter.service.exception.UnsafePasswordException;
import be.hexter.hexter.service.exception.UserNotFoundException;
import be.hexter.hexter.service.exception.UsergroupNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CredentialRecoveryRepository credentialRecoveryRepository;

    @Autowired
    private AuthenticationTokenRepository authenticationTokenRepository;

    @Override
    public User registerUser(User user) throws EmailRegisteredException, IllegalArgumentException {
        if (userRepository.findByEmail(user.getCredential().getEmail()) != null) {
            throw new EmailRegisteredException(user.getCredential().getEmail());
        }
        user.getCredential().setPassword(BCryptPasswordEncoder.getInstance.encode(user.getCredential().getPassword()));
        final ArrayList<User> users = (ArrayList<User>) userRepository
                .findUsergroupByFirstAndLastName(user.getFirstname(), user.getLastname());
        String randomHash = null;
        boolean foundMatch = false;
        do {
            foundMatch = false;
            randomHash = RandomHash.generateRandomString(4);
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getHash().equals(randomHash)) {
                    foundMatch = true;
                    break;
                }
            }
        } while (foundMatch);
        user.setHash(randomHash);
        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) throws EmailUnregisteredException {
        final User user = userRepository.findByEmail(email);
        if (user instanceof User) {
            return user;
        }
        throw new EmailUnregisteredException(email);
    }

    @Override
    public AuthenticationToken authenticateUser(Credential credential)
            throws EmailUnregisteredException, PasswordMismatchException {
        final User user = this.findUserByEmail(credential.getEmail());
        if (!(user instanceof User)) {
            throw new EmailUnregisteredException(credential.getEmail());
        }
        final boolean passwordMatches = user.getCredential().isPasswordMatching(credential.getPassword());
        if (!passwordMatches) {
            throw new PasswordMismatchException(credential.getEmail());
        }
        final String fingerprint = credential.getFingerprint();
        final List<AuthenticationToken> authenticationTokens = user.getCredential().getAuthenticationTokens();
        Optional<AuthenticationToken> fingerprintedAuthenticationToken = authenticationTokens.stream()
                .filter((authenticationToken) -> authenticationToken.getFingerprint()
                        .equals(fingerprint))
                .findFirst();
        if (fingerprintedAuthenticationToken.isPresent()) {
            final AuthenticationToken authenticationToken = fingerprintedAuthenticationToken.get();
            authenticationToken.setFingerprint("hidden.");
            return authenticationToken;
        }
        AuthenticationToken registeredAuthenticationToken = authenticationTokenRepository.save(
                AuthenticationToken.builder().id(null).credential(user.getCredential()).fingerprint(fingerprint)
                        .authenticationToken(UUID.randomUUID()).build());
        registeredAuthenticationToken.setFingerprint("hidden.");
        return registeredAuthenticationToken;
    }

    @Override
    public User findUserByFirstname(String firstname) throws UsernameNotFoundException {
        final User user = userRepository.findUserByFirstname(firstname);
        if (user instanceof User) {
            return user;
        }
        throw new UsernameNotFoundException(firstname);
    }

    @Override
    public User findUserByLastname(String lastname) throws UsernameNotFoundException {
        final User user = userRepository.findUserByLastname(lastname);
        if (user instanceof User) {
            return user;
        }
        throw new UsernameNotFoundException(lastname);
    }

    @Override
    public List<User> findUsergroupByFirstnameAndLastname(String firstname, String lastname)
            throws UsergroupNotFoundException {
        final List<User> user = userRepository.findUsergroupByFirstAndLastName(firstname, lastname);
        if (user instanceof List<User>) {
            return user;
        }
        throw new UsergroupNotFoundException(firstname, lastname);
    }

    @Override
    public void storeCredentialRecoveryToken(User user, String token) {
        credentialRecoveryRepository
                .save(new CredentialRecovery(null, token, LocalDateTime.now(), user.getCredential()));
    }

    @Override
    public User findByRecoveryToken(String token) {
        final CredentialRecovery credentialRecovery = credentialRecoveryRepository.findByRecoveryToken(token);

        if (credentialRecovery.credential.getUser() instanceof User) {
            return credentialRecovery.credential.getUser();
        }
        throw new UserNotFoundException();
    }

    @Override
    public void changePassword(String recoveryToken, String password) {
        try {
            final User user = this.findByRecoveryToken(recoveryToken);
            if (user instanceof User) {
                Boolean safePassword = BCryptPasswordEncoder.isPasswordMeetingStandards(password);
                if (safePassword) {
                    user.getCredential().setPassword(BCryptPasswordEncoder.getInstance.encode(password));
                } else {
                    throw new UnsafePasswordException();
                }
                userRepository.save(user);
            } else
                throw new UserNotFoundException();
        } catch (UserNotFoundException ex) {
            log.error(ex.getMessage());
        }
    }
}
