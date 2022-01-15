package in.co.sunrays.proj3.util;

import java.util.Date;

public class DataValidator {
	
	public static boolean isNull(String val)
	{
		if(val==null||val.trim().length()==0)
		{
			return true;
		}
		else
		{
		return false;
		}
	}
	public static boolean isNotNull(String val)
	{
		return !isNull(val);
	}
    public static boolean isInteger(String val)
    {
    	System.out.println("in d1 "+val+"d2");
    	if(isNotNull(val))
    	{
    		try{
    			
    		Integer.parseInt(val);
    		
    		return true;
    		}catch(NumberFormatException e)
    		{
    			e.printStackTrace();
    			return false;
    		}
    	}
    	else
    	{
		return false;
    	}
    }
    public static boolean isLong(String val)
    {
    	if(isNotNull(val))
    	{
    		try{
    		Long.parseLong(val);
    		return true;
    		}catch(NumberFormatException e)
    		{
    			return false;
    		}
    	}
    	else
    	{
		return false;
    	}
   }
    public static  boolean isPassword(String val) {

		String passreg = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

		if (isNotNull(val)) {
			try {
				return val.matches(passreg);
			} catch (NumberFormatException e) {
				return false;
			}

		} else {
			return false;
		}
	}
    public static boolean isPhoneNo(String val)
    {
    	String regx="^[6-9][0-9]{9}$";
    	
    	if(isNotNull(val) && isPhoneNoLength(val))
    	{
    		
    		
    		try{
    			return val.matches(regx);
    		}
    		catch(NumberFormatException e)
    		{
    			return false;
    		}
    		
    		
    	}
    	else
    	{
		return false;
    	}
    }
    public static boolean isPhoneNoLength(String val)
    {
    	if(isNotNull(val) && val.trim().length()==10)
    	{
    		return true;
    	}
    	else
    	{
		return false;
    	}
    }
    public static  boolean isEmail(String email){
		String reg="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		if(isNotNull(email)){
			try{
				return email.matches(reg);
			}catch(Exception e){
				return false;
			}
		}
		else{
			return false;
		}
	}
   
    public static boolean isDate(String date)
    {
    	if(isNotNull(date))
    	{
    		try
    		{
    		Date date1=	DataUtility.getDate(date);
    		if(date1!=null)
    		{
    			return true;
    		}
    		else
    		{
    			return false;
    		}
    		}
    		catch(Exception e)
    		{
    			return false;
    		}
    	}
		return false;
    	
    }
    
    public static boolean isFname(String val)
    {
    	if(isNotNull(val))
    	{
    		String firstChar = val.substring(0, 1);

    		if (firstChar.equals(firstChar.toUpperCase())) {
    		   return true;
    		}
    		else
    		{
    			return false;
    		}
    	}
		return false;
    	
    }
    
    public static boolean isRollNo(String val)
    {
    	String rollreg="((?:[0-9]|[A-Za-z])+[0-9])";
    	if(isNotNull(val)) {
    	try{
    		return val.matches(rollreg);
    	}
    	catch(Exception e) {
    		return false;
    	}
    	}else {
    		return false;
    	}
    }
  
}
