package ca.uqtr.tp3.persistence.exception;

public class NoPrimaryKeyFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoPrimaryKeyFoundException() {
		// TODO Auto-generated constructor stub
	}

	public NoPrimaryKeyFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NoPrimaryKeyFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public NoPrimaryKeyFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NoPrimaryKeyFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public NoPrimaryKeyFoundException(Class<?> bean) {
		super("No primary key found in class " + bean.getName());
	}
	
	public NoPrimaryKeyFoundException(Class<?> bean, Throwable cause) {
		super("No primary key found in class " + bean.getName(), cause);
	}
}
