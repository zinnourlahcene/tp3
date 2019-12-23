/**
 * 
 */
package ca.uqtr.tp3.persistence.exception;

import ca.uqtr.tp3.persistence.annotation.Table;

/**
 * MissingTableAnnotation
 *
 */
public class MissingTableAnnotation extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Initializes a newly created MissingTableAnnotation object so that it represents a MissingTableAnnotation. 
	 *
	 */
	public MissingTableAnnotation() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Initializes a newly created MissingTableAnnotation object so that it represents a MissingTableAnnotation. 
	 *
	 * @param message
	 */
	public MissingTableAnnotation(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Initializes a newly created MissingTableAnnotation object so that it represents a MissingTableAnnotation. 
	 *
	 * @param cause
	 */
	public MissingTableAnnotation(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Initializes a newly created MissingTableAnnotation object so that it represents a MissingTableAnnotation. 
	 *
	 * @param message
	 * @param cause
	 */
	public MissingTableAnnotation(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Initializes a newly created MissingTableAnnotation object so that it represents a MissingTableAnnotation. 
	 *
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public MissingTableAnnotation(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public MissingTableAnnotation(Class<?> bean) {
		super("No @"+ Table.class.getSimpleName()+" found on " + bean.getName());
	}

}
