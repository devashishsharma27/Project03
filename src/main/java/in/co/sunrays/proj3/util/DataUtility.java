package in.co.sunrays.proj3.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtility {
	/**
	 * Application Date Format
	 */
	private static final String APP_DATE_FORMAT = "dd/MM/yyyy";
	private static final String APP_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
	/**
	 * Date formatter
	 */
	static final SimpleDateFormat dateFormat = new SimpleDateFormat(APP_DATE_FORMAT);
	 static final SimpleDateFormat timeFormat = new SimpleDateFormat(APP_TIME_FORMAT);
	/**
	 * Trims and trailing and leading spaces of a String
	 * 
	 * @param value
	 * @return
	 */
	public static String getString(String value) {
		if (DataValidator.isNotNull(value))
			return value.trim();
		else
			return value;

	}
	/**
	 * Converts Object into String
	 * 
	 * @param val
	 * @return: string
	 */
	public static String getStringData(Object val) {
		if (val != null) {
			return val.toString();
		} else {
			return "";
		}
	}
	/**
	 * Converts String into Integer
	 * 
	 * @param val
	 * @return: int
	 */
	public static int getInt(String val) {
		
		
		if (DataValidator.isInteger(val))
		{
			return Integer.parseInt(val);
		}else
		{
			return 0;
		}
	}
	/**
	 * Converts Long into Integer
	 * 
	 * @param val
	 * @return: long
	 */
	public static long getLong(String val) {
		if (DataValidator.isLong(val))
			{
			try {
			System.out.println("val....."+val);
			return Long.parseLong(val);
			}catch(Exception e)
			{
				
				e.printStackTrace();
				return 0;
			}
			}

         return 0;
		
	}
	/**
	 * Converts String into Date
	 * 
	 * @param val
	 * @return
	 */
	public static Date getDate(String val) {
		Date date = null;
		try {
			date = dateFormat.parse(val);
		} catch (Exception e) {

		}
		return date;
	}
	/**
	 * Converts Date into String
	 * @param date
	 * @return: string
	 */
	public static String getDateString(Date date) {
		try {
			return dateFormat.format(date);
		} catch (Exception e) {
		}
		return "";
	}
	/**
	 * Gets date after n number of days
	 * @param date
	 * @param day
	 * @return: date
	 */
	public static Date getDate(Date date, int day) {
		return null;
	}
	/**
	 * Converts String into Time
	 * @param val
	 * @return: timestamp
	 */
	public static Timestamp getTimestamp(String val) {

		Timestamp timeStamp = null;
		try {
			timeStamp = new Timestamp((timeFormat.parse(val)).getTime());
		} catch (Exception e) {
			return null;
		}
		return timeStamp;
	}

	/**
	 * Converts long value to timestamp
	 * @param l
	 * @return
	 */
	public static Timestamp getTimestamp(long l) {

		Timestamp timeStamp = null;
		try {
			timeStamp = new Timestamp(l);
		} catch (Exception e) {
			return null;
		}
		return timeStamp;
	}

	/**
	 * return the current timestamp
	 * @return
	 */
	public static Timestamp getCurrentTimestamp() {
		Timestamp timeStamp = null;
		try {
			timeStamp = new Timestamp(new Date().getTime());
		} catch (Exception e) {
		}
		return timeStamp;

	}

	public static long getTimestamp(Timestamp tm) {
		try {
			return tm.getTime();
		} catch (Exception e) {
			return 0;
		}
	}


}
