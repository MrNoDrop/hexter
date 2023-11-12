package be.patryksitko.contest.ip2location.com.component.exception;

public class InitializationException extends RuntimeException {
    public InitializationException() {
        super("Could not initialize databases.");
    }
}
