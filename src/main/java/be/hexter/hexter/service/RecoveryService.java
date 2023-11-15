package be.hexter.hexter.service;

import be.hexter.hexter.model.CredentialRecovery;

public interface RecoveryService {

    CredentialRecovery findByRecoveryToken(String token);
}
