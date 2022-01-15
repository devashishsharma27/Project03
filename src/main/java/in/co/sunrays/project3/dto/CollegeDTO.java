package in.co.sunrays.project3.dto;

/**
 * College DTO class
 * @author Facade
 *
 */
public class CollegeDTO extends BaseDTO{
	
	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of College
	 */
	private String name;
	
	/**
	 * Address of college
	 */
	private String address;
	
	/**
	 * State of college
	 */
	private String state;
	
	/**
	 * city of college
	 */
	private String city;
	
	/**
	 * Phone Number of college
	 */
	private String phoneNo;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getKey() {
		
		return id+"";
	}
	public String getValue() {
		return name;
	}
	public int compareTo(BaseDTO o) {
		
		return 0;
	}
	

}
