package fr.versiontracker2.transverse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class NonReadableDependencyFileException extends Exception {

	private static final long serialVersionUID = 3454881829056051163L;

	public NonReadableDependencyFileException() {
        super();
    }
    public NonReadableDependencyFileException(String message, Throwable cause) {
        super(message, cause);
    }
    public NonReadableDependencyFileException(String message) {
        super(message);
    }
    public NonReadableDependencyFileException(Throwable cause) {
        super(cause);
    }
}
