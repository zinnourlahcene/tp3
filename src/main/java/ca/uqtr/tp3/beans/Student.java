package ca.uqtr.tp3.beans;

import java.util.ArrayList;
import java.util.List;

import ca.uqtr.tp3.persistence.annotation.Column;
import ca.uqtr.tp3.persistence.annotation.Join;
import ca.uqtr.tp3.persistence.annotation.PrimaryKey;
import ca.uqtr.tp3.persistence.annotation.Table;

@Table(name="etudiant")
public class Student {
	@PrimaryKey
	@Column(name="etudiantid")
	private Integer studentId;
	@Column(name="fname")
	private String fName;
	@Column(name="lname")
	private String lName;
	@Column
	private Integer age;
	@Join
	private List<Inscription> inscriptions;
	
	public Student() {}
	
	/**
	 * Initializes a newly created Student object so that it represents a Student. 
	 *
	 * @param fName
	 * @param age
	 */
	public Student(String fName, String lNane, int age) {
		super();
		this.fName = fName;
		this.lName = lNane;
		this.age = age;
		this.inscriptions = new ArrayList<Inscription>();
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
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	/**
	 * @return the fName
	 */
	public String getFName() {
		return fName;
	}
	/**
	 * @param fName the fName to set
	 */
	public void setFName(String fName) {
		this.fName = fName;
	}
	/**
	 * @return the lName
	 */
	public String getLName() {
		return lName;
	}
	/**
	 * @param lName the lName to set
	 */
	public void setLName(String lName) {
		this.lName = lName;
	}
	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
	/**
	 * @return the List<Inscription>
	 */
	public List<Inscription> getInscriptions() {
		return inscriptions;
	}
	/**
	 * @param inscriptions
	 */
	public void setInscriptions(List<Inscription> inscriptions) {
		this.inscriptions = inscriptions;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((fName == null) ? 0 : fName.hashCode());
		result = prime * result + ((lName == null) ? 0 : lName.hashCode());
		result = prime * result + studentId;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		Student other = (Student) obj;
		if (age != other.age) {
			return false;
		}
		if (fName == null) {
			if (other.fName != null) {
				return false;
			}
		} else if (!fName.equals(other.fName)) {
			return false;
		}
		if (lName == null) {
			if (other.lName != null) {
				return false;
			}
		} else if (!lName.equals(other.lName)) {
			return false;
		}
		if (studentId != other.studentId) {
			return false;
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 5;
		StringBuilder builder = new StringBuilder();
		builder.append("\n • studentId: ");
		builder.append(studentId);
		builder.append("\n • fName: ");
		builder.append(fName);
		builder.append("\n • lName: ");
		builder.append(lName);
		builder.append("\n • age: ");
		builder.append(age);
		
		if(inscriptions != null) {
			builder.append("\n • Cours inscrient: ");
			builder.append(String.format("\n\n| %-100s", " ").replaceAll(" ", "-"));
			builder.append(String.format("\n| %-20s%-20s%-40s%-20s", "coursid", "sigle", "name", "description"));
			builder.append(String.format("\n| %-100s", " ").replaceAll(" ", "-"));
			
			inscriptions.subList(0, Math.min(inscriptions.size(), maxLen))
			.forEach(i -> builder.append(String.format("\n| %-20s%-20s%-40s%-20s |", 
					i.getCourse().getCourseId(),
					i.getCourse().getSigle(),
					i.getCourse().getName(),
					i.getCourse().getDescription().substring(0,(i.getCourse().getDescription().length()*50)/100) + "...")));
			if(inscriptions.size() == 0) {				
				builder.append("\n|Aucune inscription!");
			}
			builder.append(String.format("\n| %-100s", " ").replaceAll(" ", "-"));
		}		
		
		return builder.toString();
	}
	
	
}
