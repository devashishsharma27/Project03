package in.co.sunrays.proj3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.proj3.util.ServletUtility;

/**
 * Welcome functionality Controller. Performs operation for Show Welcome page
 * 
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */
@WebServlet(name = "WelcomeCtl", urlPatterns = "/WelcomeCtl")
public class WelcomeCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(WelcomeCtl.class);

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		System.out.println("Welcome Ctl do get started");
		log.debug("Welcome Ctl do get started");
		ServletUtility.forward(getView(), req, resp);
		log.debug("Welcome Ctl do get started");
	}

	protected String getView() {
		return ORSView.WELCOME_VIEW;
	}
}
