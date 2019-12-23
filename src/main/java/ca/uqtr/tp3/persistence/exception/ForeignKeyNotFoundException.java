package ca.uqtr.tp3.persistence.exception;

import java.util.stream.Collectors;

import ca.uqtr.tp3.utils.ReflectionUtils;

public class ForeignKeyNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ForeignKeyNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public ForeignKeyNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ForeignKeyNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ForeignKeyNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ForeignKeyNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ForeignKeyNotFoundException(Class<?> parentClass, Class<?> innerType) throws PrimaryKeyColumnNotFoundException {
		this("No foreign key found in " + parentClass.getName() + " class "
				+ "for " + innerType.getName() + " primary key(s): " + ReflectionUtils.getPrimaryKeys(innerType)
				.stream()
				.map(p -> p.getName())
				.collect(Collectors.joining(", ")) + ", "
						+ "Please add one of these primary key(s) in " + parentClass.getName() + " as foreign key(s)");
	}

}
