package in.co.sunrays.proj3.controller;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.exception.RecordNotFoundException;
import in.co.sunrays.proj3.model.ModelFactory;
import in.co.sunrays.proj3.model.UserModelInt;
import in.co.sunrays.proj3.util.DataUtility;
import in.co.sunrays.proj3.util.DataValidator;
import in.co.sunrays.proj3.util.PropertyReader;
import in.co.sunrays.proj3.util.ServletUtility;
import in.co.sunrays.project3.dto.BaseDTO;
import in.co.sunrays.project3.dto.UserDTO;

/**
 * Forget Password functionality Controller. Performs operation for Forget
 * Password
 * 
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */

@WebServlet(name = "ForgetPasswordCtl", urlPatterns = "/ForgetPasswordCtl")
public class ForgetPasswordCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(ForgetPasswordCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {

		log.debug("ForgetPasswordCtl Method validate Started");

		boolean pass = true;

		String login = request.getParameter("emailId");

		if (DataValidator.isNull(login)) {
			request.setAttribute("emailId", PropertyReader.getValue("error.require", "Email Id"));
			pass = false;
		} else if (!DataValidator.isEmail(login)) {
			request.setAttribute("emailId", PropertyReader.getValue("error.email", "emailId "));
			pass = false;
		}
		log.debug("ForgetPasswordCtl Method validate Ended");

		return pass;
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		log.debug("ForgetPasswordCtl Method populatebean Started");

		UserDTO dto = new UserDTO();

		dto.setLoginId(DataUtility.getString(request.getParameter("emailId")));

		log.debug("ForgetPasswordCtl Method populatebean Ended");

		return dto;
	}

	/**
	 * View Logic
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("ForgetPasswordCtl Method doGet Started");

		ServletUtility.forward(getView(), request, response);

		log.debug("ForgetPasswordCtl Method doGet End");
	}

	/**
	 * Submit Logic
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.debug("ForgetPasswordCtl Method doPost Started");

		String op = DataUtility.getString(req.getParameter("operation"));

		UserDTO dto = (UserDTO) populateDTO(req);
		System.out.println("in fctl");
		// get model
		UserModelInt model = ModelFactory.getInstance().getUserModel();

		if (OP_GO.equalsIgnoreCase(op)) {
			System.out.println("in g");
			try {

				model.forgetPassword(dto.getLoginId());

				ServletUtility.setSuccessMessage("Password has been sent to your email id.", req);
			} catch (RecordNotFoundException e) {
				System.out.println("in r");
				ServletUtility.setErrorMessage(e.getMessage(), req);
				log.error(e);
			} catch (ApplicationException e) {
				System.out.println("in r1");
				log.error(e);
				ServletUtility.handleException(e, req, resp);
				return;
			} catch (MessagingException e) {
				System.out.println("in 2");
				System.out.println(e.getMessage());
				req.setAttribute("msg", e.getMessage());
				ServletUtility.forward(ORSView.ERROR_VIEW, req, resp);
				return;
			}
			ServletUtility.forward(getView(), req, resp);
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.FORGET_PASSWORD_CTL, req, resp);
			return;
		}
		log.debug("ForgetPasswordCtl Method doPost Ended");
	}

	protected String getView() {

		return ORSView.FORGET_PASSWORD_VIEW;
	}

}
