package be.hexter.hexter.service;

import java.util.List;

import be.hexter.hexter.model.Credential;
import be.hexter.hexter.model.CredentialRecovery;

public interface RecoveryService {

    CredentialRecovery findByRecoveryToken(String token);

    List<CredentialRecovery> findByCredential(Credential credential);

    void deleteRecoveryToken(String token);
}
