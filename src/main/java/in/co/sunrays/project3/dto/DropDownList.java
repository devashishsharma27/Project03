package in.co.sunrays.project3.dto;

/**
 * DropdownList interface is implemented by DTOs those are used to create drop
 * down list on HTML pages
 * @author Facade
 *
 */
public interface DropDownList {
	/**
	 * Returns key of list elements
	 * @return key
	 */
	public String getKey();

	/**
	 * Returns value of list element
	 * @return value
	 */
	public String getValue();
}
