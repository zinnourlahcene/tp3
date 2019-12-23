package ca.uqtr.tp3.persistence.interfaces;

import java.util.List;

import ca.uqtr.tp3.persistence.exception.PersitenceException;

public interface EntityManager {
	<T> List<T> retrieve(Class<T> bean, String sql) throws PersitenceException;
	<T> int insert(T bean) throws PersitenceException;
	<T> int bulkInsert(List<T> bean) throws PersitenceException;
}
