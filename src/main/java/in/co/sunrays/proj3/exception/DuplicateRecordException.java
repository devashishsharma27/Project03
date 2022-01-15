package in.co.sunrays.proj3.exception;

/**
 * DuplicateRecordException thrown when a duplicate record occurred
 *
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 *
 */
public class DuplicateRecordException extends Exception {

	public DuplicateRecordException() {
		super();
	}

	/**
	 * @param msg error message
	 */
	public DuplicateRecordException(String msg) {
		super(msg);
	}

}
