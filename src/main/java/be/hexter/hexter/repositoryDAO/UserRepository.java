package be.hexter.hexter.repositoryDAO;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import be.hexter.hexter.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT user FROM User AS user INNER JOIN user.credential AS credential WHERE credential.email = ?1")
    User findByEmail(String email);

    User findUserByFirstname(String firstname);

    User findUserByLastname(String lastname);

    @Query("SELECT user From User AS user Where user.firstname = ?1 AND user.lastname = ?2")
    List<User> findUsergroupByFirstAndLastName(String firstname, String lastname);
}
