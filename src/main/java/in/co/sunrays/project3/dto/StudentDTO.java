package in.co.sunrays.project3.dto;

import java.util.Date;

/**
 * Student DTO class
 * @author Facade
 *
 */
public class StudentDTO extends BaseDTO{

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
      	
     
	/**
	 * collegeId of student
	 */
	private long collegeId;
	/**
	 * college name of student
	 */
	private String collegeName;
	
	/**
	 * student's First name
	 */
	private String firstName;
	/**
	 * student's Last name
	 */
	private String lastName;
	/**
	 * student's date of birth
	 */
	private Date dob;
	/**
	 * student's mobile number
	 */
	private String mobileNo;
	/**
	 * student's email Id
	 */
	private String email;
	private long courseId;
	private String courseName;
	
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
	public long getCollegeId() {
		return collegeId;
	}
	public void setCollegeId(long collegeId) {
		this.collegeId = collegeId;
	}
	public String getCollegeName() {
		return collegeName;
	}
	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getKey() {
		
		return id+" ";
	}
	public String getValue() {
		
		return firstName+" "+lastName;
	}
	public int compareTo(BaseDTO o) {
		return 0;
	}
}
