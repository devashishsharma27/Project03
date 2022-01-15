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
import in.co.sunrays.proj3.model.FacultyModelInt;
import in.co.sunrays.proj3.model.ModelFactory;
import in.co.sunrays.proj3.util.DataUtility;
import in.co.sunrays.proj3.util.PropertyReader;
import in.co.sunrays.proj3.util.ServletUtility;
import in.co.sunrays.project3.dto.BaseDTO;
import in.co.sunrays.project3.dto.FacultyDTO;

/**
 * Faculty List functionality Controller. Performs operation for list, search
 * and delete operations of Faculty
 * 
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */
@WebServlet(name = "FacultyListCtl", urlPatterns = "/ctl/FacultyListCtl")
public class FacultyListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(FacultyListCtl.class);

	protected void preload(HttpServletRequest request) {

		CollegeModelInt smodel = ModelFactory.getInstance().getCollegeModel();
		try {

			List l = smodel.list();

			request.setAttribute("collegeList", l);

		} catch (ApplicationException e) {
			log.error(e);
		}

	}

	protected BaseDTO populateDto(HttpServletRequest request) {
		FacultyDTO dto = new FacultyDTO();
		dto.setId(DataUtility.getLong(request.getParameter("facultyId")));
		dto.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
		dto.setFirstName(DataUtility.getString(request.getParameter("firstName")));

		dto.setCollegeId(DataUtility.getLong(request.getParameter("collegeId")));
		dto.setEmailId(DataUtility.getString(request.getParameter("loginId")));

		return dto;
	}

	/**
	 * View Logic
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.debug("FacultyListCtl doGet Start");
		List list = null;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		FacultyDTO dto = (FacultyDTO) populateDto(req);
		String op = DataUtility.getString(req.getParameter("operation"));

		FacultyModelInt model = ModelFactory.getInstance().getFacultyModel();
		try {
			list = model.search(dto, pageNo, pageSize);
			System.out.println("list..........." + list);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", req);
			}
			ServletUtility.setList(list, req);
			ServletUtility.setPageNo(pageNo, req);
			ServletUtility.setPageSize(pageSize, req);
			ServletUtility.forward(getView(), req, resp);
		} catch (ApplicationException e) {
			log.error(e);
			e.printStackTrace();
			ServletUtility.handleException(e, req, resp);
			return;
		}
		log.debug("FacultyListCtl doGet End");
	}

	/**
	 * Submit Logic
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.debug("FacultyListCtl doPost Start");
		List list = null;
		int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(req.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		FacultyDTO dto = (FacultyDTO) populateDto(req);
		String op = DataUtility.getString(req.getParameter("operation"));

		// get the selected checkbox ids array for delete list
		String[] ids = req.getParameterValues("ids");
		FacultyModelInt model = ModelFactory.getInstance().getFacultyModel();
		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op) || "Previous".equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					/// pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}

			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.FACULTY_CTL, req, resp);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					FacultyDTO deleteDto = new FacultyDTO();
					for (String id : ids) {
						deleteDto.setId(DataUtility.getInt(id));
						model.delete(deleteDto);
					}

					ServletUtility.setSuccessMessage("Record deleted successfully", req);
				} else {
					ServletUtility.setErrorMessage("Select at least one record", req);
				}
			} else if (OP_BACK.equalsIgnoreCase(op)) {
				pageNo = DataUtility.getInt(req.getParameter("pageNo1"));
				System.out.println("page" + pageNo);
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, req, resp);
				return;
			}

			list = model.search(dto, pageNo, pageSize);
			ServletUtility.setList(list, req);

			if ((list == null || list.size() == 0) && !OP_DELETE.equalsIgnoreCase(op)) {
				ServletUtility.setErrorMessage("No record found ", req);
			}
			req.setAttribute("sbean", dto);
			ServletUtility.setDto(dto, req);
			ServletUtility.setList(list, req);
			ServletUtility.setPageNo(pageNo, req);
			ServletUtility.setPageSize(pageSize, req);
			ServletUtility.forward(getView(), req, resp);

		} catch (ApplicationException e) {
			log.error(e);
			e.printStackTrace();
			ServletUtility.handleException(e, req, resp);
			return;
		}
		log.debug("FacultyListCtl doPost End");
	}

	protected String getView() {

		return ORSView.FACULTY_LIST_VIEW;
	}

}
