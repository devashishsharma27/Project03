package in.co.sunrays.proj3.model;

import java.util.List;

import javax.mail.MessagingException;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.exception.DuplicateRecordException;
import in.co.sunrays.proj3.exception.RecordNotFoundException;
import in.co.sunrays.project3.dto.UserDTO;

/**
 * Data Access Object of Users
 *
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */
public interface UserModelInt {

	/**
	 * Add a user
	 *
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException : throws when user already exists
	 */
	public long add(UserDTO dto) throws DuplicateRecordException, ApplicationException;

	/**
	 * Find user by login
	 *
	 * @param login : get parameter
	 * @return dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public UserDTO findByLogin(String login) throws ApplicationException;

	/**
	 * Find user by PK
	 *
	 * @param pk : get parameter
	 * @return dto
	 * @throws ApplicationException
	 */
	public UserDTO findByPK(long pk) throws ApplicationException;

	/**
	 * Delete a user
	 *
	 * @param dto
	 * @throws ApplicationException
	 */
	public void delete(UserDTO dto) throws ApplicationException;

	/**
	 * Update a User
	 *
	 * @param dto
	 * @throws ApplicationException
	 * @throws DuplicateRecordException : if updated user record is already exist
	 */
	public void update(UserDTO dto) throws ApplicationException;

	/**
	 * User Authentication
	 *
	 * @return dto : Contains User's information
	 * @param login    : User Login
	 * @param password : User Password
	 * @throws ApplicationException
	 */
	public UserDTO authenticate(String login, String pass) throws ApplicationException;

	/**
	 * Search Users with pagination
	 *
	 * @return list : List of Users
	 * @param dto      : Search Parameters
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @throws ApplicationException
	 */
	public List search(UserDTO dto, int pageNo, int pageSize) throws ApplicationException;

	/**
	 * Search Users
	 *
	 * @return list : List of Users
	 * @param dto : Search Parameters
	 * @throws ApplicationException
	 */
	public List search(UserDTO dto) throws ApplicationException;

	/**
	 * Get List of Users with pagination
	 *
	 * @return list : List of Users
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @throws ApplicationException
	 */
	public List list(int pageNo, int pageSize) throws ApplicationException;

	/**
	 * Get List of Users
	 *
	 * @return list : List of Users
	 * @throws DatabaseException
	 */
	public List list() throws ApplicationException;

	/**
	 * Register a User
	 *
	 * @param dto
	 * @return
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public long registerUser(UserDTO bean) throws ApplicationException, DuplicateRecordException, MessagingException;

	/**
	 * Change Password By pk
	 *
	 * @param pk ,oldPassword,newPassword : get parameter
	 * @return dto
	 * @throws ApplicationException
	 * @throws RecordNotFoundException
	 */
	public boolean changePassword(long id, String oldPassword, String newPassword)
			throws RecordNotFoundException, ApplicationException, MessagingException;

	/**
	 * forget password
	 */
	public boolean forgetPassword(String login)
			throws ApplicationException, RecordNotFoundException, MessagingException;

}
