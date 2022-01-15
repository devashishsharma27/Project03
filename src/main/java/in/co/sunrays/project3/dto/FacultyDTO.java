package in.co.sunrays.project3.dto;

import java.util.Date;

/**
 * Faculty DTO class
 * @author Facade
 *
 */
public class FacultyDTO extends BaseDTO {

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * College ID
	 */
	private long collegeId;
	/**
	 * College Name
	 */
	private String collegeName;
	/**
	 * Subject ID
	 */
	private long subjectId;
	/**
	 * Subject Name
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
	 * Faculty's First NAme
	 */
	private String firstName;
	/**
	 * Faculty's Last Name
	 */
	private String lastName;
	/**
	 * Faculty's  Gender
	 */
	private String gender;
	/**
	 * Faculty's date of birth
	 */
	private Date dob;
	/**
	 * Faculty's email Id
	 */
	private String emailId;
	/**
	 * Faculty's mobile no.
	 */
	private String mobileNo;
    private String qualification;
    public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public Date getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	private Date joiningDate;
	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
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

	public long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getKey() {
	  return id+"";
	}

	public String getValue() {
		return firstName+" "+lastName;
	}

	public int compareTo(BaseDTO o) {
		return 0;
	}

}
