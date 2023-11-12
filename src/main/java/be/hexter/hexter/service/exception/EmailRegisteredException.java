package be.patryksitko.contest.ip2location.com.service.exception;

public class EmailRegisteredException extends RuntimeException {

    public EmailRegisteredException(String email) {
        super("[EMAIL]:The email \"" + email + "\" is already registered.");
    }
}
