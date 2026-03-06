package be.hexter.hexter.service.exception;

public class HashNotFoundException extends RuntimeException {

    public HashNotFoundException(Integer hash) {
        super(String.format("Hash '%d' not found or is unavailable", hash));
    }
}
