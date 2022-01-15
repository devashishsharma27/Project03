package in.co.sunrays.proj3.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.model.CourseModelInt;
import in.co.sunrays.proj3.model.ModelFactory;
import in.co.sunrays.proj3.model.SubjectModelInt;
import in.co.sunrays.proj3.util.DataUtility;
import in.co.sunrays.proj3.util.PropertyReader;
import in.co.sunrays.proj3.util.ServletUtility;
import in.co.sunrays.project3.dto.BaseDTO;
import in.co.sunrays.project3.dto.CourseDTO;
import in.co.sunrays.project3.dto.SubjectDTO;

/**
 * Subject List functionality Controller. Performs operation for list, search
 * and delete operations of Subject
 * 
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */
@WebServlet(name = "SubjectListCtl", urlPatterns = "/ctl/SubjectListCtl")
public class SubjectListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(SubjectListCtl.class);

	protected void preload(HttpServletRequest request) {

		List list = null;
		CourseModelInt model = ModelFactory.getInstance().getCourseModel();
		try {
			list = model.list();
			request.setAttribute("courseList", list);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

	}

	protected BaseDTO populateDTO(HttpServletRequest request) {
		SubjectDTO dto = new SubjectDTO();
		dto.setSubjectName(DataUtility.getString(request.getParameter("subName")));
		dto.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
		return dto;
	}

	/**
	 * View Logic
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.debug("SubjectListCtl do get Method started");

		List list = null;
		int pageNo = DataUtility.getInt(req.getParameter("pageNo"));

		int pageSize = DataUtility.getInt(req.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;

		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		System.out.println("pageNo  " + pageNo + "   pageSize  " + pageSize);

		SubjectModelInt model = ModelFactory.getInstance().getSubjectModel();

		SubjectDTO dto = (SubjectDTO) populateDTO(req);

		try {
			list = model.search(dto, pageNo, pageSize);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", req);
			}
			System.out.println("in do get");
			ServletUtility.setList(list, req);
			ServletUtility.setPageNo(pageNo, req);
			ServletUtility.setPageSize(pageSize, req);
			ServletUtility.forward(getView(), req, resp);
		} catch (ApplicationException e) {
			ServletUtility.handleException(e, req, resp);
		}
		log.debug("SubjectListCtl do get Method ended");
	}

	/**
	 * Submit Logic
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("SubjectListCtl do post Method started");

		List list = null;
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));

		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		String op = DataUtility.getString(request.getParameter("operation"));
		pageNo = (pageNo == 0) ? 0 : pageNo;

		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		SubjectModelInt model = ModelFactory.getInstance().getSubjectModel();

		SubjectDTO dto = (SubjectDTO) populateDTO(request);

		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op) || "Previous".equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}

			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				// get the selected checkbox ids array for delete list
				String[] ids = request.getParameterValues("ids");
				if (ids != null && ids.length > 0) {

					SubjectDTO deletedDto = new SubjectDTO();
					for (String id : ids) {
						deletedDto.setId(DataUtility.getLong(id));
						model.delete(deletedDto);
						ServletUtility.setSuccessMessage("Data deleted successfully", request);
					}
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}
			}

			if (OP_RESET.equalsIgnoreCase(op)) {
				System.out.println("in reset");
				ServletUtility.redirect(ORSView.SUBJECT_LIST_CTL, request, response);
				return;
			}
			list = model.search(dto, pageNo, pageSize);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);

			ServletUtility.handleException(e, request, response);
			return;
		}

		log.debug("SubjectListCtl doPost Method End");
	}

	protected String getView() {

		return ORSView.SUBJECT_LIST_VIEW;
	}

}
