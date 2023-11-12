package be.hexter.hexter.other.helper.exception;

public class PasswordFormatException extends RuntimeException {

    public PasswordFormatException(String message) {
        super("[PASSWORD]:" + message);
    }

}
