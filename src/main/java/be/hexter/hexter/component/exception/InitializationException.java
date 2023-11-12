package be.hexter.hexter.component.exception;

public class InitializationException extends RuntimeException {
    public InitializationException() {
        super("Could not initialize databases.");
    }
}
