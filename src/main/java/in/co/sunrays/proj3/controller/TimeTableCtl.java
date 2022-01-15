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
import in.co.sunrays.proj3.exception.RecordNotFoundException;
import in.co.sunrays.proj3.model.ModelFactory;
import in.co.sunrays.proj3.model.TimeTableModelInt;
import in.co.sunrays.proj3.util.DataUtility;
import in.co.sunrays.proj3.util.DataValidator;
import in.co.sunrays.proj3.util.PropertyReader;
import in.co.sunrays.proj3.util.ServletUtility;
import in.co.sunrays.project3.dto.BaseDTO;
import in.co.sunrays.project3.dto.TimetableDTO;

/**
 * TimeTable functionality Controller. Performs operation for add, update and
 * get TimeTable
 * 
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */

@WebServlet(name = "TimeTableCtl", urlPatterns = "/ctl/TimeTableCtl")
public class TimeTableCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(TimeTableCtl.class);

	protected boolean validate(HttpServletRequest request) {

		log.debug("TimeTableCtlMethod validate started");
		boolean pass = true;
		String op = DataUtility.getString(request.getParameter("operation"));

		if (DataUtility.getString(request.getParameter("courseId")).equals("")) {
			request.setAttribute("courseId", PropertyReader.getValue("error.require", "Course Name"));
			pass = false;
		}

		if (DataUtility.getString(request.getParameter("subjectId")).equals("")) {
			request.setAttribute("subjectId", PropertyReader.getValue("error.require", "Subject Name"));
			pass = false;
		}

		if (DataUtility.getString(request.getParameter("semester")).equals("")) {
			request.setAttribute("semester", PropertyReader.getValue("error.require", "Semester"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("examDate"))) {
			request.setAttribute("examDate", PropertyReader.getValue("error.require", "Date of exam"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("examDate"))) {
			request.setAttribute("examDate", PropertyReader.getValue("error.date", "Date of exam"));
			pass = false;
		} else if (DataUtility.getDate(request.getParameter("examDate")).getDay() == 0) {
			request.setAttribute("examDate", "Exam can't be scheduled on sunday");
			pass = false;

		} /*
			 * else if (DataUtility.getDate(request.getParameter("examDate"))!=new Date()){
			 * request.setAttribute("examDate", "Use Another Date"); pass = false;
			 * 
			 * }
			 */

		if (DataUtility.getString(request.getParameter("examTime")).equals("")) {
			request.setAttribute("examTime", PropertyReader.getValue("error.require", "Examtime"));
			pass = false;
		}

		log.debug("TimeTableCtl Method validate Ended");
		return pass;

	}

	protected void preload(HttpServletRequest request) {
		log.debug("time table ctl preload started");
		try {
			List clist = ModelFactory.getInstance().getCourseModel().list();
			List slist = ModelFactory.getInstance().getSubjectModel().list();
			request.setAttribute("courseList", clist);
			request.setAttribute("subjectList", slist);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("timetable preload ended");
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {

		log.debug("time table populate started");
		TimetableDTO dto = new TimetableDTO();
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		System.out.println("id ......" + dto.getId());
		dto.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
		dto.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));
		dto.setSemester(DataUtility.getString(request.getParameter("semester")));
		dto.setExamTime(DataUtility.getString(request.getParameter("examTime")));
		dto.setExamDate(DataUtility.getDate(request.getParameter("examDate")));
		populateDateTime(dto, request);
		log.debug("time table populate ended");
		return dto;
	}

	/**
	 * View Logic
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.debug("TimeTableCtl Method doGet Started");
		// System.out.println("In do get");
		String op = DataUtility.getString(req.getParameter("operation"));
		long id = DataUtility.getLong(req.getParameter("id"));
		// System.err.println("bbd.b" + op);
		// get model
		System.out.println("id  " + id);
		TimeTableModelInt model = ModelFactory.getInstance().getTimeTableModel();
		if (id > 0) {
			System.out.println("inside id>0");
			TimetableDTO dto;
			try {
				dto = model.findByPK(id);
				System.out.println(dto.getCourseName() + " " + dto.getSubjectName() + " " + dto.getExamTime());
				ServletUtility.setDto(dto, req);
			} catch (ApplicationException e) {
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, req, resp);
				return;
			}
		}

		ServletUtility.forward(getView(), req, resp);

		log.debug("TimeTableCtl Method doGet Ended");
	}

	/**
	 * Submit Logic
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.debug("TimeTableCtl Method doPost Started");
		String op = DataUtility.getString(req.getParameter("operation"));
		// get model

		TimeTableModelInt model = ModelFactory.getInstance().getTimeTableModel();
		long id = DataUtility.getLong(req.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			TimetableDTO bean = (TimetableDTO) populateDTO(req);
			try {
				if (id > 0) {
					model.update(bean);
					ServletUtility.setDto(bean, req);
					ServletUtility.setSuccessMessage("Data is successfully updated", req);
				} else {
					long pk = model.add(bean);
					///// bean.setId(pk);

					ServletUtility.setSuccessMessage("Data is successfully added", req);
				}
			} catch (ApplicationException e) {
				log.error(e);
				e.printStackTrace();
				ServletUtility.handleException(e, req, resp);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(bean, req);
				ServletUtility.setErrorMessage("TimeTable is already exists", req);
			} catch (RecordNotFoundException e) {
				ServletUtility.setDto(bean, req);
				ServletUtility.setErrorMessage(e.getMessage(), req);
			}

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			TimetableDTO bean = (TimetableDTO) populateDTO(req);
			try {
				model.delete(bean);
				ServletUtility.redirect(ORSView.TIME_TABLE_LIST_CTL, req, resp);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, req, resp);
				return;
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.TIME_TABLE_CTL, req, resp);
			return;

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.TIME_TABLE_LIST_CTL, req, resp);
			return;

		}
		ServletUtility.forward(getView(), req, resp);

		log.debug("TimeTableCtl Method doPost Ended");

	}

	protected String getView() {

		return ORSView.TIME_TABLE_VIEW;
	}

}
