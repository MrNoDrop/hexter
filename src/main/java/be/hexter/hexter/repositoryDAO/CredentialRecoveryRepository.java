package be.hexter.hexter.repositoryDAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import be.hexter.hexter.model.Credential;
import be.hexter.hexter.model.CredentialRecovery;
import java.util.List;

@Repository
public interface CredentialRecoveryRepository extends CrudRepository<CredentialRecovery, Long> {

    CredentialRecovery findByRecoveryToken(String recoveryToken);

    List<CredentialRecovery> findByCredential(Credential credential);

}
