package be.hexter.hexter.repositoryDAO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import be.hexter.hexter.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT user FROM User AS user INNER JOIN user.credential AS credential WHERE credential.email = ?1")
    User findByEmail(String email);
}
