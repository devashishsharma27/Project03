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
import in.co.sunrays.proj3.util.DataUtility;
import in.co.sunrays.proj3.util.PropertyReader;
import in.co.sunrays.proj3.util.ServletUtility;
import in.co.sunrays.project3.dto.BaseDTO;
import in.co.sunrays.project3.dto.CourseDTO;
import in.co.sunrays.project3.dto.SubjectDTO;

/**
 * Course List functionality Controller. Performs operation for list, search
 * and delete operations of Course
 * 
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */
@WebServlet(name="CourseListCtl",urlPatterns = "/ctl/CourseListCtl")
public class CourseListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(CourseListCtl.class);
    
	protected BaseDTO populateDTO(HttpServletRequest request) 
	{
		CourseDTO dto = new CourseDTO();
		dto.setCourseName(DataUtility.getString(request.getParameter("name")));
	dto.setId(DataUtility.getLong(request.getParameter("courseId")));
		return dto;
	}
	/**
	 * View Logic
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     
		log.debug("CourseListCtl doGet Start");

		List list = null;
		
		System.out.println("in courseListCtl");

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		CourseDTO dto = (CourseDTO) populateDTO(request);

		String op = DataUtility.getString(request.getParameter("operation"));

		CourseModelInt model = ModelFactory.getInstance().getCourseModel();

		try {
			list = model.search(dto, pageNo, pageSize);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			
     	} catch (ApplicationException e) {
			log.error(e);

			ServletUtility.handleException(e, request, response);
			return;
		}
		System.out.println("in courseListCtl");
		log.debug("CourseListCtl doGet End");
		
		ServletUtility.forward(ORSView.COURSE_LIST_VIEW, request, response);

	}
	
	/**
	 * Submit Logic
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
		log.debug("CourseListCtl do Post Start");

		List list = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		CourseDTO dto = (CourseDTO) populateDTO(request);

		String op = DataUtility.getString(request.getParameter("operation"));

		CourseModelInt model = ModelFactory.getInstance().getCourseModel();

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

	                     CourseDTO deletedDto=new CourseDTO();
						for (String id : ids) {
							deletedDto.setId(DataUtility.getLong(id));
							model.delete(deletedDto);
							ServletUtility.setSuccessMessage("Data deleted successfully", request);
						}
					} else {
						ServletUtility.setErrorMessage("Select at least one record", request);
					}
				}

				list = model.search(dto, pageNo, pageSize);
				ServletUtility.setList(list, request);
				if (list == null || list.size() == 0&&!OP_DELETE.equalsIgnoreCase(op)) {
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

			log.debug("CourseListCtl doPost Method End");

	}
	
	protected String getView() {
		
		return ORSView.COURSE_LIST_VIEW;
	}

}
