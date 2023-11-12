package be.patryksitko.contest.ip2location.com.repositoryDAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import be.patryksitko.contest.ip2location.com.model.Credential;

@Repository
public interface CredentialRepository extends CrudRepository<Credential, Long> {

    Credential findByEmail(String email);
}
