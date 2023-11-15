package be.hexter.hexter.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import be.hexter.hexter.model.AuthenticationToken;
import be.hexter.hexter.model.Credential;
import be.hexter.hexter.model.User;
import be.hexter.hexter.service.exception.EmailRegisteredException;
import be.hexter.hexter.service.exception.EmailUnregisteredException;
import be.hexter.hexter.service.exception.PasswordMismatchException;
import be.hexter.hexter.service.exception.UsergroupNotFoundException;

public interface UserService {

        public User registerUser(User user) throws EmailRegisteredException;

        public User findUserByEmail(String email) throws EmailUnregisteredException;

        public User findUserByFirstname(String firstname) throws UsernameNotFoundException;

        public User findUserByLastname(String lastname) throws UsernameNotFoundException;

        public List<User> findUsergroupByFirstnameAndLastname(String firstname, String lastname)
                        throws UsergroupNotFoundException;

        public void storeCredentialRecoveryToken(User user, String token);

        public AuthenticationToken authenticateUser(Credential credential)
                        throws EmailUnregisteredException, PasswordMismatchException;
}