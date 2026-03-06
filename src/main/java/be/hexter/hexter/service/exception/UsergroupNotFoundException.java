package be.hexter.hexter.service.exception;

public class UsergroupNotFoundException extends RuntimeException {

    public UsergroupNotFoundException(String firstname, String lastname) {
        super(String.format("No users found matching: %s, %s", firstname, lastname));
    }
}
