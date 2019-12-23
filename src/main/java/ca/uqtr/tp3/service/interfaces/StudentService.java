package ca.uqtr.tp3.service.interfaces;

import ca.uqtr.tp3.beans.Student;
import ca.uqtr.tp3.persistence.exception.PersitenceException;
import ca.uqtr.tp3.persistence.interfaces.EntityManager;

import java.util.List;

public interface StudentService {

    int insertStudent(Student student) throws PersitenceException ;

    List<Student> getStudentsList() throws PersitenceException;

    Student getStudentByFirstName(String firstName) throws PersitenceException ;

    Student getStudentById(int id) throws PersitenceException ;

    Student getStudentBySqlQuery(String query) throws PersitenceException;

    List<Student> getListStudentBySqlQuery(String query) throws PersitenceException;
}
