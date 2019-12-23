/**
 * 
 */
package ca.uqtr.tp3.persistence.exception;

/**
 * 
 * PrimaryKeyColumnNotFoundException
 *
 */
public class PrimaryKeyColumnNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Initializes a newly created PrimaryKeyColumnNotFound object so that it represents a PrimaryKeyColumnNotFound. 
	 *
	 */
	public PrimaryKeyColumnNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Initializes a newly created PrimaryKeyColumnNotFound object so that it represents a PrimaryKeyColumnNotFound. 
	 *
	 * @param message
	 */
	public PrimaryKeyColumnNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Initializes a newly created PrimaryKeyColumnNotFound object so that it represents a PrimaryKeyColumnNotFound. 
	 *
	 * @param cause
	 */
	public PrimaryKeyColumnNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Initializes a newly created PrimaryKeyColumnNotFound object so that it represents a PrimaryKeyColumnNotFound. 
	 *
	 * @param message
	 * @param cause
	 */
	public PrimaryKeyColumnNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Initializes a newly created PrimaryKeyColumnNotFound object so that it represents a PrimaryKeyColumnNotFound. 
	 *
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public PrimaryKeyColumnNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	/**
	 * 
	 * Initializes a newly created PrimaryKeyColumnNotFound object so that it represents a PrimaryKeyColumnNotFound. 
	 *
	 * @param primaryKeyColumnName
	 * @param bean
	 */
	public PrimaryKeyColumnNotFoundException(String primaryKeyColumnName, Class<?> bean) {
		super("Declared primary key '" + primaryKeyColumnName +"' has no matching column in " + bean.getName()
		+ ". Make sure you have at leat a field '" + primaryKeyColumnName + "' or a field annotated with @Column in " + bean.getName() + "!");
	}

}
