
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
import in.co.sunrays.proj3.model.CourseModelInt;
import in.co.sunrays.proj3.model.ModelFactory;
import in.co.sunrays.proj3.util.DataUtility;
import in.co.sunrays.proj3.util.PropertyReader;
import in.co.sunrays.proj3.util.ServletUtility;
import in.co.sunrays.project3.dto.BaseDTO;
import in.co.sunrays.project3.dto.CollegeDTO;

/**
 * College List functionality Controller. Performs operation for list, search
 * and delete operations of College
 * 
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */
@WebServlet(name = "CollegeListCtl", urlPatterns = "/ctl/CollegeListCtl")
public class CollegeListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(CollegeListCtl.class);

	protected void preload(HttpServletRequest request) {
		CollegeModelInt model = ModelFactory.getInstance().getCollegeModel();
		CourseModelInt crModel = ModelFactory.getInstance().getCourseModel();
		try {

			List l = model.list();
			List l1 = crModel.list();

			request.setAttribute("collegeList", l);
			request.setAttribute("courseList", l1);
			System.out.println("list of student " + l + " " + l1);
		} catch (ApplicationException e) {
			log.error(e);
		}

	}

	protected BaseDTO populateDto(HttpServletRequest request) {
		CollegeDTO dto = new CollegeDTO();

		dto.setId(DataUtility.getLong(request.getParameter("collegeId")));
		dto.setCity(DataUtility.getString(request.getParameter("city")));
		System.out.println("city------->" + dto.getCity());
		return dto;
	}

	/**
	 * View Logic
	 */

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		System.out.println("pageSize " + pageSize);
		CollegeDTO bean = (CollegeDTO) populateDto(req);
		CollegeModelInt model = ModelFactory.getInstance().getCollegeModel();

		List list = null;

		try {
			list = model.search(bean, pageNo, pageSize);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, req, resp);
			return;
		}
		System.out.println("in collge list " + list);

		if (list == null || list.size() == 0) {
			ServletUtility.setErrorMessage("No record found ", req);
		}

		ServletUtility.setList(list, req);
		ServletUtility.setPageNo(pageNo, req);
		ServletUtility.setPageSize(pageSize, req);
		ServletUtility.forward(getView(), req, resp);
	}

	/**
	 * Submit Logic
	 */

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.debug("CollegeListCtl doPost Start");

		List list = null;

		int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(req.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;

		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		CollegeDTO bean = (CollegeDTO) populateDto(req);

		String op = DataUtility.getString(req.getParameter("operation"));

		CollegeModelInt model = ModelFactory.getInstance().getCollegeModel();
		String[] ids = req.getParameterValues("ids");
		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op) || "Previous".equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					//// pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}

			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				//// pageNo = 1;
				if (ids != null && ids.length > 0) {
					CollegeDTO deleteDto = new CollegeDTO();
					for (String id : ids) {
						deleteDto.setId(DataUtility.getInt(id));
						model.delete(deleteDto);
					}
					ServletUtility.setSuccessMessage("Record has deleted successfully", req);
				} else {
					ServletUtility.setErrorMessage("Select at least one record", req);
				}
			} else if (OP_BACK.equalsIgnoreCase(op)) {
				pageNo = DataUtility.getInt(req.getParameter("pageNo1"));
				System.out.println("page" + pageNo);
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, req, resp);
				return;
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.COLLEGE_CTL, req, resp);
				return;
			}
			list = model.search(bean, pageNo, pageSize);
			ServletUtility.setList(list, req);
			if (list == null || list.size() == 0 && !(OP_DELETE.equalsIgnoreCase(op))) {
				ServletUtility.setErrorMessage("No record found ", req);
			}
			req.setAttribute("cbean", bean);
			ServletUtility.setList(list, req);

			ServletUtility.setPageNo(pageNo, req);
			ServletUtility.setPageSize(pageSize, req);
			ServletUtility.forward(getView(), req, resp);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, req, resp);
			return;
		}
		log.debug("CollegeListCtl doGet End");

	}

	protected String getView() {

		return ORSView.COLLEGE_LIST_VIEW;
	}

}
