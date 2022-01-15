package in.co.sunrays.proj3.exception;

/**
 * RecordNotFoundException thrown when a record not found occurred
 *
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 *
 */
public class RecordNotFoundException extends Exception {

	public RecordNotFoundException() {
		super();
	}

	/**
	 * @param msg error message
	 */
	public RecordNotFoundException(String msg) {
		super(msg);
	}

}
