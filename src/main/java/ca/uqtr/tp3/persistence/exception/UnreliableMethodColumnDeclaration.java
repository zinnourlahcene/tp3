package ca.uqtr.tp3.persistence.exception;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

public class UnreliableMethodColumnDeclaration extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnreliableMethodColumnDeclaration(AnnotatedElement annotatedElement) {
		super("You declared a @Column on a " + annotatedElement.getClass().getSimpleName().toLowerCase() 
				+ " without providing the 'name' of the column annotation attribute. "
				+ "Cannot determine a column name for " + annotatedElement.getClass().getSimpleName().toLowerCase()
				+ ((Method) annotatedElement).getName());
	}
}
