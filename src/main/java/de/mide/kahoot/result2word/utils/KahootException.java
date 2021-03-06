package de.mide.kahoot.result2word.utils;

/**
 * Custom exception class.
 * <br><br>
 *
 * This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3).
 */
public class KahootException extends Exception {

	private static final long serialVersionUID = 1L;


	/**
	 * Create new exception object, message needs to be supplied.
	 *
	 * @param message  Message describing the cause of the exception.
	 */
	public KahootException(String message) {

		super(message);
	}


	/**
	 * Create new exception object, message and other exception objectneeds to be supplied.
	 *
	 * @param message  Message describing the cause of the exception.
	 *
	 * @param exception  Exception object to be encapsulated by this object.
	 */
	public KahootException(String message, Exception exception) {

		super(message, exception);
	}

}
