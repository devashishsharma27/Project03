package in.co.sunrays.proj3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.proj3.util.ServletUtility;
@WebServlet(name="ErrorCtl1",urlPatterns = "/ErrorCtl1")
public class ErrorCtl1 extends BaseCtl{
	
	private static Logger log=Logger.getLogger(ErrorCtl.class);
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      
		log.debug("Error Ctl do get Method started");
		
		ServletUtility.forward(getView(), req, resp);
        
		log.debug("Error Ctl do get Method End");
	}
	
	protected String getView() {
		
		return ORSView.ERROR_VIEW1;
	}

}
