package ca.uqtr.tp3.service;

import ca.uqtr.tp3.beans.Course;
import ca.uqtr.tp3.persistence.exception.PersitenceException;
import ca.uqtr.tp3.persistence.interfaces.EntityManager;
import ca.uqtr.tp3.service.interfaces.CourseService;

import java.util.List;

public class CourseServiceImpl implements CourseService {


    private EntityManager entityManager;

    public CourseServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    //-----------------------------------------Course-------------------------------------------------------

    @Override
    public int insertCourse(Course course) throws PersitenceException {
        System.out.println("\n------------- insertCourse -----------\n");
        System.out.println("Creation d'un cours : \n");
        return this.entityManager.insert(course);
    }

    @Override
    public List<Course> getCourseList() throws PersitenceException {
        System.out.println("\n------------- courseList -----------\n");
        System.out.println("\nLa liste des cours : \n");
        return this.entityManager.retrieve(Course.class, "SELECT * FROM cours;");
    }

    @Override
    public Course getCourseBySigle(String sigle) throws PersitenceException {
        System.out.println("\n------------- courseBySigle -----------\n");
        System.out.println("Cours ("+sigle+") : \n");
        return this.entityManager.retrieve(Course.class, "SELECT * FROM cours as c WHERE c.sigle = '"+sigle+"';").get(0);
    }

    @Override
    public Course  getCourseById(int id) throws PersitenceException {
        System.out.println("\n------------- courseById -----------\n");
        System.out.println("Cours ("+id+") : \n");
        return this.entityManager.retrieve(Course.class, "SELECT * FROM cours as c WHERE c.coursid = "+id+";").get(0);
    }


}
