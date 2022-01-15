package in.co.sunrays.proj3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.exception.DataBaseException;
import in.co.sunrays.proj3.exception.DuplicateRecordException;
import in.co.sunrays.proj3.exception.RecordNotFoundException;
import in.co.sunrays.proj3.util.JDBCDataSource;
import in.co.sunrays.project3.dto.CourseDTO;
import in.co.sunrays.project3.dto.SubjectDTO;
import in.co.sunrays.project3.dto.TimetableDTO;

public class TimeTableModelJDBCImp implements TimeTableModelInt  {

	private static Logger log = Logger.getLogger(TimeTableModelJDBCImp.class);

	/**
	 * Find next PK of TimeTable
	 * 
	 * @throws DatabaseException
	 */
	public Integer nextPK() throws DataBaseException {
		log.debug("Model nextPK Started");
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_TIMETABLE");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();

		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new DataBaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPK End");
		return pk + 1;
	}

	/**
	 * Add a TimeTable
	 * 
	 * @param bean
	 * @throws RecordNotFoundException
	 * @throws DatabaseException
	 * 
	 */

	/**
	 * semester validation according to course duration
	 * 
	 * @param cb
	 * @param tb
	 * @throws RecordNotFoundException
	 */
	public void semChecker(CourseDTO cb, TimetableDTO tb) throws RecordNotFoundException {
		String duration = cb.getDuration();

		
		if (duration.equalsIgnoreCase("3 years") && (tb.getSemester().equalsIgnoreCase("VII")
				|| tb.getSemester().equalsIgnoreCase("VIII"))) {
		
			throw new RecordNotFoundException("This Semester is not available for this course");

		}
		else if (duration.equalsIgnoreCase("2 years") && (tb.getSemester().equalsIgnoreCase("V")
				|| tb.getSemester().equalsIgnoreCase("VI") || tb.getSemester().equalsIgnoreCase("VII")
				|| tb.getSemester().equalsIgnoreCase("VIII"))) {
			throw new RecordNotFoundException("This Semester is not available for this course");

		}else{}
	}

	/**
	 * Add timetable
	 * 
	 * @param bean
	 * @return
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 * @throws RecordNotFoundException
	 */
	public long add(TimetableDTO bean) throws ApplicationException, DuplicateRecordException, RecordNotFoundException 
	{

		log.debug("Model add started");
		Connection conn = null;

		CourseModelJDBCImp cModel = new CourseModelJDBCImp();
		CourseDTO courseBean = cModel.findByPK(bean.getCourseId());
		bean.setCourseName(courseBean.getCourseName());

		SubjectModelJDBCImp sModel = new SubjectModelJDBCImp();
		SubjectDTO subjectBean = sModel.findByPK(bean.getSubjectId());
		if (subjectBean != null) {
			if (subjectBean.getCourseId() != bean.getCourseId()) {

				throw new RecordNotFoundException("This Subject is not Available For This Course");
			}
		}
		bean.setSubjectName(subjectBean.getSubjectName());

		semChecker(courseBean, bean);

		TimetableDTO duplicatename = findTimeTableDuplicacy(bean.getCourseId(), bean.getSemester(),
				bean.getExamDate());

		TimetableDTO duplicatename1 = findTimeTableDuplicacy(bean.getCourseId(), bean.getSemester(),
				bean.getSubjectId());

		TimetableDTO duplicatename2 = findTimeTableDuplicacy(bean.getCourseId(), bean.getSubjectId(),
				bean.getExamDate());

		int pk = 0;

		if (duplicatename1 != null || duplicatename != null || duplicatename2 != null) {
			throw new DuplicateRecordException("Time Table already exist");

		}

		/*
		 * if (duplicatename != null) { throw new
		 * DuplicateRecordException("Time Table already exist"); }
		 */
		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();
			// Get auto-generated next primary key

			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement psmt = conn.prepareStatement("INSERT INTO ST_TIMETABLE VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
			psmt.setInt(1, pk);
			
			psmt.setLong(2, bean.getSubjectId());
			psmt.setString(3, bean.getSubjectName());
			psmt.setLong(4, bean.getCourseId());
			psmt.setString(5, bean.getCourseName());
			psmt.setString(6, bean.getSemester());
			psmt.setDate(7, new java.sql.Date(bean.getExamDate().getTime()));
			psmt.setString(8, bean.getExamTime());
			psmt.setString(9, bean.getCreatedBy());
			psmt.setString(10, bean.getModifiedBy());
			psmt.setTimestamp(11, bean.getCreatedDateTime());
			psmt.setTimestamp(12, bean.getModifiedDateTime());

			psmt.executeUpdate();
			conn.commit();// End Transaction
			psmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add TimeTable");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model add End");
		return pk;
	}

	/**
	 * Delete a TimeTable
	 * 
	 * @param bean
	 * @throws ApplicationException
	 * @throws DatabaseException
	 */
	public void delete(TimetableDTO bean) throws ApplicationException {
		log.debug("Model delete Started");
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement psmt = conn.prepareStatement("DELETE FROM ST_TIMETABLE WHERE ID=?");
			psmt.setLong(1, bean.getId());
			psmt.executeUpdate();
			conn.commit();
			psmt.close();

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete TimeTable");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model delete Ended");
	}

	/**
	 * Update a TimeTable
	 * 
	 * @param bean
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 * @throws RecordNotFoundException
	 * @throws @throws
	 *             DatabaseException
	 */

	public void update(TimetableDTO bean)
			throws ApplicationException, DuplicateRecordException, RecordNotFoundException {
		log.debug("Model update Started");
		Connection conn = null;

		// check duplicacy
		
		
		
		
		
		
		
		/*TimeTableBean duplicatename = findTimeTableDuplicacy(bean.getCourseId(), bean.getSemester(),
				bean.getExamDate());

		TimeTableBean duplicatename1 = findTimeTableDuplicacy(bean.getCourseId(),bean.getSemester(), bean.getSubId()
			);
		
		
		TimeTableBean duplicatename2=	findTimeTableDuplicacy(bean.getCourseId(), bean.getSubId(), bean.getExamDate());

		if ((duplicatename != null && duplicatename.getId() != bean.getId())||(duplicatename1!=null&&duplicatename1.getId()!=bean.getId())
				||(duplicatename2!=null&&duplicatename2.getId()!=bean.getId())) {
			throw new DuplicateRecordException("Time Table already exist");
		}
*/
		CourseModelJDBCImp cModel = new CourseModelJDBCImp();
		CourseDTO courseBean = cModel.findByPK(bean.getCourseId());
		bean.setCourseName(courseBean.getCourseName());

		SubjectModelJDBCImp sModel = new SubjectModelJDBCImp();
		SubjectDTO subjectBean = sModel.findByPK(bean.getSubjectId());
		bean.setSubjectName(subjectBean.getSubjectName());

		/*if (subjectBean != null) {
			if (subjectBean.getCourseId() != bean.getCourseId()) {

				throw new RecordNotFoundException("This Subject is not Available For This Course");
			}
		}
		semChecker(courseBean, bean);*/

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement psmt = conn.prepareStatement(
					"UPDATE ST_TIMETABLE SET Subject_ID=?,Subject_NAME=?,Course_ID=?,Course_NAME=?,SEMESTER=?,EXAMDATE=?,EXAM_TIME=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");

			
			psmt.setLong(1, bean.getSubjectId());
			psmt.setString(2, bean.getSubjectName());
			psmt.setLong(3, bean.getCourseId());
			psmt.setString(4, bean.getCourseName());
			psmt.setString(5, bean.getSemester());
			psmt.setDate(6, new java.sql.Date(bean.getExamDate().getTime()));
			psmt.setString(7, bean.getExamTime());
			psmt.setString(8, bean.getCreatedBy());
			psmt.setString(9, bean.getModifiedBy());
			psmt.setTimestamp(10, bean.getCreatedDateTime());
			psmt.setTimestamp(11, bean.getModifiedDateTime());
			psmt.setLong(12, bean.getId());
			psmt.executeUpdate();
			conn.commit();
			psmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in updating TimeTable ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model update End");

	}

	/**
	 * Find TimeTableDuplicacy course id ,semester,exam date
	 * 
	 * @throws ApplicationException
	 */
	public TimetableDTO findTimeTableDuplicacy(Long courseId, String Semester, Date examdate)
			throws ApplicationException {
		log.debug("Method FindTimeTable of Model TimeTable started");
		System.out.println("in find timetable");
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ST_TIMETABLE WHERE COURSE_ID=?  AND SEMESTER=? AND EXAMDATE = ?");
		TimetableDTO bean = null;
		Connection conn = null;
		System.out.println(sql);
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, courseId);
			pstmt.setString(2, Semester);
			pstmt.setDate(3, new java.sql.Date(examdate.getTime()));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimetableDTO();
				bean.setId(rs.getLong(1));
				bean.setSubjectId(rs.getInt(2));
				bean.setSubjectName(rs.getString(3));
				bean.setCourseId(rs.getLong(4));
				bean.setCourseName(rs.getString(5));
				bean.setSemester(rs.getString(6));
				bean.setExamDate(rs.getDate(7));
				bean.setExamTime(rs.getString(8));
				bean.setCreatedBy(rs.getString(9));
				bean.setModifiedBy(rs.getString(10));
				bean.setCreatedDateTime(rs.getTimestamp(11));
				bean.setModifiedDateTime(rs.getTimestamp(12));
			}

			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting TimeTable");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Method FindTimeTable of Model TimeTable End");
		return bean;

	}

	public TimetableDTO findTimeTableDuplicacy(Long courseId, long subjectId, Date examDate)
			throws ApplicationException {
		log.debug("Method FindTimeTable of Model TimeTable started");
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ST_TIMETABLE WHERE COURSE_ID=?  AND SUBJECT_ID=? AND EXAMDATE= ?");
		TimetableDTO bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, courseId);
			pstmt.setDate(3, new java.sql.Date(examDate.getTime()));
			pstmt.setLong(2, subjectId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimetableDTO();
				bean.setId(rs.getLong(1));
				bean.setSubjectId(rs.getInt(2));
				bean.setSubjectName(rs.getString(3));
				bean.setCourseId(rs.getLong(4));
				bean.setCourseName(rs.getString(5));
				bean.setSemester(rs.getString(6));
				bean.setExamDate(rs.getDate(7));
				bean.setExamTime(rs.getString(8));
				bean.setCreatedBy(rs.getString(9));
				bean.setModifiedBy(rs.getString(10));
				bean.setCreatedDateTime(rs.getTimestamp(11));
				bean.setModifiedDateTime(rs.getTimestamp(12));
			}

			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting TimeTable");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Method FindTimeTable of Model TimeTable End");
		return bean;

	}

	/**
	 * Find TimeTableDuplicacy by Course id ,semester, subject id
	 * 
	 * @throws ApplicationException
	 */
	public TimetableDTO findTimeTableDuplicacy(Long courseId, String Semester, long subjectId)
			throws ApplicationException {
		log.debug("Method FindTimeTable of Model TimeTable started");
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ST_TIMETABLE WHERE COURSE_ID=?  AND SEMESTER=? AND SUBJECT_ID = ?");
		TimetableDTO bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, courseId);
			pstmt.setString(2, Semester);
			pstmt.setLong(3, subjectId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimetableDTO();
				bean.setId(rs.getLong(1));
				bean.setSubjectId(rs.getInt(2));
				bean.setSubjectName(rs.getString(3));
				bean.setCourseId(rs.getLong(4));
				bean.setCourseName(rs.getString(5));
				bean.setSemester(rs.getString(6));
				bean.setExamDate(rs.getDate(7));
				bean.setExamTime(rs.getString(8));
				bean.setCreatedBy(rs.getString(9));
				bean.setModifiedBy(rs.getString(10));
				bean.setCreatedDateTime(rs.getTimestamp(11));
				bean.setModifiedDateTime(rs.getTimestamp(12));
			}

			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting TimeTable");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Method FindTimeTable of Model TimeTable End");
		return bean;

	}

	/**
	 * Find TimeTable by PK
	 * 
	 * @param pk
	 *            : get parameter
	 * @return bean
	 * @throws ApplicationException
	 * 
	 */

	public TimetableDTO findByPK(long pk) throws ApplicationException {
		log.debug("Model findByPK Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM ST_TIMETABLE WHERE ID=?");
		TimetableDTO bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimetableDTO();
				bean.setId(rs.getLong(1));
				
				bean.setSubjectId(rs.getInt(2));
				bean.setSubjectName(rs.getString(3));
				bean.setCourseId(rs.getLong(4));
				bean.setCourseName(rs.getString(5));
				bean.setSemester(rs.getString(6));
				bean.setExamDate(rs.getDate(7));
				bean.setExamTime(rs.getString(8));
				bean.setCreatedBy(rs.getString(9));
				bean.setModifiedBy(rs.getString(10));
				bean.setCreatedDateTime(rs.getTimestamp(11));
				bean.setModifiedDateTime(rs.getTimestamp(12));
			}

			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting TimeTable by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findByPK End");

		return bean;
	}

	/**
	 * Search TimeTable
	 * 
	 * @param bean
	 *            : Search Parameters
	 * @throws ApplicationException
	 * @throws DatabaseException
	 */

	public List search(TimetableDTO bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Search TimeTable with pagination
	 * 
	 * @return list : List of TimeTable
	 * @param bean
	 *            : Search Parameters
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * @throws ApplicationException
	 * 
	 * @throws DatabaseException
	 */
	public List search(TimetableDTO bean, int pageNo, int PageSize) throws ApplicationException {
		log.debug("Model search Started");
		StringBuffer sql = new StringBuffer("Select * From St_TimeTable Where 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());
			}
			if (bean.getCourseId() > 0) {

				sql.append(" AND COURSE_ID = " + bean.getCourseId());
			}
			if (bean.getSubjectId() > 0) {

				sql.append(" AND SUBJECT_ID = " + bean.getSubjectId());
			}
			if (bean.getCourseName() != null && bean.getCourseName().length() > 0) {
				sql.append(" AND COURSE_NAME like '" + bean.getCourseName() + "%'");
			}
			if (bean.getSubjectName() != null && bean.getSubjectName().trim().length() > 0) {
				sql.append(" AND SUBJECT_NAME like '" + bean.getSubjectName() + "%'");
			}
			if (bean.getSemester() != null && bean.getSemester().length() > 0) {
				sql.append(" AND SEMESTER like '" + bean.getSemester() + "'");
			}
			if (bean.getExamDate() != null) {

				sql.append(
						" AND EXAMDATE = '" +new java.sql.Date(bean.getExamDate().getTime()) + "'");
			}

		}

		// if pageSize1 is greater than zero then apply pagination
		if (PageSize > 0) {
			// calculate start record index
			pageNo = (pageNo - 1) * PageSize;
			sql.append(" limit " + pageNo + "," + PageSize);
		}
		System.out.println(sql);
		ArrayList list = new ArrayList();
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement psmt = conn.prepareStatement(sql.toString());
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {

				bean = new TimetableDTO();
				bean.setId(rs.getLong(1));
				bean.setSubjectId(rs.getInt(2));
				bean.setSubjectName(rs.getString(3));
				bean.setCourseId(rs.getLong(4));
				bean.setCourseName(rs.getString(5));
				bean.setSemester(rs.getString(6));

				bean.setExamDate(rs.getDate(7));
				bean.setExamTime(rs.getString(8));
				bean.setCreatedBy(rs.getString(9));
				bean.setModifiedBy(rs.getString(10));
				bean.setCreatedDateTime(rs.getTimestamp(11));
				bean.setModifiedDateTime(rs.getTimestamp(12));

				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in search TimeTable");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model search End");
		return list;
	}

	/**
	 * Get List of TimeTable
	 * 
	 * @return list : List of TimeTable
	 * @throws DatabaseException
	 */

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * Get List of TimeTable with pagination
	 * 
	 * @return list : List of TimeTable
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * @throws ApplicationException
	 * @throws DatabaseException
	 */
	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from st_timetable");
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				TimetableDTO bean = new TimetableDTO();
				bean.setId(rs.getLong(1));
				bean.setSubjectId(rs.getInt(2));
				bean.setSubjectName(rs.getString(3));
				bean.setCourseId(rs.getLong(4));
				bean.setCourseName(rs.getString(5));
				bean.setSemester(rs.getString(6));
				bean.setExamDate(rs.getDate(7));
				bean.setExamTime(rs.getString(8));
				bean.setCreatedBy(rs.getString(9));
				bean.setModifiedBy(rs.getString(10));
				bean.setCreatedDateTime(rs.getTimestamp(11));
				bean.setModifiedDateTime(rs.getTimestamp(12));

				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting list of TimeTable");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model list End");
		return list;
	}

	

}
