package be.patryksitko.contest.ip2location.com.service.exception;

public class EmailUnregisteredException extends RuntimeException {

    public EmailUnregisteredException(String email) {
        super("[EMAIL]:The email \"" + email + "\" is unregistered.");
    }
}
