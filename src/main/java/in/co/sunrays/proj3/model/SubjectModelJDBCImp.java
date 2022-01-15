package in.co.sunrays.proj3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.exception.DataBaseException;
import in.co.sunrays.proj3.exception.DuplicateRecordException;
import in.co.sunrays.proj3.util.JDBCDataSource;
import in.co.sunrays.project3.dto.CourseDTO;
import in.co.sunrays.project3.dto.StudentDTO;
import in.co.sunrays.project3.dto.SubjectDTO;

public class SubjectModelJDBCImp implements SubjectModelInt{

	 private static Logger log = Logger.getLogger(SubjectModelJDBCImp.class);

	    /**
	     * Find next PK of Subject
	     * 
	     * @throws DatabaseException
	     */
	    public Integer nextPK() throws DataBaseException {
	        log.debug("Model nextPK Started");
	        Connection conn = null;
	        int pk = 0;
	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn
	                    .prepareStatement("SELECT MAX(ID) FROM ST_SUBJECT");
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
	     * Add a Subject
	     * 
	     * @param bean
	     * @throws DatabaseException
	     * 
	     */
	    public long add(SubjectDTO bean) throws ApplicationException,
	            DuplicateRecordException {
	        log.debug("Model add Started");
	        Connection conn = null;

	        // get Course Name
	        CourseModelJDBCImp cModel = new CourseModelJDBCImp();
	        CourseDTO CourseBean = cModel.findByPK(bean.getCourseId());
	        bean.setCourseName(CourseBean.getCourseName());

	        SubjectDTO duplicateName = findByName(bean.getSubjectName());
	        int pk = 0;

	        if (duplicateName != null) {
	            throw new DuplicateRecordException("Subject Name already exists");
	        }

	        try {
	            conn = JDBCDataSource.getConnection();
	            pk = nextPK();
	            // Get auto-generated next primary key
	            System.out.println(pk + " in ModelJDBC");
	            conn.setAutoCommit(false); // Begin transaction
	            PreparedStatement pstmt = conn
	                    .prepareStatement("INSERT INTO ST_SUBJECT VALUES(?,?,?,?,?,?,?,?,?,?)");
	            pstmt.setInt(1, pk);
	            pstmt.setString(2, bean.getSemester());
	            pstmt.setString(3, bean.getSubjectName());
	            pstmt.setString(4, bean.getCourseName());
	            pstmt.setString(5, bean.getDescription());
	            pstmt.setLong(6, bean.getCourseId());
	           
	            pstmt.setString(7, bean.getCreatedBy());
	            pstmt.setString(8, bean.getModifiedBy());
	            pstmt.setTimestamp(9, bean.getCreatedDateTime());
	            pstmt.setTimestamp(10, bean.getModifiedDateTime());
	            pstmt.executeUpdate();
	            conn.commit(); // End transaction
	            pstmt.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	            log.error("Database Exception..", e);
	            try {
	                conn.rollback();
	            } catch (Exception ex) {
	                throw new ApplicationException(
	                        "Exception : add rollback exception " + ex.getMessage());
	            }
	            throw new ApplicationException(
	                    "Exception : Exception in add Subject");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	        log.debug("Model add End");
	        return pk;
	    }

	    /**
	     * Delete a Subject
	     * 
	     * @param bean
	     * @throws DatabaseException
	     */
	    public void delete(SubjectDTO bean) throws ApplicationException {
	        log.debug("Model delete Started");
	        Connection conn = null;
	        try {
	            conn = JDBCDataSource.getConnection();
	            conn.setAutoCommit(false); // Begin transaction
	            PreparedStatement pstmt = conn
	                    .prepareStatement("DELETE FROM ST_SUBJECT WHERE ID=?");
	            pstmt.setLong(1, bean.getId());
	            pstmt.executeUpdate();
	            conn.commit(); // End transaction
	            pstmt.close();

	        } catch (Exception e) {
	            log.error("Database Exception..", e);
	            try {
	                conn.rollback();
	            } catch (Exception ex) {
	                throw new ApplicationException(
	                        "Exception : Delete rollback exception "
	                                + ex.getMessage());
	            }
	            throw new ApplicationException(
	                    "Exception : Exception in delete Subject");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	        log.debug("Model delete Started");
	    }

	    /**
	     * Find User by Subject
	     * 
	     * @param Email
	     *            : get parameter
	     * @return bean
	     * @throws DatabaseException
	     */

	    public SubjectDTO findByName(String name) throws ApplicationException {
	        log.debug("Model findBy Name Started");
	        StringBuffer sql = new StringBuffer(
	                "SELECT * FROM ST_SUBJECT WHERE  Subject_NAME=?");
	        SubjectDTO bean = null;
	        Connection conn = null;
	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setString(1, name);
	            ResultSet rs = pstmt.executeQuery();
	            while (rs.next()) {
	                bean = new SubjectDTO();
	                bean.setId(rs.getLong(1));
	                bean.setSemester(rs.getString(2));
	                bean.setSubjectName(rs.getString(3));
	                bean.setCourseName(rs.getString(4));
	                bean.setDescription(rs.getString(5));
	                bean.setCourseId(rs.getLong(6));
	                
	                bean.setCreatedBy(rs.getString(7));
	                bean.setModifiedBy(rs.getString(8));
	                bean.setCreatedDateTime(rs.getTimestamp(9));
	                bean.setModifiedDateTime(rs.getTimestamp(10));
	            }
	            rs.close();
	        } catch (Exception e) {
	            log.error("Database Exception..", e);
	            throw new ApplicationException(
	                    "Exception : Exception in getting Subject by Name");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	        log.debug("Model findBy Name End");
	        return bean;
	    }

	    /**
	     * Find Subject by PK
	     * 
	     * @param pk
	     *            : get parameter
	     * @return bean
	     * @throws DatabaseException
	     */

	    public SubjectDTO findByPK(long pk) throws ApplicationException {
	        log.debug("Model findByPK Started");
	        StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT WHERE ID=?");
	        SubjectDTO bean = null;
	        Connection conn = null;
	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setLong(1, pk);
	            ResultSet rs = pstmt.executeQuery();
	            while (rs.next()) {
	                bean = new SubjectDTO();
	                bean.setId(rs.getLong(1));
	                bean.setSemester(rs.getString(2));
	                bean.setSubjectName(rs.getString(3));
	                bean.setCourseName(rs.getString(4));
	                bean.setDescription(rs.getString(5));
	                bean.setCourseId(rs.getLong(6));
	                
	                bean.setCreatedBy(rs.getString(7));
	                bean.setModifiedBy(rs.getString(8));
	                bean.setCreatedDateTime(rs.getTimestamp(9));
	                bean.setModifiedDateTime(rs.getTimestamp(10));
	            }
	            rs.close();
	        } catch (Exception e) {
	            log.error("Database Exception..", e);
	            throw new ApplicationException(
	                    "Exception : Exception in getting Subject by pk");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	        log.debug("Model findByPK End");
	        return bean;
	    }

	    /**
	     * Update a Subject
	     * 
	     * @param bean
	     * @throws DatabaseException
	     */

	    public void update(SubjectDTO bean) throws ApplicationException,
	            DuplicateRecordException {
	        log.debug("Model update Started");
	        Connection conn = null;

	        SubjectDTO beanExist = findByName(bean.getCourseName());

	        // Check if updated id already exist
	        if (beanExist != null && beanExist.getId() != bean.getId()) {
	            throw new DuplicateRecordException("Subject Name is already exist");
	        }

	        // get Course Name
	        CourseModelJDBCImp cModel = new CourseModelJDBCImp();
	        CourseDTO CourseBean = cModel.findByPK(bean.getCourseId());
	        bean.setCourseName(CourseBean.getCourseName());

	        try {

	            conn = JDBCDataSource.getConnection();

	            conn.setAutoCommit(false); // Begin transaction
	            PreparedStatement pstmt = conn
	                    .prepareStatement("UPDATE ST_SUBJECT SET SEMESTER=?,subject_Name=?, Course_NAME=?,Description=?,Course_ID=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");
	            pstmt.setString(1, bean.getSemester());
	            pstmt.setString(2, bean.getSubjectName());
	            pstmt.setString(3, bean.getCourseName());
	            pstmt.setString(4, bean.getDescription());
	            pstmt.setLong(5, bean.getCourseId());
	            pstmt.setString(6, bean.getCreatedBy());
	            pstmt.setString(7, bean.getModifiedBy());
	            pstmt.setTimestamp(8, bean.getCreatedDateTime());
	            pstmt.setTimestamp(9, bean.getModifiedDateTime());
	            pstmt.setLong(10, bean.getId());
	            pstmt.executeUpdate();
	            conn.commit(); // End transaction
	            pstmt.close();
	        } catch (Exception e) {
	            log.error("Database Exception..", e);
	            try {
	                conn.rollback();
	            } catch (Exception ex) {
	                throw new ApplicationException(
	                        "Exception : Delete rollback exception "
	                                + ex.getMessage());
	            }
	            throw new ApplicationException("Exception in updating Subject ");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	        log.debug("Model update End");
	    }

	    /**
	     * Search Subject
	     * 
	     * @param bean
	     *            : Search Parameters
	     * @throws DatabaseException
	     */

	    public List search(SubjectDTO bean) throws ApplicationException {
	        return search(bean, 0, 0);
	    }

	    /**
	     * Search Subject with pagination
	     * 
	     * @return list : List of Subjects
	     * @param bean
	     *            : Search Parameters
	     * @param pageNo
	     *            : Current Page No.
	     * @param pageSize
	     *            : Size of Page
	     * 
	     * @throws DatabaseException
	     */

	    public List search(SubjectDTO bean, int pageNo, int pageSize)
	            throws ApplicationException {
	        log.debug("Model search Started");
	        StringBuffer sql = new StringBuffer("SELECT * FROM ST_SUBJECT WHERE 1=1");

	        if (bean != null) {
	            if (bean.getId() > 0) {
	                sql.append(" AND id = " + bean.getId());
	            }
	            if(bean.getCourseId()>0) {
	            	sql.append(" AND COURSE_ID = " + bean.getCourseId());
	            }
	            if (bean.getCourseName() != null && bean.getCourseName().trim().length() > 0) {
	                sql.append(" AND course_NAME like '" + bean.getCourseName()
	                        + "%'");
	            }
	            if (bean.getDescription() != null && bean.getDescription().length() > 0) {
	                sql.append(" AND DESCRIPTION like '" + bean.getDescription() + "%'");
	            }

	            if (bean.getSubjectName() != null
	                    && bean.getSubjectName().length() > 0) {
	                sql.append(" AND SUBJECT_NAME like '" + bean.getSubjectName()+"%'");
	            }

	        }

	        // if page size is greater than zero then apply pagination
	        if (pageSize > 0) {
	            // Calculate start record index
	            pageNo = (pageNo - 1) * pageSize;

	            sql.append(" Limit " + pageNo + ", " + pageSize);
	            // sql.append(" limit " + pageNo + "," + pageSize);
	        }
	         System.out.println(sql);
	        ArrayList list = new ArrayList();
	        Connection conn = null;
	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	            ResultSet rs = pstmt.executeQuery();
	            while (rs.next()) {
	                bean = new SubjectDTO();
	                bean.setId(rs.getLong(1));
	                bean.setSemester(rs.getString(2));
	                bean.setSubjectName(rs.getString(3));
	                bean.setCourseName(rs.getString(4));
	                bean.setDescription(rs.getString(5));
	                bean.setCourseId(rs.getLong(6));
	                
	                bean.setCreatedBy(rs.getString(7));
	                bean.setModifiedBy(rs.getString(8));
	                bean.setCreatedDateTime(rs.getTimestamp(9));
	                bean.setModifiedDateTime(rs.getTimestamp(10));
	                list.add(bean);
	            }
	            rs.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	            log.error("Database Exception..", e);
	            throw new ApplicationException(
	                    "Exception : Exception in search Subject");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        log.debug("Model search End");
	        return list;
	    }

	    /**
	     * Get List of Subject
	     * 
	     * @return list : List of Subject
	     * @throws DatabaseException
	     */

	    public List list() throws ApplicationException {
	        return list(0, 0);
	    }

	    /**
	     * Get List of Subject with pagination
	     * 
	     * @return list : List of Subject
	     * @param pageNo
	     *            : Current Page No.
	     * @param pageSize
	     *            : Size of Page
	     * @throws DatabaseException
	     */

	    public List list(int pageNo, int pageSize) throws ApplicationException {
	        log.debug("Model list Started");
	        ArrayList list = new ArrayList();
	        StringBuffer sql = new StringBuffer("select * from ST_SUBJECT");
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
	                SubjectDTO bean = new SubjectDTO();
	                bean.setId(rs.getLong(1));
	                bean.setSemester(rs.getString(2));
	                bean.setSubjectName(rs.getString(3));
	                bean.setCourseName(rs.getString(4));
	                bean.setDescription(rs.getString(5));
	                bean.setCourseId(rs.getLong(6));
	                
	                bean.setCreatedBy(rs.getString(7));
	                bean.setModifiedBy(rs.getString(8));
	                bean.setCreatedDateTime(rs.getTimestamp(9));
	                bean.setModifiedDateTime(rs.getTimestamp(10));
	                list.add(bean);
	            }
	            rs.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	            log.error("Database Exception..", e);
	            throw new ApplicationException(
	                    "Exception : Exception in getting list of Subject");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        log.debug("Model list End");
	        return list;

	    }

}
