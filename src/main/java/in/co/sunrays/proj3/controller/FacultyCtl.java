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
import in.co.sunrays.proj3.model.CollegeModelInt;
import in.co.sunrays.proj3.model.CourseModelInt;
import in.co.sunrays.proj3.model.FacultyModelInt;
import in.co.sunrays.proj3.model.ModelFactory;
import in.co.sunrays.proj3.model.SubjectModelInt;
import in.co.sunrays.proj3.util.DataUtility;
import in.co.sunrays.proj3.util.DataValidator;
import in.co.sunrays.proj3.util.PropertyReader;
import in.co.sunrays.proj3.util.ServletUtility;
import in.co.sunrays.project3.dto.BaseDTO;
import in.co.sunrays.project3.dto.FacultyDTO;

/**
 * Faculty functionality Controller. Performs operation for add, update and get
 * Faculty
 * 
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */
@WebServlet(name = "FacultyCtl", urlPatterns = "/ctl/FacultyCtl")
public class FacultyCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(FacultyCtl.class);

	protected void preload(HttpServletRequest request) {
		CollegeModelInt collegeModel = ModelFactory.getInstance().getCollegeModel();
		CourseModelInt courseModel = ModelFactory.getInstance().getCourseModel();
		SubjectModelInt subjectModel = ModelFactory.getInstance().getSubjectModel();
		try {
			List collegeList = collegeModel.list();
			List courseList = courseModel.list();
			List subjectList = subjectModel.list();
			request.setAttribute("collegeList", collegeList);
			request.setAttribute("courseList", courseList);
			request.setAttribute("subjectList", subjectList);
			System.out.println(collegeList + " " + courseList + " " + subjectList);
		} catch (ApplicationException e) {
			log.error(e);
		}

	}

	protected boolean validate(HttpServletRequest request) {

		log.debug("FacultyCtl Method validate Started");

		boolean pass = true;

		String emailId = request.getParameter("loginId");
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

		if (DataUtility.getString(request.getParameter("gender")).equals("")) {
			request.setAttribute("gender", PropertyReader.getValue("error.require", "gender"));
			pass = false;
		}

		if (DataUtility.getInt(request.getParameter("collegeId")) == 0) {
			request.setAttribute("collegeId", PropertyReader.getValue("error.require", "college Name"));
			pass = false;
		}

		/*
		 * if(DataUtility.getInt(request.getParameter("courseName"))==0) {
		 * request.setAttribute("courseName", PropertyReader.getValue("error.require",
		 * "course Name")); pass = false; }
		 */

		if (DataUtility.getInt(request.getParameter("subjectId")) == 0) {
			request.setAttribute("subjectId", PropertyReader.getValue("error.require", "subject Name"));
			pass = false;
		}
		if (DataUtility.getInt(request.getParameter("courseId")) == 0) {
			request.setAttribute("courseId", PropertyReader.getValue("error.require", "Course Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("qualification"))) {

			request.setAttribute("qualification", PropertyReader.getValue("error.require", "Qualification"));
			pass = false;
		}

		if (DataValidator.isNull(emailId)) {

			request.setAttribute("loginId", PropertyReader.getValue("error.require", "Email Id"));
			pass = false;
		} else if (!DataValidator.isEmail(emailId)) {

			request.setAttribute("loginId", PropertyReader.getValue("error.require", "Email "));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile number"));
			pass = false;
		}

		if (DataValidator.isNull(dob)) {

			request.setAttribute("dob", PropertyReader.getValue("error.require", "Joining Date"));

			pass = false;
		} /*
			 * else if (!DataValidator.isDate(dob)) {
			 * 
			 * request.setAttribute("jod", PropertyReader.getValue("error.require",
			 * "Joining Date")); pass = false; }
			 */

		log.debug("FacultyCtl Method validate Ended");

		return pass;
	}

	protected BaseDTO populateDto(HttpServletRequest request) {

		log.debug("FacultyCtl Method populatebean Started");

		FacultyDTO dto = new FacultyDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));

		dto.setFirstName(DataUtility.getString(request.getParameter("firstName")));

		dto.setLastName(DataUtility.getString(request.getParameter("lastName")));

		dto.setCollegeId(DataUtility.getLong(request.getParameter("collegeId")));

		dto.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));

		dto.setCourseId(DataUtility.getLong(request.getParameter("courseId")));

		dto.setQualification(DataUtility.getString(request.getParameter("qualification")));

		dto.setEmailId(DataUtility.getString(request.getParameter("loginId")));

		dto.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));

		dto.setGender(DataUtility.getString(request.getParameter("gender")));

		dto.setJoiningDate(DataUtility.getDate(request.getParameter("doj")));

		dto.setDob(DataUtility.getDate(request.getParameter("dob")));

		populateDateTime(dto, request);

		log.debug("FacultyCtl Method populatebean Ended");

		return dto;
	}

	/**
	 * View Logic
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.debug("FacultyCtl Method doGet Started");

		String op = DataUtility.getString(req.getParameter("operation"));
		// get model
		FacultyModelInt model = ModelFactory.getInstance().getFacultyModel();
		long id = DataUtility.getLong(req.getParameter("id"));

		if (id > 0 || op != null) {
			FacultyDTO dto;
			try {
				dto = model.findByPK(id);
				ServletUtility.setDto(dto, req);
			} catch (ApplicationException e) {
				log.error(e);
				e.printStackTrace();
				ServletUtility.handleException(e, req, resp);
				return;
			}
		}

		ServletUtility.forward(getView(), req, resp);
		log.debug("FacultyCtl Method doGet Ended");
	}

	/**
	 * Submit Logic
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.debug("FacultyCtl Method doPost Started");

		String op = DataUtility.getString(req.getParameter("operation"));
		// get model
		FacultyModelInt model = ModelFactory.getInstance().getFacultyModel();
		long id = DataUtility.getLong(req.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			FacultyDTO dto = (FacultyDTO) populateDto(req);

			try {
				if (id > 0) {
					model.update(dto);
					ServletUtility.setDto(dto, req);
					ServletUtility.setSuccessMessage("Data is succesfully updated", req);
				} else {
					long pk = model.add(dto);
					/// bean.setId(pk);
					/// ServletUtility.setDto(dto, req);
					ServletUtility.setSuccessMessage("Data is successfully saved", req);
				}

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, req, resp);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, req);
				ServletUtility.setErrorMessage("Login id already exists", req);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			FacultyDTO dto = (FacultyDTO) populateDto(req);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, req, resp);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, req, resp);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, req, resp);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.FACULTY_CTL, req, resp);
			return;
		}
		ServletUtility.forward(getView(), req, resp);

		log.debug("FacultyCtl Method doPostEnded");
	}

	protected String getView() {

		return ORSView.FACULTY_VIEW;
	}

}
