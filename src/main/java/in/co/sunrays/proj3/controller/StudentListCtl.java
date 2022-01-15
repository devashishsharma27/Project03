package in.co.sunrays.proj3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.model.CollegeModelInt;
import in.co.sunrays.proj3.model.ModelFactory;
import in.co.sunrays.proj3.model.StudentModelInt;
import in.co.sunrays.proj3.util.DataUtility;
import in.co.sunrays.proj3.util.PropertyReader;
import in.co.sunrays.proj3.util.ServletUtility;
import in.co.sunrays.project3.dto.BaseDTO;
import in.co.sunrays.project3.dto.StudentDTO;

/**
 * Student List functionality Controller. Performs operation for list, search
 * and delete operations of Student
 * 
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */
@WebServlet(name = "StudentListCtl", urlPatterns = "/ctl/StudentListCtl")
public class StudentListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(StudentListCtl.class);

	protected void preload(HttpServletRequest request) {

		CollegeModelInt cModel = ModelFactory.getInstance().getCollegeModel();
		List list = null;
		try {

			list = cModel.list();
			request.setAttribute("cList", list);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

	}

	protected BaseDTO populateDTO(HttpServletRequest request) {

		StudentDTO dto = new StudentDTO();
		dto.setFirstName(DataUtility.getString(request.getParameter("firstName")));
		dto.setCollegeId(DataUtility.getLong(request.getParameter("collegeId")));
		dto.setEmail(DataUtility.getString(request.getParameter("email")));
		return dto;
	}

	/**
	 * View Logic
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.debug("StudentListCtl doGet Start");

		List list = null;

		int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(req.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;

		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		StudentDTO dto = (StudentDTO) populateDTO(req);

		String op = DataUtility.getString(req.getParameter("operation"));

		StudentModelInt model = ModelFactory.getInstance().getStudentModel();

		try {

			list = model.search(dto, pageNo, pageSize);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", req);
			}
			ServletUtility.setList(list, req);
			ServletUtility.setPageNo(pageNo, req);
			ServletUtility.setPageSize(pageSize, req);
			ServletUtility.forward(ORSView.STUDENT_LIST_VIEW, req, resp);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, req, resp);
			return;
		}
		log.debug("StudentListCtl doGet End");
	}

	/**
	 * Submit Logic
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("StudentListCtl doPost Start");

		List list = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		System.out.println("pageNo " + pageNo + "  pageSize  " + pageSize);

		pageNo = (pageNo == 0) ? 1 : pageNo;

		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		StudentDTO dto = (StudentDTO) populateDTO(request);

		String op = DataUtility.getString(request.getParameter("operation"));

		StudentModelInt model = ModelFactory.getInstance().getStudentModel();

		try {

			if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.STUDENT_LIST_CTL, request, response);
				return;
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.STUDENT_CTL, request, response);
				return;
			} else if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op)
					|| "Previous".equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}

			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				String[] ids = request.getParameterValues("ids");
				if (ids == null || ids.length == 0) {
					ServletUtility.setErrorMessage("Please select at least one record ", request);

				} else {
					for (String string : ids) {

						StudentDTO deleteId = new StudentDTO();
						deleteId.setId(DataUtility.getLong(string));
						model.delete(deleteId);
					}
					ServletUtility.setSuccessMessage("Record deleted successfully", request);
				}
			}
			list = model.search(dto, pageNo, pageSize);

			if (list == null && !(OP_DELETE.equalsIgnoreCase(op))) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(ORSView.STUDENT_LIST_VIEW, request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		log.debug("StudentListCtl doPost End");
	}

	protected String getView() {

		return ORSView.STUDENT_LIST_VIEW;
	}

}
