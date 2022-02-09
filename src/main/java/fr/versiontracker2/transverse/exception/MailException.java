package fr.versiontracker2.transverse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class MailException extends Exception {

	private static final long serialVersionUID = 1744456948152208838L;

	public MailException() {
        super();
    }
    public MailException(String message, Throwable cause) {
        super(message, cause);
    }
    public MailException(String message) {
        super(message);
    }
    public MailException(Throwable cause) {
        super(cause);
    }
}