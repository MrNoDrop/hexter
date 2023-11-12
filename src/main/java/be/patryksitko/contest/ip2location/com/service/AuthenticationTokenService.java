package be.patryksitko.contest.ip2location.com.service;

import be.patryksitko.contest.ip2location.com.model.AuthenticationToken;

public interface AuthenticationTokenService {

    boolean validateAuthenticationToken(AuthenticationToken authenticationToken);
}
