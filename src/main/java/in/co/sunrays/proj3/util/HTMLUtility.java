package in.co.sunrays.proj3.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;


import in.co.sunrays.project3.dto.DropDownList;
/**
 * HTML Utility class to produce HTML contents like Dropdown List.
 *
 * @author Ravi Rathore
 * @version 1.0
 * @Copyright (c) RaviOS
 *
 */
public class HTMLUtility {
	/**
	 * Create HTML SELECT list from MAP paramters values
	 * 
	 * @param name
	 * @param selectedVal
	 * @param map
	 * @return
	 */

	public static String getList(String name, String selectedVal, HashMap<String, String> map) {

		StringBuffer sb = new StringBuffer("<select style='width:"+173+"px' class='form-control' name='" + name + "'>");
    
    
		Set<String> keys = map.keySet();
          
		String val = null;
		
		sb.append("<option selected value=''>-------------Select------------</option>");
		for (String key : keys) {
			val = map.get(key);
			if (key.trim().equals(selectedVal)) {
				
				sb.append("<option selected value='" + key + "'>" + val + "</option>");
			} else {
				
				sb.append("<option value='" + key + "'>" + val + "</option>");
				
			}
		}
		sb.append("</select>");
		return sb.toString();
	}
	 /**
     * Create HTML SELECT List from List parameter
     *
     * @param name
     * @param selectedVal
     * @param list
     * @return
     */
	public static String getList(String name, String selectedVal, List list) {

		 Collections.sort(list);

	 List<DropDownList> dd = (List<DropDownList>) list;

		StringBuffer sb = new StringBuffer("<select style='width:"+173+"px' class='form-control' name='"+name+"'>");
		sb.append("<option  selected value=''>------------Select-------------</option>");
		String key = null;
		String val = null;

	

			 for (DropDownList obj : dd) {
			key = obj.getKey();
			val = obj.getValue();

			if (key.trim().equals(selectedVal)) {
				sb.append("<option selected value='"+key.trim()+"'>" + val + "</option>");
			} else {
				sb.append("<option value='"+key.trim()+"'>" + val + "</option>");
			}
		}
		sb.append("</select>");
		return sb.toString();
	}
	public static String getList(String name, String selectedVal,HashMap<String,String> map,boolean select)
	{
		StringBuffer sb=new StringBuffer("<select style='width:"+165+"px' class='form-control' name='"+ name+"'>");
		Set<String> Keys=map.keySet();
		String val=null;
		if(select)
		{
			sb.append("<option selected value=''> --Select-- </option>");
		}
		sb.append("<option style='width:100px; height:30px;' selected value=''>-------select-------</option>");
		for(String key:Keys)
		{
			val=map.get(key);
			if(val.trim().equals(selectedVal))
			{
				sb.append("<option selected value='"+ key +"'>" + val
                        + "</option>");
			}
			else
			{
				sb.append("<option value='"+ key +"'>" + val + "</option>");
			}
		}
		sb.append("</select>");
		return sb.toString();
		
	}
	public static String getInputErrorMessages(HttpServletRequest req)
	{
		Enumeration<String> e=req.getAttributeNames();
		StringBuffer sb=new StringBuffer("<UL>");
		String name;
		while(e.hasMoreElements())
		{
			name=e.nextElement();
			if(name.startsWith("error"))
			{
				sb.append("<LI class='error'>" + req.getAttribute(name)
                + "</LI>");
			}
		}
		sb.append("</UL>");
		return sb.toString();
	}
	 public static String getErrorMessage(HttpServletRequest request) {
	        String msg = ServletUtility.getErrorMessage(request);
	        if (!DataValidator.isNull(msg)) {
	            msg = "<p class='st-error-header'>" + msg + "</p>";
	        }
	        return msg;
	    }
	 public static String getSuccessMessage(HttpServletRequest request) {
	        String msg = ServletUtility.getSuccessMessage(request);
	        if (!DataValidator.isNull(msg)) {
	            msg = "<p class='st-success-header'>" + msg + "</p>";
	        }
	        return msg;
	    }
	 public static String getSubmitButton(String label, boolean access,
	            HttpServletRequest request) {

	        String button = "";

	        if (access) {
	            button = "<input type='submit' name='operation'    value='" + label
	                    + "' >";
	        }
	        return button;
	    }
	 public static String getCommonFields(HttpServletRequest request) {

	        ///BaseModel model = ServletUtility.getModel(request);

	        StringBuffer sb = new StringBuffer();

	        //sb.append("<input type='hidden' name='id' value=" + model.getId() + ">");
	        /*
	         * sb.append("<input type='hidden' name='createdBy' value=" +
	         * DataUtility.getString(model.getCreatedBy()) + ">");
	         * sb.append("<input type='hidden' name='modifiedBy' value=" +
	         * DataUtility.getString(model.getModifiedBy()) + ">");
	         * sb.append("<input type='hidden' name='createdDatetime' value=" +
	         * DataUtility.getTimestamp(model.getCreatedDatetime()) + ">");
	         * sb.append("<input type='hidden' name='modifiedDatetime' value=" +
	         * DataUtility.getTimestamp(model.getModifiedDatetime()) + ">");
	         */
	        return sb.toString();
	    }

}
