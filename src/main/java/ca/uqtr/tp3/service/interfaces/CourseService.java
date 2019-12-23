package ca.uqtr.tp3.service.interfaces;

import ca.uqtr.tp3.beans.Course;
import ca.uqtr.tp3.persistence.exception.PersitenceException;

import java.util.List;

public interface CourseService {

    int insertCourse(Course course) throws PersitenceException;

    List<Course> getCourseList() throws PersitenceException;

    Course getCourseBySigle(String sigle) throws PersitenceException ;

    Course  getCourseById(int id) throws PersitenceException ;
}
