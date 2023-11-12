package be.hexter.hexter.service;

import be.hexter.hexter.model.AuthenticationToken;

public interface AuthenticationTokenService {

    boolean validateAuthenticationToken(AuthenticationToken authenticationToken);
}
