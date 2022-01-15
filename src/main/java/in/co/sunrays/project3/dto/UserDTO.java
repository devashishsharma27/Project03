package in.co.sunrays.project3.dto;

import java.sql.Timestamp;
import java.util.Date;

/**
 * User DTO class
 * 
 * @author Facade
 *
 */
public class UserDTO extends BaseDTO{
	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Lock Active constant for User
	 */
	public static final String ACTIVE = "Active";
	/**
	 * Lock Inactive constant for User
	 */
	public static final String INACTIVE = "Inactive";
	/**
	 * First name of user
	 */
	private String firstName;
	/**
	 * Last name of user
	 */
	private String lastName;
	/**
	 * Login Id of user
	 */
	private String loginId;
	/**
	 * password of user
	 */
	private String password;
	/**
	 * Confirm Password of User
	 */
	private String confirmPassword;
	/**
	 * Date of Birth of user
	 */
	private Date dob;
	/**
	 * Mobile Number of user
	 */
	private String mobileNo;
	/**
	 * gender of user
	 */
	private String gender;

	/**
	 * Role Id of user
	 */
	private long roleId;
    private String roleName;
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * Number of unsuccessful login attempt
	 */
	private int unsuccessfullLogin;
	/**
	 * Last login Timestamp of user
	 */
	private Timestamp lastLogin;
	/**
	 * User Lock
	 */
	private String userLock = INACTIVE;
	/**
	 * IP Address of User from where User was registred.
	 */
	private String registeredIP;
	/**
	 * IP Address of User of his last login
	 */
	private String lastLoginIP;

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

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public int getUnsuccessfullLogin() {
		return unsuccessfullLogin;
	}

	public void setUnsuccessfullLogin(int unsuccessfullLogin) {
		this.unsuccessfullLogin = unsuccessfullLogin;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getUserLock() {
		return userLock;
	}

	public void setUserLock(String userLock) {
		this.userLock = userLock;
	}

	public String getRegisteredIP() {
		return registeredIP;
	}

	public void setRegisteredIP(String registeredIP) {
		this.registeredIP = registeredIP;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getLastLoginIP() {
		return lastLoginIP;
	}

	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
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
