package in.co.sunrays.proj3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.model.ModelFactory;
import in.co.sunrays.proj3.model.RoleModelHibImp;
import in.co.sunrays.proj3.model.RoleModelInt;
import in.co.sunrays.proj3.model.UserModelHibImp;
import in.co.sunrays.proj3.model.UserModelInt;
import in.co.sunrays.proj3.util.DataUtility;
import in.co.sunrays.proj3.util.DataValidator;
import in.co.sunrays.proj3.util.PropertyReader;
import in.co.sunrays.proj3.util.ServletUtility;
import in.co.sunrays.project3.dto.BaseDTO;
import in.co.sunrays.project3.dto.RoleDTO;
import in.co.sunrays.project3.dto.UserDTO;

/**
 * Login functionality Controller. Performs operation for Login
 * 
 * 
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */
@WebServlet(name = "LoginCtl", urlPatterns = "/LoginCtl")
public class LoginCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	public static final String OP_REGISTER = "Register";
	public static final String OP_SIGN_IN = "SignIn";
	public static final String OP_SIGN_UP = "SignUp";
	public static final String OP_LOG_OUT = "logout";
	private static Logger log = Logger.getLogger(LoginCtl.class);

	protected boolean validate(HttpServletRequest request) {

		log.debug("LoginCtl Method validate Started");

		boolean pass = true;

		String op = request.getParameter("operation");
		if (OP_SIGN_UP.equals(op) || OP_LOG_OUT.equals(op)) {
			return pass;
		}

		String login = request.getParameter("login");

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
		}

		log.debug("LoginCtl Method validate Ended");

		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {

		log.debug(" populateDTO method of loginCtl started");

		UserDTO dto = new UserDTO();

		dto.setLoginId(DataUtility.getString(request.getParameter("login")));
		dto.setPassword(DataUtility.getString(request.getParameter("password")));

		return dto;
	}

	/**
	 * Submit Logic
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.debug(" do get method of loginCtl started");

		String op = req.getParameter("operation");
		if (OP_LOG_OUT.equalsIgnoreCase(op)) {
			HttpSession session = req.getSession(true);
			session.invalidate();
			ServletUtility.setSuccessMessage("You have successfully logout....", req);
		}

		ServletUtility.forward(getView(), req, resp);

		log.debug(" do get method of loginCtl end");

	}

	/**
	 * Submit Logic
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.debug(" do post method of loginCtl started");

		String op = DataUtility.getString(req.getParameter("operation"));
		String uri = DataUtility.getString(req.getParameter("uri"));

		UserModelInt uModel = ModelFactory.getInstance().getUserModel();
		RoleModelInt rModel = ModelFactory.getInstance().getRoleModel();

		if (OP_SIGN_IN.equalsIgnoreCase(op)) {

			UserDTO dto = (UserDTO) populateDTO(req);

			try {

				dto = uModel.authenticate(dto.getLoginId(), dto.getPassword());

				if (dto != null) {
					HttpSession session = req.getSession(true);
					session.setAttribute("user", dto);
					long rollId = dto.getRoleId();

					RoleDTO roleDto = rModel.findByPK(rollId);

					if (roleDto != null) {
						session.setAttribute("role", roleDto.getName());
					}
					System.out.println("near WelcomeCtl " + uri);
					if (uri != null) {
						if (uri.trim().length() > 0) {
							System.out.println("in uri");
							ServletUtility.redirect(uri, req, resp);
							return;
						}
					} else {
						System.out.println("in welcome");
						ServletUtility.redirect(ORSView.WELCOME_CTL, req, resp);
						return;
					}
				} else {
					dto = (UserDTO) populateDTO(req);
					ServletUtility.setDto(dto, req);
					ServletUtility.setErrorMessage("Invalid LoginId And Password", req);
				}

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, req, resp);
				return;
			}

		} else if (OP_SIGN_UP.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.USER_REGISTRATION_CTL, req, resp);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.LOGIN_CTL, req, resp);
			return;
		}
		ServletUtility.forward(getView(), req, resp);

		log.debug(" do post method of loginCtl end");
	}

	protected String getView() {

		return ORSView.LOGIN_VIEW;
	}

}
