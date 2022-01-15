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
import in.co.sunrays.proj3.model.TimeTableModelInt;
import in.co.sunrays.proj3.model.TimeTableModelJDBCImp;
import in.co.sunrays.proj3.util.DataUtility;
import in.co.sunrays.proj3.util.PropertyReader;
import in.co.sunrays.proj3.util.ServletUtility;
import in.co.sunrays.project3.dto.BaseDTO;
import in.co.sunrays.project3.dto.TimetableDTO;

/**
 * TimeTable List functionality Controller. Performs operation for list, search
 * and delete operations of TimeTable
 * 
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 */
@WebServlet(name = "TimeTableListCtl", urlPatterns = "/ctl/TimeTableListCtl")
public class TimeTableListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(TimeTableListCtl.class);

	protected void preload(HttpServletRequest request) {
		CourseModelInt courseModel = ModelFactory.getInstance().getCourseModel();

		try {
			List listCourse = courseModel.list();
			request.setAttribute("cLIst", listCourse);

		} catch (ApplicationException e) {
			log.error(e);
		}
		SubjectModelInt subjectModel = ModelFactory.getInstance().getSubjectModel();
		try {
			List listSubject = subjectModel.list();
			request.setAttribute("sLIst", listSubject);
		} catch (ApplicationException e) {
			log.error(e);
		}
	}
    
	/**
	 * View Logic
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id = DataUtility.getLong(req.getParameter("id"));
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		TimetableDTO bean = (TimetableDTO) populateDTO(req);

		TimeTableModelInt model = ModelFactory.getInstance().getTimeTableModel();

		List list = null;
		try {
			list = model.search(bean, pageNo, pageSize);

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No Record Found", req);
			}
			System.out.println("pageNo....."+pageNo+"pageSize......"+pageSize);
			ServletUtility.setList(list, req);
			ServletUtility.setPageNo(pageNo, req);
			ServletUtility.setPageSize(pageSize, req);
		} catch (ApplicationException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			ServletUtility.handleException(e, req, resp);
			return;
		}
		ServletUtility.forward(getView(), req, resp);
	}
    /**
     * Submit Logic
     */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		List list = null;

		int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(req.getParameter("pageSize"));
		int count = 0;
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		TimetableDTO dto = (TimetableDTO) populateDTO(req);
		String op = DataUtility.getString(req.getParameter("operation"));

		String[] ids = req.getParameterValues("ids");
		TimeTableModelInt model = ModelFactory.getInstance().getTimeTableModel();
		try {
			if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {
				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.TIME_TABLE_CTL, req, resp);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				/// pageNo=1;
				if (ids != null && ids.length > 0) {
					TimetableDTO deleteBean = new TimetableDTO();
					for (String id : ids) {
						deleteBean.setId(DataUtility.getInt(id));
						model.delete(deleteBean);
						count++;
					}
					ServletUtility.setSuccessMessage("Record deleted successfully", req);
				} else {
					ServletUtility.setErrorMessage("Please Select at least one record ", req);
				}
			} else if (OP_BACK.equalsIgnoreCase(op)) {
				pageNo = DataUtility.getInt(req.getParameter("pageNo1"));
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.TIME_TABLE_LIST_CTL, req, resp);
				return;
			}
			list = model.search(dto, pageNo, pageSize);
			ServletUtility.setList(list, req);
			if (list == null || list.size() == 0 && (!OP_DELETE.equalsIgnoreCase(op))) {
				ServletUtility.setErrorMessage("No Record Found", req);
			}
			req.setAttribute("dto", dto);
			// ServletUtility.setBean(bean, request);
			ServletUtility.setList(list, req);
			ServletUtility.setPageNo(pageNo, req);
			ServletUtility.setPageSize(pageSize, req);

		} catch (ApplicationException e) {
			e.printStackTrace();
			ServletUtility.handleException(e, req, resp);
			return;
		}
		ServletUtility.forward(getView(), req, resp);
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {
		TimetableDTO dto = new TimetableDTO();
		// bean.setCourseName(DataUtility.getString(request.getParameter("cName")));
		dto.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
		// bean.setSubjectId(DataUtility.getLong(request.getParameter("roleName")));
		dto.setSemester(DataUtility.getString(request.getParameter("semester")));
       
		dto.setExamDate(DataUtility.getDate(request.getParameter("examDate")));
		System.out.println(dto.getExamDate());

		return dto;
	}

	protected String getView() {

		return ORSView.TIME_TABLE_LIST_VIEW;
	}

}
