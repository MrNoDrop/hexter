package be.hexter.hexter.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.hexter.hexter.model.Credential;
import be.hexter.hexter.model.CredentialRecovery;
import be.hexter.hexter.repositoryDAO.CredentialRecoveryRepository;
import be.hexter.hexter.service.RecoveryService;
import be.hexter.hexter.service.exception.CredentialRecoveryTokenNotFound;

@Service
public class RecoveryServiceImplementation implements RecoveryService {

    @Autowired
    CredentialRecoveryRepository credentialRecoveryRepository;

    @Override
    public CredentialRecovery findByRecoveryToken(String token) {
        final CredentialRecovery credentialRecovery = credentialRecoveryRepository.findByRecoveryToken(token);

        if (credentialRecovery instanceof CredentialRecovery) {
            return credentialRecovery;
        }
        throw new CredentialRecoveryTokenNotFound();
    }

    @Override
    public List<CredentialRecovery> findByCredential(Credential credential) {
        return credentialRecoveryRepository.findByCredential(credential);
    }

}
