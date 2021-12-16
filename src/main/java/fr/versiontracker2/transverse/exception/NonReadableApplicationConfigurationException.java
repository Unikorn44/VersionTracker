package fr.versiontracker.transverse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class NonReadableApplicationConfigurationException extends Exception {
    public NonReadableApplicationConfigurationException() {
        super();
    }
    public NonReadableApplicationConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
    public NonReadableApplicationConfigurationException(String message) {
        super(message);
    }
    public NonReadableApplicationConfigurationException(Throwable cause) {
        super(cause);
    }
}
