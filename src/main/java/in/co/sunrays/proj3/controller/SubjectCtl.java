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
import in.co.sunrays.proj3.model.CourseModelInt;
import in.co.sunrays.proj3.model.ModelFactory;
import in.co.sunrays.proj3.model.SubjectModelInt;
import in.co.sunrays.proj3.util.DataUtility;
import in.co.sunrays.proj3.util.DataValidator;
import in.co.sunrays.proj3.util.PropertyReader;
import in.co.sunrays.proj3.util.ServletUtility;
import in.co.sunrays.project3.dto.BaseDTO;
import in.co.sunrays.project3.dto.CourseDTO;
import in.co.sunrays.project3.dto.SubjectDTO;

/**
 * Subject functionality Controller. Performs operation for add, update and get
 * Subject
 * 
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */

@WebServlet(name = "SubjectCtl", urlPatterns = "/ctl/SubjectCtl")
public class SubjectCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(SubjectCtl.class);

	protected void preload(HttpServletRequest request) {

		CourseModelInt model = ModelFactory.getInstance().getCourseModel();
		List list = null;
		try {
			list = model.list();
			request.setAttribute("courseList", list);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	protected boolean validate(HttpServletRequest request) {
		log.debug(" validate started");
		boolean pass = true;
		if (DataValidator.isNull(request.getParameter("subName"))) {
			request.setAttribute("subName", PropertyReader.getValue("error.require", "Subject name"));
			pass = false;
		} else if (!DataValidator.isFname(request.getParameter("subName"))) {
			request.setAttribute("subName", PropertyReader.getValue("error.name", "Subject name"));
		}
		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			pass = false;
		} else if (!DataValidator.isFname(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.name", "Description"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("courseId"))
				|| !(DataUtility.getInt(request.getParameter("courseId")) > 0)) {
			request.setAttribute("courseId", PropertyReader.getValue("error.require", "Course name"));
			pass = false;
		} /*
			 * if(DataValidator.isNull(request.getParameter("semester"))){
			 * request.setAttribute("semester", PropertyReader.getValue("error.require",
			 * "Semester")); pass=false; }
			 */

		if (DataValidator.isNull(request.getParameter("semester"))) {
			request.setAttribute("semester", PropertyReader.getValue("error.require", "Semester"));
			pass = false;
		}
		log.debug(" validate ended");
		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {
		log.debug("populateDTO started");
		SubjectDTO dto = new SubjectDTO();
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setSubjectName(DataUtility.getString(request.getParameter("subName")));
		dto.setDescription(DataUtility.getString(request.getParameter("description")));
		dto.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
		dto.setSemester(DataUtility.getString(request.getParameter("semester")));

		populateDateTime(dto, request);
		log.debug("popolateDTO Ended");
		return dto;
	}

	/**
	 * View Logic
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("Subject ctl doGet started");

		System.out.println("in do get of student model");
		SubjectDTO dto = (SubjectDTO) populateDTO(request);

		SubjectModelInt model = ModelFactory.getInstance().getSubjectModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		try {
			if (id > 0) {
				dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);
			}
		} catch (ApplicationException e) {
			log.error(e);
			e.printStackTrace();
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("Subject ctl doGet Ended");
	}

	/**
	 * Submit Logic
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("Subject ctl doPost started");
		String op = DataUtility.getString(request.getParameter("operation"));
		if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.SUBJECT_CTL, request, response);
			return;
		}
		if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.SUBJECT_LIST_CTL, request, response);
			return;
		}

		SubjectDTO dto = (SubjectDTO) populateDTO(request);

		SubjectModelInt model = ModelFactory.getInstance().getSubjectModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			try {
				if (dto.getId() > 0) {
					model.update(dto);
					ServletUtility.setSuccessMessage("Data updated successfully", request);
					ServletUtility.setDto(dto, request);
				} else {
					dto.setId(model.add(dto));
					ServletUtility.setSuccessMessage("Data saved successfully", request);
				}

			} catch (ApplicationException e) {
				log.error("Exception in Subject ctl doPost" + e);
				ServletUtility.handleException(e, request, response);
			} catch (DuplicateRecordException e) {
				log.warn("Exception in Subject ctl" + e);
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Subject is already exists", request);
			}
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("Subject ctl doPost started");
	}

	protected String getView() {

		return ORSView.SUBJECT_VIEW;
	}

}
