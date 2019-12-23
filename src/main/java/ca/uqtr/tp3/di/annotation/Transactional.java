package ca.uqtr.tp3.di.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that a method should be in transactional mode
 *
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface Transactional {

}
