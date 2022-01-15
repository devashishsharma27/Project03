package in.co.sunrays.proj3.exception;

/**
 * DatabaseException is propogated by DAO classes when an unhandled Database
 * exception occurred
 *
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 *
 */
public class DataBaseException extends Exception {

	public DataBaseException() {
		super();
	}

	/**
	 * @param msg : Error message
	 */
	public DataBaseException(String msg) {
		super(msg);
	}

}
