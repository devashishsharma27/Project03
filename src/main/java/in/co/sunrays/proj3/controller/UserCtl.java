package in.co.sunrays.proj3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.exception.DuplicateRecordException;
import in.co.sunrays.proj3.model.ModelFactory;
import in.co.sunrays.proj3.model.RoleModelInt;
import in.co.sunrays.proj3.model.UserModelInt;
import in.co.sunrays.proj3.util.DataUtility;
import in.co.sunrays.proj3.util.DataValidator;
import in.co.sunrays.proj3.util.PropertyReader;
import in.co.sunrays.proj3.util.ServletUtility;
import in.co.sunrays.project3.dto.BaseDTO;
import in.co.sunrays.project3.dto.UserDTO;

/**
 * User functionality Controller. Performs operation for add, update and get
 * User
 *  
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */
@WebServlet(name = "UserCtl", urlPatterns = "/ctl/UserCtl")
public class UserCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(UserCtl.class);

	
	protected void preload(HttpServletRequest request) {
		RoleModelInt model = ModelFactory.getInstance().getRoleModel();
		try {
			List l = model.list();
			request.setAttribute("roleList", l);
		} catch (ApplicationException e) {
			log.error(e);
		}

	}

	@Override
	protected boolean validate(HttpServletRequest request) {

		log.debug("UserCtl Method validate Started");

		boolean pass = true;

		String login = request.getParameter("login");
		String dob = request.getParameter("dob");

		if (DataValidator.isNull(request.getParameter("firstName"))) {

			request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
			pass = false;
		} else if (!DataValidator.isFname(request.getParameter("firstName"))) {

			request.setAttribute("firstName", PropertyReader.getValue("error.name", "First Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("lastName"))) {

			request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
			pass = false;
		} else if (!DataValidator.isFname(request.getParameter("lastName"))) {

			request.setAttribute("lastName", PropertyReader.getValue("error.name", "Last Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("mobileNo"))) {

			request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile Numeber"));
			pass = false;
		} else if (!DataValidator.isPhoneNo(request.getParameter("mobileNo"))) {

			request.setAttribute("mobileNo", PropertyReader.getValue("error.mobile", "Mobile Numeber"));
			pass = false;
		}
		if (DataValidator.isNull(login)) {

			request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
			pass = false;
		} else if (!DataValidator.isEmail(login)) {

			request.setAttribute("login", PropertyReader.getValue("error.email", "Login "));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("password"))) {

			request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
			pass = false;
		} else if (!DataValidator.isPassword(request.getParameter("password"))) {

			request.setAttribute("password", PropertyReader.getValue("error.password", "Password"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("gender"))) {
			request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("roleId"))
				|| !(Integer.parseInt(request.getParameter("roleId")) > 0)) {
			request.setAttribute("roleId", PropertyReader.getValue("error.require", "Role Name"));
			pass = false;
		}

		if (DataValidator.isNull(dob)) {
			request.setAttribute("dob", PropertyReader.getValue("error.require", "Date Of Birth"));
			pass = false;
		} else if (!DataValidator.isDate(dob)) {
			request.setAttribute("dob", PropertyReader.getValue("error.date", "Date Of Birth"));
			pass = false;
		}

		log.debug("UserCtl Method validate Ended");

		return pass;
	}

	
	protected BaseDTO populateDTO(HttpServletRequest request) {

		log.debug("UserCtl Method populateDTO Started");

		UserDTO dto = new UserDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		
		System.out.println("id "+dto.getId());

		dto.setRoleId(DataUtility.getLong(request.getParameter("roleId")));

		dto.setFirstName(DataUtility.getString(request.getParameter("firstName")));

		dto.setLastName(DataUtility.getString(request.getParameter("lastName")));

		dto.setLoginId(DataUtility.getString(request.getParameter("login")));

		dto.setPassword(DataUtility.getString(request.getParameter("password")));

		dto.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));

		dto.setGender(DataUtility.getString(request.getParameter("gender")));
		
		dto.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
		
		dto.setDob(DataUtility.getDate(request.getParameter("dob")));

		log.debug("UserCtl Method populateDTO Ended");
		populateDateTime(dto, request);
		return dto;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("UserCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));

		UserModelInt model = ModelFactory.getInstance().getUserModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0 || op != null) {

			UserDTO dto = null;
			try {
				dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		}
		ServletUtility.forward(ORSView.USER_VIEW, request, response);

		log.debug("UserCtl Method doGet Ended");
	}
   /**
    * Submit Logic
    */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("UserCtl Method doGet Ended");
		String op = DataUtility.getString(request.getParameter("operation"));

		System.out.println("in do post of userCtl");

		// get model
		UserModelInt model = ModelFactory.getInstance().getUserModel();

		long id = DataUtility.getLong(request.getParameter("id"));
		
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			UserDTO dto = (UserDTO) populateDTO(request);

			try {
				if (id > 0) {

					ServletUtility.setSuccessMessage("Data updated successfully ", request);
					ServletUtility.setDto(dto, request);
				} else {
					long pk = model.add(dto);
					//// dto.setId(pk);
				//	 ServletUtility.setDto(dto, request);
					/* System.out.println("id "+dto.getId()); */
					ServletUtility.setSuccessMessage("Data saved successfully ", request);
				}

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.USER_CTL, request, response);
			return;
		}
		ServletUtility.forward(ORSView.USER_VIEW, request, response);
		log.debug("UserCtl Method doGet Ended");
	}

	protected String getView() {
		return ORSView.USER_VIEW;
	}

}
