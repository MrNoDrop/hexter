package be.hexter.hexter.service.exception;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException(String email) {
        super("[PASSWORD]:The given password doesn't correspond to \"" + email + "\".");
    }
}
