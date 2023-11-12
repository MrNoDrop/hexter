package be.patryksitko.contest.ip2location.com.repositoryDAO;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import be.patryksitko.contest.ip2location.com.model.AuthenticationToken;

@Repository
public interface AuthenticationTokenRepository extends CrudRepository<AuthenticationToken, Long> {

    @Query("SELECT authToken FROM AuthenticationToken AS authToken INNER JOIN authToken.credential AS credential WHERE credential.email = ?1")
    List<AuthenticationToken> findByEmail(String email);

    List<AuthenticationToken> findByFingerprint(String fingerprint);
}
