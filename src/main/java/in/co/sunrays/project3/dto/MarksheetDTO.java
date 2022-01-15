package in.co.sunrays.project3.dto;

/**
 * Marksheet DTO class
 * 
 * @author Facade
 *
 */
public class MarksheetDTO extends BaseDTO{
	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Roll number of student
	 */
	private String rollNo;
	/**
	 * Name of student
	 */
	private String name;

	/**
	 * Id of student
	 */
	private long studentId;
	/**
	 * Student marks of Maths subject
	 */
	private int maths;
	/**
	 * Student marks of Physics subject
	 */
	private int physics;
	/**
	 * Student marks of chemistry subject
	 */
	private int chemistry;

	/**
	 * accessors
	 */
	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public int getMaths() {
		return maths;
	}

	public void setMaths(int maths) {
		this.maths = maths;
	}

	public int getPhysics() {
		return physics;
	}

	public void setPhysics(int physics) {
		this.physics = physics;
	}

	public int getChemistry() {
		return chemistry;
	}

	public void setChemistry(int chemistry) {
		this.chemistry = chemistry;
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
