package be.hexter.hexter.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.hexter.hexter.model.AuthenticationToken;
import be.hexter.hexter.repositoryDAO.AuthenticationTokenRepository;
import be.hexter.hexter.service.AuthenticationTokenService;

@Service
public class AuthentictionTokenServiceImplementation implements AuthenticationTokenService {

    @Autowired
    private AuthenticationTokenRepository authenticationTokenRepository;

    @Override
    public boolean validateAuthenticationToken(AuthenticationToken providedAuthenticationToken) {
        List<AuthenticationToken> authenticationTokens = authenticationTokenRepository
                .findByFingerprint(providedAuthenticationToken.getFingerprint());
        boolean isValid = false;
        for (AuthenticationToken indexedAuthenticationToken : authenticationTokens) {
            isValid = indexedAuthenticationToken.getAuthenticationToken()
                    .equals(providedAuthenticationToken.getAuthenticationToken());
            if (isValid) {
                break;
            }
        }
        return isValid;
    }

}
