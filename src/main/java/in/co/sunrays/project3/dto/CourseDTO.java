package in.co.sunrays.project3.dto;

/**
 * Course DTO class
 * @author Facade
 *
 */
public class CourseDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	/**
	 * Name of Course
	 */
	private String courseName;
	/**
	 * Course Duration
	 */
	private String duration;
	/**
	 * Course description
	 */
	private String description;

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
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
		return courseName;
	}

	public int compareTo(BaseDTO o) {
		return 0;
	}
}
