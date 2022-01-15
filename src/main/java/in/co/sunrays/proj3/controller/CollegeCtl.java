package in.co.sunrays.proj3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.exception.DuplicateRecordException;
import in.co.sunrays.proj3.model.CollegeModelInt;
import in.co.sunrays.proj3.model.ModelFactory;
import in.co.sunrays.proj3.util.DataUtility;
import in.co.sunrays.proj3.util.DataValidator;
import in.co.sunrays.proj3.util.PropertyReader;
import in.co.sunrays.proj3.util.ServletUtility;
import in.co.sunrays.project3.dto.BaseDTO;
import in.co.sunrays.project3.dto.CollegeDTO;

/**
 * College functionality Controller. Performs operation for add, update, delete
 * and get College
 * 
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */
@WebServlet(name = "CollegeCtl", urlPatterns = "/ctl/CollegeCtl")
public class CollegeCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(CollegeCtl.class);

	protected boolean validate(HttpServletRequest request) {

		log.debug("CollegeCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("cName"))) {
			request.setAttribute("cName", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("address"))) {
			request.setAttribute("address", PropertyReader.getValue("error.require", "Address"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("state"))) {
			request.setAttribute("state", PropertyReader.getValue("error.require", "State"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("city"))) {
			request.setAttribute("city", PropertyReader.getValue("error.require", "City"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("phoneNo"))) {
			request.setAttribute("phoneNo", PropertyReader.getValue("error.require", "Phone No"));
			pass = false;
		}

		log.debug("CollegeCtl Method validate Ended");

		return pass;
	}

	protected BaseDTO populateDto(HttpServletRequest request) {

		log.debug("CollegeCtl Method populatebean Started");

		CollegeDTO dto = new CollegeDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));

		dto.setName(DataUtility.getString(request.getParameter("cName")));

		dto.setAddress(DataUtility.getString(request.getParameter("address")));

		dto.setState(DataUtility.getString(request.getParameter("state")));

		dto.setCity(DataUtility.getString(request.getParameter("city")));

		dto.setPhoneNo(DataUtility.getString(request.getParameter("phoneNo")));

		populateDateTime(dto, request);

		log.debug("CollegeCtl Method populatebean Ended");

		return dto;
	}

	/**
	 * View Logic
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String op = DataUtility.getString(req.getParameter("operation"));

		// get model
		CollegeModelInt model = ModelFactory.getInstance().getCollegeModel();

		long id = DataUtility.getLong(req.getParameter("id"));

		if (id > 0) {
			CollegeDTO dto;
			try {
				dto = model.findByPk(id);
				ServletUtility.setDto(dto, req);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, req, resp);
				return;
			}
		}

		ServletUtility.forward(getView(), req, resp);
	}

	/**
	 * Submit Logic
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.debug("CollegeCtl Method doPost Started");

		String op = DataUtility.getString(req.getParameter("operation"));

		// get model
		CollegeModelInt model = ModelFactory.getInstance().getCollegeModel();

		long id = DataUtility.getLong(req.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			CollegeDTO dto = (CollegeDTO) populateDto(req);
			System.out.println("College id= " + dto.getId());
			try {
				if (id > 0) {
					System.out.println("in update");
					model.update(dto);
					ServletUtility.setDto(dto, req);
					ServletUtility.setSuccessMessage("Data is successfully updated", req);
				} else {
					long pk = model.add(dto);

					System.out.println("in add College id= " + dto.getId());
					/// ServletUtility.setDto(dto, req);
					ServletUtility.setSuccessMessage("Data is successfully saved", req);

				}

			} catch (ApplicationException e) {
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, req, resp);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, req);
				ServletUtility.setErrorMessage("College Name already exists", req);
			} catch (Exception e) {

			}

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			CollegeDTO dto = (CollegeDTO) populateDto(req);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, req, resp);
				return;

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, req, resp);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, req, resp);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.COLLEGE_CTL, req, resp);
			return;
		}
		ServletUtility.forward(getView(), req, resp);

		log.debug("CollegeCtl Method doGet Ended");
	}

	protected String getView() {

		return ORSView.COLLEGE_VIEW;
	}

}
