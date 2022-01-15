package in.co.sunrays.proj3.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.exception.DataBaseException;
import in.co.sunrays.proj3.exception.DuplicateRecordException;
import in.co.sunrays.proj3.util.JDBCDataSource;
import in.co.sunrays.project3.dto.CollegeDTO;
import in.co.sunrays.project3.dto.CourseDTO;
import in.co.sunrays.project3.dto.StudentDTO;

public class StudentModelJDBCImp implements StudentModelInt {

	private static Logger log = Logger.getLogger(StudentModelJDBCImp.class);

    /**
     * Find next PK of Student
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
                    .prepareStatement("SELECT MAX(ID) FROM St_STUDENT");
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
     * Add a Student
     *
     * @param bean
     * @throws DatabaseException
     *
     */
    public long add(StudentDTO bean) throws ApplicationException,
            DuplicateRecordException {
        log.debug("Model add Started");
        Connection conn = null;

        // get College Name
        CollegeModelJDBCImp cModel = new CollegeModelJDBCImp();
        CollegeDTO collegeBean = cModel.findByPk(bean.getCollegeId());
        
        bean.setCollegeName(collegeBean.getName());
        CourseDTO crBean=new CourseDTO();
        CourseModelJDBCImp crModel=new CourseModelJDBCImp();
        crBean= crModel.findByPK(bean.getCourseId());
        bean.setCourseName(crBean.getCourseName());

        StudentDTO duplicateName = findByEmailId(bean.getEmail());
        int pk = 0;

        if (duplicateName != null) {
            throw new DuplicateRecordException("Email already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            pk = nextPK();
            // Get auto-generated next primary key
            System.out.println(pk + " in ModelJDBC");
            conn.setAutoCommit(false); // Begin transaction
            PreparedStatement pstmt = conn
                    .prepareStatement("INSERT INTO st_STUDENT VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setInt(1, pk);
            pstmt.setLong(2, bean.getCollegeId());
            pstmt.setString(3, bean.getCollegeName());
            pstmt.setLong(4, bean.getCourseId());
            pstmt.setString(5, bean.getCourseName());
            pstmt.setString(6, bean.getFirstName());
            pstmt.setString(7, bean.getLastName());
            pstmt.setDate(8, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(9, bean.getMobileNo());
            pstmt.setString(10, bean.getEmail());
            pstmt.setString(11, bean.getCreatedBy());
            pstmt.setString(12, bean.getModifiedBy());
            pstmt.setTimestamp(13, bean.getCreatedDateTime());
            pstmt.setTimestamp(14, bean.getModifiedDateTime());
            pstmt.executeUpdate();
            conn.commit(); // End transaction
            pstmt.close();
        } catch (Exception e) {
            log.error("Database Exception..", e);
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException(
                        "Exception : add rollback exception " + ex.getMessage());
            }
            throw new ApplicationException(
                    "Exception : Exception in add Student");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model add End");
        return pk;
    }

    /**
     * Delete a Student
     *
     * @param bean
     * @throws DatabaseException
     */
    public void delete(StudentDTO bean) throws ApplicationException {
        log.debug("Model delete Started");
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false); // Begin transaction
            PreparedStatement pstmt = conn
                    .prepareStatement("DELETE FROM st_STUDENT WHERE ID=?");
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
                    "Exception : Exception in delete Student");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model delete Started");
    }

    /**
     * Find User by Student
     *
     * @param Email
     *            : get parameter
     * @return bean
     * @throws DatabaseException
     */

    public StudentDTO findByEmailId(String Email) throws ApplicationException {
        log.debug("Model findBy Email Started");
        StringBuffer sql = new StringBuffer(
                "SELECT * FROM ST_STUDENT WHERE EMAIL=?");
        StudentDTO bean = null;
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, Email);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new StudentDTO();
                bean.setId(rs.getLong(1));
                bean.setCollegeId(rs.getLong(2));
                bean.setCollegeName(rs.getString(3));
                bean.setCourseId(rs.getLong(4));
                bean.setCourseName(rs.getString(5));
                bean.setFirstName(rs.getString(6));
                bean.setLastName(rs.getString(7));
                bean.setDob(rs.getDate(8));
                bean.setMobileNo(rs.getString(9));
                bean.setEmail(rs.getString(10));
                bean.setCreatedBy(rs.getString(11));
                bean.setModifiedBy(rs.getString(12));
                bean.setCreatedDateTime(rs.getTimestamp(13));
                bean.setModifiedDateTime(rs.getTimestamp(14));

            }
            rs.close();
        } catch (Exception e) {
            log.error("Database Exception..", e);
            throw new ApplicationException(
                    "Exception : Exception in getting User by Email");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model findBy Email End");
        return bean;
    }

    /**
     * Find Student by PK
     *
     * @param pk
     *            : get parameter
     * @return bean
     * @throws DatabaseException
     */

    public StudentDTO findByPK(long pk) throws ApplicationException {
        log.debug("Model findByPK Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM ST_STUDENT WHERE ID=?");
        StudentDTO bean = null;
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, pk);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new StudentDTO();
                bean.setId(rs.getLong(1));
                bean.setCollegeId(rs.getLong(2));
                bean.setCollegeName(rs.getString(3));
                bean.setCourseId(rs.getLong(4));
                bean.setCourseName(rs.getString(5));
                bean.setFirstName(rs.getString(6));
                bean.setLastName(rs.getString(7));
                bean.setDob(rs.getDate(8));
                bean.setMobileNo(rs.getString(9));
                bean.setEmail(rs.getString(10));
                bean.setCreatedBy(rs.getString(11));
                bean.setModifiedBy(rs.getString(12));
                bean.setCreatedDateTime(rs.getTimestamp(13));
                bean.setModifiedDateTime(rs.getTimestamp(14));
            }
            rs.close();
        } catch (Exception e) {
            log.error("Database Exception..", e);
            throw new ApplicationException(
                    "Exception : Exception in getting User by pk");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model findByPK End");
        return bean;
    }

    /**
     * Update a Student
     *
     * @param bean
     * @throws DatabaseException
     */

    public void update(StudentDTO bean) throws ApplicationException,
            DuplicateRecordException {
        log.debug("Model update Started");
        Connection conn = null;

        StudentDTO beanExist = findByEmailId(bean.getEmail());

        // Check if updated Roll no already exist
        if (beanExist != null && beanExist.getId() != bean.getId()) {
            throw new DuplicateRecordException("Email Id is already exist");
        }

        // get College Name
        CollegeModelJDBCImp cModel = new CollegeModelJDBCImp();
        CollegeDTO collegeBean = cModel.findByPk(bean.getCollegeId());
        bean.setCollegeName(collegeBean.getName());
        CourseDTO crBean=new CourseDTO();
        CourseModelJDBCImp crModel=new CourseModelJDBCImp();
        crBean=crModel.findByPK(bean.getCourseId());
        bean.setCourseName(crBean.getCourseName());
         System.out.println(new java.sql.Date(bean.getDob().getTime()));
         Date d=new java.sql.Date(bean.getDob().getTime());
         System.out.println(d);
        try {

            conn = JDBCDataSource.getConnection();

            conn.setAutoCommit(false); // Begin transaction
            PreparedStatement pstmt = conn
                    .prepareStatement("UPDATE St_STUDENT SET COLLEGE_ID=?,COLLEGE_NAME=? ,Course_ID=?,course_name=?,FIRST_NAME=?,LAST_NAME=?,dob=?,MOBILE_NO=?,EMAIL=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");
            pstmt.setLong(1, bean.getCollegeId());
            pstmt.setString(2, bean.getCollegeName());
            pstmt.setLong(3, bean.getCourseId());
            pstmt.setString(4, bean.getCourseName());
            pstmt.setString(5, bean.getFirstName());
            pstmt.setString(6, bean.getLastName());
            pstmt.setDate(7, new java.sql.Date(bean.getDob().getTime()));
            pstmt.setString(8, bean.getMobileNo());
            pstmt.setString(9, bean.getEmail());
            pstmt.setString(10, bean.getCreatedBy());
            pstmt.setString(11, bean.getModifiedBy());
            pstmt.setTimestamp(12, bean.getCreatedDateTime());
            pstmt.setTimestamp(13, bean.getModifiedDateTime());
            pstmt.setLong(14, bean.getId());
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
                        "Exception : Delete rollback exception "
                                + ex.getMessage());
            }
            throw new ApplicationException("Exception in updating Student ");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
        log.debug("Model update End");
    }

    /**
     * Search Student
     *
     * @param bean
     *            : Search Parameters
     * @throws DatabaseException
     */

    public List search(StudentDTO bean) throws ApplicationException {
        return search(bean, 0, 0);
    }

    /**
     * Search Student with pagination
     *
     * @return list : List of Students
     * @param bean
     *            : Search Parameters
     * @param pageNo
     *            : Current Page No.
     * @param pageSize
     *            : Size of Page
     *
     * @throws DatabaseException
     */

    public List search(StudentDTO bean, int pageNo, int pageSize)
            throws ApplicationException {
        log.debug("Model search Started");
        StringBuffer sql = new StringBuffer("SELECT * FROM St_STUDENT WHERE 1=1");

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" AND id = " + bean.getId());
            }
            if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
                sql.append(" AND FIRST_NAME like '" + bean.getFirstName()
                        + "%'");
            }
            if (bean.getLastName() != null && bean.getLastName().length() > 0) {
                sql.append(" AND LAST_NAME like '" + bean.getLastName() + "%'");
            }
            if (bean.getDob() != null && bean.getDob().getDate() > 0) {
                sql.append(" AND DOB = " + bean.getDob());
            }
            if (bean.getMobileNo() !=null) {
                sql.append(" AND MOBILE_NO like '" + bean.getMobileNo() + "%'");
            }
            if (bean.getEmail() != null && bean.getEmail().length() > 0) {
                sql.append(" AND EMAIL like '" + bean.getEmail() + "%'");
            }
            if (bean.getCollegeId()!=0) {
                sql.append(" AND COLLEGE_ID = " + bean.getCollegeId());
            }

        }

        // if page size is greater than zero then apply pagination
        if (pageSize > 0) {
            // Calculate start record index
            pageNo = (pageNo - 1) * pageSize;

            sql.append(" Limit " + pageNo + ", " + pageSize);
            // sql.append(" limit " + pageNo + "," + pageSize);
        }

        ArrayList list = new ArrayList();
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new StudentDTO();
                bean.setId(rs.getLong(1));
                bean.setCollegeId(rs.getLong(2));
                bean.setCollegeName(rs.getString(3));
                bean.setCourseId(rs.getLong(4));
                bean.setCourseName(rs.getString(5));
                bean.setFirstName(rs.getString(6));
                bean.setLastName(rs.getString(7));
                bean.setDob(rs.getDate(8));
                bean.setMobileNo(rs.getString(9));
                bean.setEmail(rs.getString(10));
                bean.setCreatedBy(rs.getString(11));
                bean.setModifiedBy(rs.getString(12));
                bean.setCreatedDateTime(rs.getTimestamp(13));
                bean.setModifiedDateTime(rs.getTimestamp(14));
                list.add(bean);
            }
            rs.close();
        } catch (Exception e) {
            log.error("Database Exception..", e);
            throw new ApplicationException(
                    "Exception : Exception in search Student");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("Model search End");
        return list;
    }

    /**
     * Get List of Student
     *
     * @return list : List of Student
     * @throws DatabaseException
     */

    public List list() throws ApplicationException {
        return list(0, 0);
    }

    /**
     * Get List of Student with pagination
     *
     * @return list : List of Student
     * @param pageNo
     *            : Current Page No.
     * @param pageSize
     *            : Size of Page
     * @throws DatabaseException
     */

    public List list(int pageNo, int pageSize) throws ApplicationException {
        log.debug("Model list Started");
        ArrayList list = new ArrayList();
        StringBuffer sql = new StringBuffer("select * from St_STUDENT");
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
                StudentDTO bean = new StudentDTO();
                bean.setId(rs.getLong(1));
                bean.setCollegeId(rs.getLong(2));
                bean.setCollegeName(rs.getString(3));
                bean.setCourseId(rs.getLong(4));
                bean.setCourseName(rs.getString(5));
                bean.setFirstName(rs.getString(6));
                bean.setLastName(rs.getString(7));
                bean.setDob(rs.getDate(8));
                bean.setMobileNo(rs.getString(9));
                bean.setEmail(rs.getString(10));
                bean.setCreatedBy(rs.getString(11));
                bean.setModifiedBy(rs.getString(12));
                bean.setCreatedDateTime(rs.getTimestamp(13));
                bean.setModifiedDateTime(rs.getTimestamp(14));
                list.add(bean);
            }
            rs.close();
        } catch (Exception e) {
        	e.printStackTrace();
            log.error("Database Exception..", e);
            throw new ApplicationException(
                    "Exception : Exception in getting list of Student");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        log.debug("Model list End");
        return list;

    }

}
