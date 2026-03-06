package be.hexter.hexter.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import be.hexter.hexter.model.AuthenticationToken;
import be.hexter.hexter.repositoryDAO.AuthenticationTokenRepository;
import be.hexter.hexter.service.AuthenticationTokenService;

@Service
public class AuthenticationTokenServiceImplementation implements AuthenticationTokenService {

    private final AuthenticationTokenRepository authenticationTokenRepository;

    public AuthenticationTokenServiceImplementation(AuthenticationTokenRepository authenticationTokenRepository) {
        this.authenticationTokenRepository = authenticationTokenRepository;
    }

    @Override
    public boolean validateAuthenticationToken(AuthenticationToken providedAuthenticationToken) {
        List<AuthenticationToken> authenticationTokens = authenticationTokenRepository
                .findByFingerprint(providedAuthenticationToken.getFingerprint());
        return authenticationTokens.stream()
                .anyMatch(indexedAuthenticationToken -> indexedAuthenticationToken.getAuthenticationToken()
                        .equals(providedAuthenticationToken.getAuthenticationToken()));
    }

}
