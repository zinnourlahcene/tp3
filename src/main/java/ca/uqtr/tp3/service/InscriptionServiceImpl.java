package ca.uqtr.tp3.service;

import ca.uqtr.tp3.beans.Course;
import ca.uqtr.tp3.beans.Inscription;
import ca.uqtr.tp3.beans.Student;
import ca.uqtr.tp3.persistence.PersistenceManager;
import ca.uqtr.tp3.persistence.exception.PersitenceException;
import ca.uqtr.tp3.persistence.interfaces.EntityManager;
import ca.uqtr.tp3.service.interfaces.InscriptionService;
import com.google.inject.Inject;

import java.util.List;

public class InscriptionServiceImpl implements InscriptionService {

    private EntityManager entityManager;

    public InscriptionServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Inscription> getAllInscription() throws PersitenceException {
        return this.entityManager.retrieve(Inscription.class, "SELECT * FROM inscription");
    }

    //-----------------------------------------One inscription & one course & one student
    @Override
    public void insertInscription(Student student, Course course) throws PersitenceException {
        Inscription inscription = new Inscription(student.getStudentId(), course.getCourseId(),course,student);
        this.entityManager.insert(inscription);
    }

    @Override
    public void insertInscription(Inscription inscription) throws PersitenceException {
        this.entityManager.insert(inscription);
    }

    //-----------------------------------------Many inscription & Many course & one student
    @Override
    public void insertInscriptionMany(Student student, List<Inscription> inscriptions) throws PersitenceException {
        inscriptions.forEach(i -> {
            try {
                insertInscription(i);
            } catch (PersitenceException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Inscription getInscriptionByStudentAndByCourse(Student student, Course course) throws PersitenceException {
        return this.entityManager.retrieve(Inscription.class, "SELECT * FROM inscription WHERE etudiantid = "+student.getStudentId()+" AND coursid = "+course.getCourseId()+";").get(0);

    }

    //-----------------------------------------Exist (Student, Course, Inscription)-------------------------------------------------------
    //--------------------------------------Is student exist (has) an insription with the same course
    @Override
    public boolean isInscriptionExist(Inscription inscription){
        if(inscription == null)
            return true;
        else
            return false;
    }

}
