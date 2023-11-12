package be.patryksitko.contest.ip2location.com.repositoryDAO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import be.patryksitko.contest.ip2location.com.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT user FROM User AS user INNER JOIN user.credential AS credential WHERE credential.email = ?1")
    User findByEmail(String email);
}
