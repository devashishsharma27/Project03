package in.co.sunrays.proj3.model;

import java.util.List;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.exception.DuplicateRecordException;
import in.co.sunrays.project3.dto.FacultyDTO;

/**
 * Data Access Object of Faculty
 *
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */
public interface FacultyModelInt {
	/**
	 * Add a faculty
	 *
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException : throws when faculty already exists
	 */
	public long add(FacultyDTO dto) throws DuplicateRecordException, ApplicationException;

	/**
	 * Delete a faculty
	 *
	 * @param dto
	 * @throws ApplicationException
	 */
	public void delete(FacultyDTO dto) throws ApplicationException;

	/**
	 * Update a faculty
	 *
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException : if updated faculty record is already exist
	 */
	public void update(FacultyDTO dto) throws ApplicationException, DuplicateRecordException;

	/**
	 * Find faculty by PK
	 *
	 * @param pk : get parameter
	 * @return dto
	 * @throws ApplicationException
	 */
	public FacultyDTO findByPK(long pk) throws ApplicationException;

	/**
	 * Find faculty by login
	 *
	 * @param login : get parameter
	 * @return dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public FacultyDTO findByEmail(String Email) throws ApplicationException;

	/**
	 * Search Faculty with pagination
	 *
	 * @return list : List of Faculty
	 * @param dto      : Search Parameters
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @throws ApplicationException
	 */
	public List search(FacultyDTO dto, int pageNo, int PageSize) throws ApplicationException;

	/**
	 * Search Faculty
	 *
	 * @return list : List of Faculty
	 * @param dto : Search Parameters
	 * @throws ApplicationException
	 */
	public List search(FacultyDTO dto) throws ApplicationException;

	/**
	 * Get List of Faculty with pagination
	 *
	 * @return list : List of Faculty
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @throws ApplicationException
	 */
	public List list(int pageNo, int pageSize) throws ApplicationException;

	/**
	 * Get List of Faculty
	 *
	 * @return list : List of Faculty
	 * @throws DatabaseException
	 */
	public List list() throws ApplicationException;
}
