package in.co.sunrays.proj3.model;

import java.util.List;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.exception.DataBaseException;
import in.co.sunrays.proj3.exception.DuplicateRecordException;
import in.co.sunrays.proj3.exception.RecordNotFoundException;
import in.co.sunrays.project3.dto.TimetableDTO;

/**
 * Data Access Object of TimeTable
 *
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */
public interface TimeTableModelInt {

	/**
	 * Add a TimeTable
	 *
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException : throws when user already exists
	 * @throws RecordNotFoundException
	 */
	public long add(TimetableDTO dto) throws ApplicationException, DuplicateRecordException, RecordNotFoundException;

	/**
	 * Find TimeTable by PK
	 *
	 * @param pk : get parameter
	 * @return dto
	 * @throws ApplicationException
	 */
	public TimetableDTO findByPK(long pk) throws ApplicationException;

	/**
	 * Update a Timetable
	 *
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException : if updated user record is already exist
	 */
	public void update(TimetableDTO dto) throws ApplicationException, DuplicateRecordException, RecordNotFoundException;

	/**
	 * Delete a TimeTable
	 *
	 * @param dto
	 * @throws ApplicationException
	 */
	public void delete(TimetableDTO dto) throws ApplicationException;

	/**
	 * Get List of TimeTable
	 *
	 * @return list : List of Timetable
	 * @throws DatabaseException
	 */
	public List list() throws ApplicationException;

	/**
	 * Get List of Timetable with pagination
	 *
	 * @return list : List of TimeTable
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @throws ApplicationException
	 */
	public List list(int pageNo, int PageSize) throws ApplicationException;

	/**
	 * Search TimeTable with pagination
	 *
	 * @return list : List of TimeTable
	 * @param dto      : Search Parameters
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @throws ApplicationException
	 */
	public List search(TimetableDTO dto, int pageNo, int pageSize) throws ApplicationException;

	/**
	 * Search TimeTable
	 *
	 * @return list : List of TimeTable
	 * @param dto : Search Parameters
	 * @throws ApplicationException
	 */
	public List search(TimetableDTO dto) throws ApplicationException;

}
