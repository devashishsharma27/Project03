package in.co.sunrays.project3.dto;

import java.util.Date;

/**
 * Timetable DTO class
 * @author Facade
 *
 */
public class TimetableDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	/**
	 * Course ID
	 */
	private long courseId;
	/**
	 * Course Name
	 */
	private String courseName;
	/**
	 * Subject Name
	 */
	private String subjectName;
	/**
	 * Subject ID
	 */
	private long subjectId;
	/**
	 * Semester
	 */
	private String semester;
	/**
	 * Timetable Descripton
	 */
	private String description;
	/**
	 * Timetable Exam Date
	 */
	private Date examDate;
	/**
	 * Exam time
	 */
	private String examTime;

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

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}


	

	public long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getExamDate() {
		return examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	public String getExamTime() {
		return examTime;
	}

	public void setExamTime(String examTime) {
		this.examTime = examTime;
	}

	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public int compareTo(BaseDTO o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
