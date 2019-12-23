package ca.uqtr.tp3.service;

import ca.uqtr.tp3.beans.Student;
import ca.uqtr.tp3.persistence.exception.PersitenceException;
import ca.uqtr.tp3.persistence.interfaces.EntityManager;
import ca.uqtr.tp3.service.interfaces.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {

    private EntityManager entityManager;

    public StudentServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public int insertStudent(Student student) throws PersitenceException {
        System.out.println("\n------------- insertStudent -----------\n");
        System.out.println("Creation d'un etudiant : \n");
        return this.entityManager.insert(student);
    }

    @Override
    public List<Student> getStudentsList() throws PersitenceException{
        System.out.println("\n------------- studentsList -----------\n");
        System.out.println("La liste des etudiants : \n");
        return this.entityManager.retrieve(Student.class, "SELECT * FROM etudiant;");
    }

    @Override
    public Student getStudentByFirstName(String firstName) throws PersitenceException {
        System.out.println("\n------------- studentByFirstName -----------\n");
        System.out.println("Le dossier de l'etudiant : "+firstName+ "\n");
        return this.entityManager.retrieve(Student.class, "SELECT * FROM etudiant as e WHERE e.fname = '"+firstName+"';").get(0);
    }

    @Override
    public Student getStudentById(int id) throws PersitenceException {
        System.out.println("\n------------- studentById -----------\n");
        System.out.println("Le dossier de l'etudiant avec l'identifiant : "+id+ "\n");
        return this.entityManager.retrieve(Student.class, "SELECT * FROM etudiant as e WHERE e.etudiantid = "+id+";").get(0);
    }

    @Override
    public Student getStudentBySqlQuery(String query) throws PersitenceException {
        System.out.println("\n------------- getStudentBySqlQuery -----------\n");
        System.out.println("Etudiant avec la requete sql ("+query+") : \n");
        return this.entityManager.retrieve(Student.class, query).get(0);
    }

    @Override
    public List<Student> getListStudentBySqlQuery(String query) throws PersitenceException {
        System.out.println("\n------------- getListStudentBySqlQuery -----------\n");
        System.out.println("Liste des etudiants avec la requete sql ("+query+") : \n");
        return this.entityManager.retrieve(Student.class, query);
    }
}
