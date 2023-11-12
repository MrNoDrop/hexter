package be.hexter.hexter.component.exception;

public class DownloadLimitException extends RuntimeException {
    public DownloadLimitException() {
        super("Download limit reached: \"THIS FILE CAN ONLY BE DOWNLOADED 5 TIMES PER HOUR\".");
    }
}
