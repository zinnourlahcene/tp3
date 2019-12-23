package ca.uqtr.tp3.beans;

import ca.uqtr.tp3.persistence.annotation.Column;
import ca.uqtr.tp3.persistence.annotation.Ignore;
import ca.uqtr.tp3.persistence.annotation.Join;
import ca.uqtr.tp3.persistence.annotation.PrimaryKey;
import ca.uqtr.tp3.persistence.annotation.Table;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

@Table(name="inscription")
public class Inscription {
	@PrimaryKey
	@Column(name="inscriptionid")
	private int inscriptionId;
	@Column(name="etudiantid")
	private int studentId;
	@Column(name="coursid")
	private int courseId;
	@Column(name="date")
	private Timestamp date;
	@Ignore(operation="insert")
	@Join
	private Course course;
	@Ignore(operation="display")
	@Join
	private Student student;
	
	public Inscription() {}
	
	/**
	 * Initializes a newly created Registration object so that it represents a Registration. 
	 *
	 * @param studentId
	 * @param courseId
	 * @param course
	 * @param student
	 */
	public Inscription(int studentId, int courseId, Course course, Student student) {
		super();
		this.studentId = studentId;
		this.courseId = courseId;
		this.course = course;
		this.student = student;
		this.date = new Timestamp(System.currentTimeMillis());
	}
        
        /**
	 * Initializes a newly created Registration object so that it represents a Registration. 
	 *
	 * @param studentId
	 * @param courseId
	 */
	public Inscription(int studentId, int courseId) {
		super();
		this.studentId = studentId;
		this.courseId = courseId;
		this.course = new Course();
		this.student = new Student();
		this.date = new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * Initializes a newly created Registration object so that it represents a Registration.
	 * @param course
	 * @param student
	 */
	public Inscription(Course course, Student student) {
		super();
		this.course = course;
		this.student = student;
		this.date = new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * @return the inscriptionId
	 */
	public int getInscriptionId() {
		return inscriptionId;
	}
	/**
	 * @param inscriptionId the inscriptionId to set
	 */
	public void setInscriptionId(int inscriptionId) {
		this.inscriptionId = inscriptionId;
	}
	/**
	 * @return the studentId
	 */
	public int getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	/**
	 * @return the courseId
	 */
	public int getCourseId() {
		return courseId;
	}
	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	/**
	 * @return the course
	 */
	public Course getCourse() {
		return course;
	}
	/**
	 * @param course the course to set
	 */
	public void setCourse(Course course) {
		this.course = course;
	}
	/**
	 * @return the student
	 */
	public Student getStudent() {
		return student;
	}
	/**
	 * @param student the student to set
	 */
	public void setStudent(Student student) {
		this.student = student;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + courseId;
		result = prime * result + inscriptionId;
		result = prime * result + ((student == null) ? 0 : student.hashCode());
		result = prime * result + studentId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Inscription other = (Inscription) obj;
		if (course == null) {
			if (other.course != null) {
				return false;
			}
		} else if (!course.equals(other.course)) {
			return false;
		}
		if (courseId != other.courseId) {
			return false;
		}
		if (inscriptionId != other.inscriptionId) {
			return false;
		}
		if (student == null) {
			if (other.student != null) {
				return false;
			}
		} else if (!student.equals(other.student)) {
			return false;
		}
		if (studentId != other.studentId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("inscriptionId: ");
		builder.append(inscriptionId);
		builder.append(", studentId: ");
		builder.append(studentId);
		builder.append(", courseId: ");
		builder.append(courseId);
		builder.append(", course: ");
		builder.append(course);
		builder.append(", student: ");
		builder.append(student);
		return builder.toString();
	}
	
	
}
