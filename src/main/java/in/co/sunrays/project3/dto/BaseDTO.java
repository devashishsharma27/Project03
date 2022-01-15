package in.co.sunrays.project3.dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * parent class of all DTO's in application. It contains generic attributes.
 * @author Facade
 *
 */
public abstract class BaseDTO implements Serializable,Comparable<BaseDTO>,DropDownList{
  
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Non-Business Primary key
	 */
	protected long id;
	/**
     * Contains USER ID who created this database record
     */
	private String createdBy;
	/**
     * Contains USER ID who modified this database record
     */
	private String modifiedBy;
	/**
     * Contains Created Timestamp of database record
     */
	private Timestamp createdDateTime;
	/**
     * Contains modified Timestamp of database record
     */
	private Timestamp modifiedDateTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public Timestamp getModifiedDateTime() {
		return modifiedDateTime;
	}
	public void setModifiedDateTime(Timestamp modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}
	/*public int compareTo(BaseDTO next) {
        return getValue().compareTo(next.getValue());
    }*/
	
}
