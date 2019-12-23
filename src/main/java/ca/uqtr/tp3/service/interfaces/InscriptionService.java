package ca.uqtr.tp3.service.interfaces;

import ca.uqtr.tp3.beans.Course;
import ca.uqtr.tp3.beans.Inscription;
import ca.uqtr.tp3.beans.Student;
import ca.uqtr.tp3.persistence.exception.PersitenceException;

import java.util.List;

public interface InscriptionService {
    List<Inscription> getAllInscription() throws PersitenceException;

    void insertInscription(Student student, Course course) throws PersitenceException;

    void insertInscription(Inscription inscription) throws PersitenceException ;
    //-----------------------------------------Many inscription & Many course & one student
    void insertInscriptionMany(Student student, List<Inscription> inscriptions) throws PersitenceException ;

    Inscription getInscriptionByStudentAndByCourse(Student student, Course course) throws PersitenceException ;

    //-----------------------------------------Exist (Student, Course, Inscription)-------------------------------------------------------
    //--------------------------------------Is student exist (has) an insription with the same course
    boolean isInscriptionExist(Inscription inscription);
}
