package ca.uqtr.tp3.persistence.exception;

public class ReflectionException extends PersitenceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReflectionException() {
		// TODO Auto-generated constructor stub
	}

	public ReflectionException(String message) {
		super(message);
	}

	public ReflectionException(Throwable cause) {
		super(cause);
	}

	public ReflectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReflectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
