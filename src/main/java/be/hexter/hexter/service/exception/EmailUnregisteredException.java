package be.hexter.hexter.service.exception;

public class EmailUnregisteredException extends RuntimeException {

    public EmailUnregisteredException(String email) {
        super("[EMAIL]:This email is unregistered.");
    }
}
