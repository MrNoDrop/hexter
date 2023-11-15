package be.hexter.hexter.service.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User could not be found.");
    }
}
