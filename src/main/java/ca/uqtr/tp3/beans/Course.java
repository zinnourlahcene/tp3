package ca.uqtr.tp3.beans;

import java.util.List;

import ca.uqtr.tp3.persistence.annotation.Column;
import ca.uqtr.tp3.persistence.annotation.Ignore;
import ca.uqtr.tp3.persistence.annotation.Join;
import ca.uqtr.tp3.persistence.annotation.PrimaryKey;
import ca.uqtr.tp3.persistence.annotation.Table;

@Table(name="cours")
public class Course {
	@PrimaryKey
	@Column(name="coursid")
	private Integer courseId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="sigle")
	private String sigle;
	
	@Column(name="description")
	private String description;
	
	@Ignore
	@Join
	private List<Inscription> inscriptions;
	
	public Course() {}
	
	/**
	 * Initializes a newly created Course object so that it represents a Course. 
	 *
	 * @param name
	 * @param sigle
	 * @param description
	 */
	public Course(String name, String sigle, String description) {
		super();
		this.name = name;
		this.sigle = sigle;
		this.description = description;
	}
	/**
	 * @return the courseId
	 */
	public Integer getCourseId() {
		return courseId;
	}
	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the sigle
	 */
	public String getSigle() {
		return sigle;
	}
	/**
	 * @param sigle the sigle to set
	 */
	public void setSigle(String sigle) {
		this.sigle = sigle;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the inscriptions
	 */
	public List<Inscription> getInscriptions() {
		return inscriptions;
	}
	/**
	 * @param inscriptions the inscriptions to set
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
		result = prime * result + courseId;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((sigle == null) ? 0 : sigle.hashCode());
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
		Course other = (Course) obj;
		if (courseId != other.courseId) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (sigle == null) {
			if (other.sigle != null) {
				return false;
			}
		} else if (!sigle.equals(other.sigle)) {
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
		builder.append("\n • courseId: ");
		builder.append(courseId);
		builder.append("\n • name: ");
		builder.append(name);
		builder.append("\n • sigle: ");
		builder.append(sigle);
		builder.append("\n • description: \n");
		builder.append(description+ "...\n");
		
		
		if(inscriptions != null) {
			builder.append("\n • Étudiants inscrient: ");
			builder.append(String.format("\n\n| %-100s", " ").replaceAll(" ", "-"));
			builder.append(String.format("\n| %-20s%-20s%-20s%-20s", "etudianid", "fname", "lname", "age"));
			builder.append(String.format("\n %-100s", " ").replaceAll(" ", "-"));
			
			inscriptions.subList(0, Math.min(inscriptions.size(), maxLen))
			.stream()
			.filter(r -> r != null)
			.forEach(r -> builder.append(String.format("\n| %-20s%-20s%-20s%-20d", 
					r.getStudent().getStudentId(),
					r.getStudent().getFName(),
					r.getStudent().getLName(),
					r.getStudent().getAge())));
			
			if(inscriptions.size() == 0) {
				builder.append("\nPas d'étudiant inscrit dans ce cours!");
			}
			
			builder.append(String.format("\n| %-100s", " ").replaceAll(" ", "-"));
		}
		
		builder.append("");
		return builder.toString();
	}
}
