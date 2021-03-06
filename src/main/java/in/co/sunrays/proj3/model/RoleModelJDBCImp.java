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
import in.co.sunrays.project3.dto.RoleDTO;

public class RoleModelJDBCImp implements RoleModelInt {

	 private static Logger log = Logger.getLogger(RoleModelJDBCImp.class);

	    /**
	     * Find next PK of Role
	     *
	     * @throws DatabaseException
	     */
	    public static Integer nextPK() throws DataBaseException {
	        log.debug("Model nextPK Started");
	        Connection conn = null;
	        int pk = 0;
	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn
	                    .prepareStatement("SELECT MAX(ID) FROM ST_ROLE");
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
	     * Add a Role
	     *
	     * @param bean
	     * @throws DatabaseException
	     *
	     */
	    public long add(RoleDTO bean) throws ApplicationException,
	            DuplicateRecordException {
	        log.debug("Model add Started");
	        Connection conn = null;
	        int pk = 0;

	        RoleDTO duplicataRole = findByName(bean);
	        // Check if create Role already exist
	        if (duplicataRole != null) {
	            throw new DuplicateRecordException("Role already exists");
	        }

	        try {
	            conn = JDBCDataSource.getConnection();
	            pk = nextPK();
	            // Get auto-generated next primary key
	            System.out.println(pk + " in ModelJDBC");
	            conn.setAutoCommit(false); // Begin transaction
	            PreparedStatement pstmt = conn
	                    .prepareStatement("INSERT INTO ST_ROLE VALUES(?,?,?,?,?,?,?)");
	            pstmt.setInt(1, pk);
	            pstmt.setString(2, bean.getName());
	            pstmt.setString(3, bean.getDescription());
	            pstmt.setString(4, bean.getCreatedBy());
	            pstmt.setString(5, bean.getModifiedBy());
	            pstmt.setTimestamp(6, bean.getCreatedDateTime());
	            pstmt.setTimestamp(7, bean.getModifiedDateTime());
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
	            throw new ApplicationException("Exception : Exception in add Role");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	        log.debug("Model add End");
	        return pk;
	    }

	    /**
	     * Delete a Role
	     *
	     * @param bean
	     * @throws DatabaseException
	     */
	    public void delete(RoleDTO bean) throws ApplicationException {
	        log.debug("Model delete Started");
	        Connection conn = null;
	        try {
	            conn = JDBCDataSource.getConnection();
	            conn.setAutoCommit(false); // Begin transaction
	            PreparedStatement pstmt = conn
	                    .prepareStatement("DELETE FROM ST_ROLE WHERE ID=?");
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
	                    "Exception : Exception in delete Role");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	        log.debug("Model delete Started");
	    }

	    /**
	     * Find User by Role
	     *
	     * @param name
	     *            : get parameter
	     * @return bean
	     * @throws DatabaseException
	     */

	    public RoleDTO findByName(RoleDTO dto) throws ApplicationException {
	        log.debug("Model findBy EmailId Started");
	        StringBuffer sql = new StringBuffer(
	                "SELECT * FROM ST_ROLE WHERE NAME=?");
	        RoleDTO bean = null;
	        Connection conn = null;
	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setString(1, dto.getName());
	            ResultSet rs = pstmt.executeQuery();
	            while (rs.next()) {
	                bean = new RoleDTO();
	                bean.setId(rs.getLong(1));
	                bean.setName(rs.getString(2));
	                bean.setDescription(rs.getString(3));
	                bean.setCreatedBy(rs.getString(4));
	                bean.setModifiedBy(rs.getString(5));
	                bean.setCreatedDateTime(rs.getTimestamp(6));
	                bean.setModifiedDateTime(rs.getTimestamp(7));

	            }
	            rs.close();
	        } catch (Exception e) {
	            log.error("Database Exception..", e);
	            throw new ApplicationException(
	                    "Exception : Exception in getting User by emailId");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	        log.debug("Model findBy EmailId End");
	        return bean;
	    }

	    /**
	     * Find Role by PK
	     *
	     * @param pk
	     *            : get parameter
	     * @return bean
	     * @throws DatabaseException
	     */

	    public RoleDTO findByPK(long pk) throws ApplicationException {
	        log.debug("Model findByPK Started");
	        StringBuffer sql = new StringBuffer("SELECT * FROM ST_ROLE WHERE ID=?");
	        RoleDTO bean = null;
	        Connection conn = null;
	        try {
	            conn = JDBCDataSource.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setLong(1, pk);
	            ResultSet rs = pstmt.executeQuery();
	            while (rs.next()) {
	                bean = new RoleDTO();
	                bean.setId(rs.getLong(1));
	                bean.setName(rs.getString(2));
	                bean.setDescription(rs.getString(3));
	                bean.setCreatedBy(rs.getString(4));
	                bean.setModifiedBy(rs.getString(5));
	                bean.setCreatedDateTime(rs.getTimestamp(6));
	                bean.setModifiedDateTime(rs.getTimestamp(7));
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
	     * Update a Role
	     *
	     * @param bean
	     * @throws DatabaseException
	     */

	    public void update(RoleDTO bean) throws ApplicationException,
	            DuplicateRecordException {
	        log.debug("Model update Started");
	        Connection conn = null;

	        RoleDTO duplicataRole = findByName(bean);
	        // Check if updated Role already exist
	        if (duplicataRole != null && duplicataRole.getId() != bean.getId()) {
	            throw new DuplicateRecordException("Role already exists");
	        }
	        try {
	            conn = JDBCDataSource.getConnection();

	            conn.setAutoCommit(false); // Begin transaction
	            PreparedStatement pstmt = conn
	                    .prepareStatement("UPDATE ST_ROLE SET NAME=?,DESCRIPTION=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");
	            pstmt.setString(1, bean.getName());
	            pstmt.setString(2, bean.getDescription());
	            pstmt.setString(3, bean.getCreatedBy());
	            pstmt.setString(4, bean.getModifiedBy());
	            pstmt.setTimestamp(5, bean.getCreatedDateTime());
	            pstmt.setTimestamp(6, bean.getModifiedDateTime());
	            pstmt.setLong(7, bean.getId());
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
	            throw new ApplicationException("Exception in updating Role ");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }
	        log.debug("Model update End");
	    }

	    /**
	     * Search Role
	     *
	     * @param bean
	     *            : Search Parameters
	     * @throws DatabaseException
	     */

	    public List search(RoleDTO bean) throws ApplicationException {
	        return search(bean, 0, 0);
	    }

	    /**
	     * Search Role with pagination
	     *
	     * @return list : List of Roles
	     * @param bean
	     *            : Search Parameters
	     * @param pageNo
	     *            : Current Page No.
	     * @param pageSize
	     *            : Size of Page
	     *
	     * @throws DatabaseException
	     */

	    public List search(RoleDTO bean, int pageNo, int pageSize)
	            throws ApplicationException {
	        log.debug("Model search Started");
	        StringBuffer sql = new StringBuffer("SELECT * FROM ST_ROLE WHERE 1=1");
	         
	        if (bean != null) {
	            if (bean.getId() > 0) {
	                sql.append(" AND id = " + bean.getId());
	            }
	            if (bean.getName() != null && bean.getName().length() > 0) {
	                sql.append(" AND NAME like '" + bean.getName() + "%'");
	            }
	            if (bean.getDescription() != null
	                    && bean.getDescription().length() > 0) {
	                sql.append(" AND DESCRIPTION like '" + bean.getDescription()
	                        + "%'");
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
	                bean = new RoleDTO();
	                bean.setId(rs.getLong(1));
	                bean.setName(rs.getString(2));
	                bean.setDescription(rs.getString(3));
	                bean.setCreatedBy(rs.getString(4));
	                bean.setModifiedBy(rs.getString(5));
	                bean.setCreatedDateTime(rs.getTimestamp(6));
	                bean.setModifiedDateTime(rs.getTimestamp(7));
	                list.add(bean);
	            }
	            rs.close();
	        } catch (Exception e) {
	            log.error("Database Exception..", e);
	            throw new ApplicationException(
	                    "Exception : Exception in search Role");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        log.debug("Model search End");
	        return list;
	    }

	    /**
	     * Get List of Role
	     *
	     * @return list : List of Role
	     * @throws DatabaseException
	     */

	    public List list() throws ApplicationException {
	        return list(0, 0);
	    }

	    /**
	     * Get List of Role with pagination
	     *
	     * @return list : List of Role
	     * @param pageNo
	     *            : Current Page No.
	     * @param pageSize
	     *            : Size of Page
	     * @throws DatabaseException
	     */

	    public List list(int pageNo, int pageSize) throws ApplicationException {
	        log.debug("Model list Started");
	        ArrayList list = new ArrayList();
	        StringBuffer sql = new StringBuffer("select * from ST_ROLE");
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
	                RoleDTO bean = new RoleDTO();
	                bean.setId(rs.getLong(1));
	                bean.setName(rs.getString(2));
	                bean.setDescription(rs.getString(3));
	                bean.setCreatedBy(rs.getString(4));
	                bean.setModifiedBy(rs.getString(5));
	                bean.setCreatedDateTime(rs.getTimestamp(6));
	                bean.setModifiedDateTime(rs.getTimestamp(7));
	                list.add(bean);
	            }
	            rs.close();
	        } catch (Exception e) {
	            log.error("Database Exception..", e);
	            throw new ApplicationException(
	                    "Exception : Exception in getting list of Role");
	        } finally {
	            JDBCDataSource.closeConnection(conn);
	        }

	        log.debug("Model list End");
	        return list;

	    }

		

}
