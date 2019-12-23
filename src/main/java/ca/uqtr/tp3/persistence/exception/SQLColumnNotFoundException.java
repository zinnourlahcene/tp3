package ca.uqtr.tp3.persistence.exception;

public class SQLColumnNotFoundException extends PersitenceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SQLColumnNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public SQLColumnNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public SQLColumnNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public SQLColumnNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public SQLColumnNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
