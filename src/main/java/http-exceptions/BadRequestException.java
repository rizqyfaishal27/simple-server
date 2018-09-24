package exceptions;

import java.lang.Exception;

public class BadRequestException extends Exception {

	public BadRequestException(String message) {
		super(message);
	}
}