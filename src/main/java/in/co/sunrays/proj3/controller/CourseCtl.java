package in.co.sunrays.proj3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.exception.DuplicateRecordException;
import in.co.sunrays.proj3.model.CourseModelInt;
import in.co.sunrays.proj3.model.ModelFactory;
import in.co.sunrays.proj3.util.DataUtility;
import in.co.sunrays.proj3.util.DataValidator;
import in.co.sunrays.proj3.util.PropertyReader;
import in.co.sunrays.proj3.util.ServletUtility;
import in.co.sunrays.project3.dto.BaseDTO;
import in.co.sunrays.project3.dto.CourseDTO;
import net.sf.ehcache.hibernate.EhCacheProvider;

/**
 * Course functionality Controller. Performs operation for add, update, delete
 * and get Course
 * 
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */
@WebServlet(name = "CourseCtl", urlPatterns = "/ctl/CourseCtl")
public class CourseCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(CourseCtl.class);

	protected boolean validate(HttpServletRequest request) {
		
		
		
		log.debug("course ctl validate started");
		boolean pass = true;
		if (DataValidator.isNull(request.getParameter("cname"))) {
			request.setAttribute("cname", PropertyReader.getValue("error.require", "Course Name"));
			pass = false;
		} else if (!DataValidator.isFname(request.getParameter("cname"))) {
			request.setAttribute("cname", PropertyReader.getValue("error.name", "Course Name"));
		}
		if (DataValidator.isNull(request.getParameter("duration"))) {
			request.setAttribute("duration", PropertyReader.getValue("error.require", "Duration"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			pass = false;
		} else if (!DataValidator.isFname(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.name", "Description"));
			pass = false;
		}
		log.debug("course ctl validate ended");
		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {
		log.debug("populateDTO started");
		CourseDTO dto = new CourseDTO();
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setCourseName(DataUtility.getString(request.getParameter("cname")));
		dto.setDescription(DataUtility.getString(request.getParameter("description")));
		dto.setDuration(DataUtility.getString(request.getParameter("duration")));
		populateDateTime(dto, request);
		log.debug("popolateDTO Ended");
		return dto;
	}

	/**
	 * View Logic
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("course ctl doGet Started");
		CourseDTO dto = (CourseDTO) populateDTO(request);
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0) {
			try {
				dto = ModelFactory.getInstance().getCourseModel().findByPK(dto.getId());
				ServletUtility.setDto(dto, request);
			} catch (ApplicationException e) {
				log.error("Exception in course ctl doGet:" + e);
				e.printStackTrace();
			}
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("course ctl doGet Ended");
	}

	/**
	 * Submit Logic
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("Course ctl doPost Started");
		String op = DataUtility.getString(request.getParameter("operation"));

		if (OP_RESET.equals(op)) {
			ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
			return;
		}
		if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
			return;
		}
		CourseDTO dto = (CourseDTO) populateDTO(request);

		CourseModelInt model = ModelFactory.getInstance().getCourseModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			try {
				if (dto.getId() > 0) {
					model.update(dto);
					ServletUtility.setSuccessMessage("Data updated successfully", request);
					ServletUtility.setDto(dto, request);
				} else {
					model.add(dto);
					ServletUtility.setSuccessMessage("Data saved successfully", request);
				}

			} catch (ApplicationException e) {
				log.error("Exception in course ctl doPost" + e);
				ServletUtility.handleException(e, request, response);
			} catch (DuplicateRecordException e) {
				log.warn("Exception in course ctl" + e);
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Course is already exists", request);
			} catch (Exception e) {

			}
		}
		log.debug("course ctl doPost Ended");
		ServletUtility.forward(getView(), request, response);

	}

	protected String getView() {

		return ORSView.COURSE_VIEW;
	}

}
