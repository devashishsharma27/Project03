package in.co.sunrays.project3.dto;

/**
 * Subject DTO class
 * @author Facade
 *
 */
public class SubjectDTO extends BaseDTO {

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	

    private String semester;
    
    

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	/**
	 * Subject name
	 */
	private String subjectName;
	/**
	 * Course ID
	 */
	private long courseId;
	/**
	 * Course Name
	 */
	private String courseName;
	/**
	 * Subject description
	 */
	private String description;

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKey() {
		return id+"";
	}

	public String getValue() {
		return subjectName;
	}

	public int compareTo(BaseDTO o) {
		return 0;
	}

}
