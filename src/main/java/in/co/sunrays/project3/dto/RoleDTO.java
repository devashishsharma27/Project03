package in.co.sunrays.project3.dto;

/**
 * Role DTO class
 * 
 * @author Facade
 *
 */
public class RoleDTO extends BaseDTO{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	public static final long STUDENT = 2;
	public static final long ADMIN=1;
	public static final long COLLEGE=3; 
	public static final long FACULTY=4; 
	/**
	 * Role name
	 */
	private String name;
	/**
	 * Role description
	 */
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
		return name;
	}

	public int compareTo(BaseDTO o) {
		return 0;
	}
}
