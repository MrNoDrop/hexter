package be.hexter.hexter.repositoryDAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import be.hexter.hexter.model.Credential;

@Repository
public interface CredentialRepository extends CrudRepository<Credential, Long> {

    Credential findByEmail(String email);
}
