package ca.uqtr.tp3;
import java.util.ArrayList;
import java.util.List;

import ca.uqtr.tp3.beans.Inscription;
import ca.uqtr.tp3.service.CourseServiceImpl;
import ca.uqtr.tp3.service.InscriptionServiceImpl;
import ca.uqtr.tp3.service.StudentServiceImpl;
import ca.uqtr.tp3.service.interfaces.CourseService;
import ca.uqtr.tp3.service.interfaces.InscriptionService;
import ca.uqtr.tp3.service.interfaces.StudentService;
import com.google.inject.Guice;
import com.google.inject.Injector;

import ca.uqtr.tp3.beans.Course;
import ca.uqtr.tp3.beans.Student;
import ca.uqtr.tp3.di.PersistenceModule;
import ca.uqtr.tp3.persistence.interfaces.EntityManager;

public class Application {
    private static StudentService studentService;
    private static CourseService courseService;
    private static InscriptionService inscriptionService;

	public static void main(String[] args) {


		try {

			Injector injector = Guice.createInjector(new PersistenceModule());
			EntityManager em = injector.getInstance(EntityManager.class);
			// Les 3 services: StudentService, CourseService et InscriptionService
            studentService = new StudentServiceImpl(em);
            courseService = new CourseServiceImpl(em);
            inscriptionService = new InscriptionServiceImpl(em);

            //Examples
			//---------------------------------------Student (with the courses list)-----------------------------------------------------
			String query1 = "SELECT * FROM etudiant WHERE etudiantid = 2";
			System.out.println(studentService.getStudentBySqlQuery(query1));//1

			//String query2 = "SELECT * FROM etudiant WHERE age < 23";
			//print(studentService.getListStudentBySqlQuery(query2));//2

			//print(studentService.getStudentsList());//3

			//Student s1 = studentService.getStudentByFirstName("sarah");//4
			//System.out.println(s1);

			//Student s2 = studentService.getStudentById(1);//5
			//System.out.println(s2);

			//int studentId = studentService.insertStudent(s3);//6

			//-----------------------------------------Course-------------------------------------------------------
			//print(courseService.getCourseList());//1

			//Course c1 = courseService.getCourseBySigle("INF1013");//2
			//System.out.println(c1);

			//Course c2 = courseService.getCourseById(1);//3
			//System.out.println(c2);

			//Course c3 = new Course("Math 2", "INF1018", "Math 2");//4
			//int courseId = courseService.insertCourse(c3);

			//-----------------------------------------Inscription-------------------------------------------------------
			//Inscription inscription = new Inscription(studentId, courseId, c1, s1);//1
			//inscriptionService.insertInscription(inscription);

			//inscriptionService.insertInscription(s1, c1);//2

			//print(inscriptionService.getAllInscription());//3

			//System.out.println(inscriptionService.getInscriptionByStudentAndByCourse(s1, c1));//4

			//insert multiple courses (exists in the database) with one student (exist in the database) //5
			/*Course c4 = courseService.getCourseBySigle("INF1035");
			Course c5 = courseService.getCourseBySigle("INF1018");
			List<Inscription> inscriptionList = new ArrayList<>();
			inscriptionList.add(new Inscription(s1.getStudentId(), c4.getCourseId(), c4, s1));
			inscriptionList.add(new Inscription(s1.getStudentId(), c5.getCourseId(), c5, s1));
			inscriptionService.insertInscriptionMany(s1, inscriptionList);*/

			//Create inscription(plus course and student) with setter student.setInscription //6
			/*Course c4 = courseService.getCourseBySigle("MAP6004");
			Student s3 = studentService.getStudentByFirstName("amiraaaaaaaaa");
			List<Inscription> inscription = new ArrayList<>();
			Inscription i = new Inscription(s3.getStudentId(), c4.getCourseId(), c4, s3);
			inscription.add(i);
			s3.setInscriptions(inscription);
			studentService.insertStudent(s3);*/



		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static void print(List<?> list) {
		list.forEach(System.out::println);
	}

}
