package ca.uqtr.tp3.persistence.metadata;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import ca.uqtr.tp3.persistence.annotation.Column;
import ca.uqtr.tp3.persistence.exception.UnreliableMethodColumnDeclaration;

/**
 * Represent a column metadata.
 *
 */
public class ColumnInfo {
	/**
	 * Column name
	 */
	private String name;
	
	/**
	 * AnnotatedElement
	 */
	private AnnotatedElement annotatedElement;
	
	public ColumnInfo(AnnotatedElement el) {
		if(el == null) {
			throw new NullPointerException("Can't construct column metadata from null");
		}
		
		this.annotatedElement = el;
		
		Column annotation = el.getAnnotation(Column.class);
		
		if(annotation != null) {	
			if(annotation.name().isEmpty()) {
				this.name = ((Field) annotatedElement).getName();
			}else {
				this.name = annotation.name();
			}
		}else {
			this.name = ((Field) annotatedElement).getName();
		}
	}
	/**
	 * Initializes a newly created ColumnInfo object so that it represents a ColumnInfo. 
	 *
	 * @param name
	 * @param annotatedElement
	 * @throws UnreliableMethodColumnDeclaration 
	 */
	public ColumnInfo(String name, AnnotatedElement annotatedElement) throws UnreliableMethodColumnDeclaration {
		this(annotatedElement);
		this.name = name;
	}
	/**
	 * @return the column name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the annotatedElement
	 */
	public AnnotatedElement getAnnotatedElement() {
		return annotatedElement;
	}
	/**
	 * @param annotatedElement the annotatedElement to set
	 */
	public void setAnnotatedElement(AnnotatedElement annotatedElement) {
		this.annotatedElement = annotatedElement;
	}
}
