package be.hexter.hexter.service;

import be.hexter.hexter.model.AuthenticationToken;
import be.hexter.hexter.model.Credential;
import be.hexter.hexter.model.User;
import be.hexter.hexter.service.exception.EmailRegisteredException;
import be.hexter.hexter.service.exception.EmailUnregisteredException;
import be.hexter.hexter.service.exception.PasswordMismatchException;

public interface UserService {

    public User registerUser(User user) throws EmailRegisteredException;

    public User findUserByEmail(String email) throws EmailUnregisteredException;

    public AuthenticationToken authenticateUser(Credential credential)
            throws EmailUnregisteredException, PasswordMismatchException;
}