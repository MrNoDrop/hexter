package be.hexter.hexter.service.exception;

public class EmailRegisteredException extends RuntimeException {

    public EmailRegisteredException(String email) {
        super("[EMAIL]:This email is already registered.");
    }
}
