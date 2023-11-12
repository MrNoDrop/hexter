package be.patryksitko.contest.ip2location.com.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.patryksitko.contest.ip2location.com.model.AuthenticationToken;
import be.patryksitko.contest.ip2location.com.model.Credential;
import be.patryksitko.contest.ip2location.com.model.User;
import be.patryksitko.contest.ip2location.com.other.helper.BCryptPasswordEncoder;
import be.patryksitko.contest.ip2location.com.repositoryDAO.AuthenticationTokenRepository;
import be.patryksitko.contest.ip2location.com.repositoryDAO.UserRepository;
import be.patryksitko.contest.ip2location.com.service.UserService;
import be.patryksitko.contest.ip2location.com.service.exception.EmailRegisteredException;
import be.patryksitko.contest.ip2location.com.service.exception.EmailUnregisteredException;
import be.patryksitko.contest.ip2location.com.service.exception.PasswordMismatchException;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationTokenRepository authenticationTokenRepository;

    @Override
    public User registerUser(User user) throws EmailRegisteredException, IllegalArgumentException {
        if (userRepository.findByEmail(user.getCredential().getEmail()) != null) {
            throw new EmailRegisteredException(user.getCredential().getEmail());
        }
        user.getCredential().setPassword(BCryptPasswordEncoder.getInstance.encode(user.getCredential().getPassword()));
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
}
