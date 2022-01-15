package in.co.sunrays.proj3.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import in.co.sunrays.proj3.exception.ApplicationException;

/**
 * JDBC DataSource is a Data Connection Pool
 *
 * @author SUNRAYS Technologies
 * @version 1.0
 * @Copyright (c) SUNRAYS Technologies
 *
 */
public final class JDBCDataSource {
	/**
	 * JDBC Database connection pool ( DCP )
	 */
	public static JDBCDataSource dataSource = null;
	private ComboPooledDataSource cpds;

	private JDBCDataSource() {
	}

	/**
	 * Create instance of Connection Pool
	 *
	 * @return
	 */
	public static JDBCDataSource getInstance() {
		if (dataSource == null) {
			ResourceBundle rb = ResourceBundle.getBundle("in.co.sunrays.proj3.bundle.system");
			dataSource = new JDBCDataSource();
			dataSource.cpds = new ComboPooledDataSource();
			try {
				dataSource.cpds.setDriverClass(rb.getString("driver"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			dataSource.cpds.setJdbcUrl(rb.getString("url"));
			dataSource.cpds.setUser(rb.getString("username"));
			dataSource.cpds.setPassword(rb.getString("password"));
			dataSource.cpds.setInitialPoolSize(new Integer((String) rb.getString("initialPoolSize")));
			dataSource.cpds.setAcquireIncrement(new Integer((String) rb.getString("acquireIncrement")));
			dataSource.cpds.setMaxPoolSize(new Integer((String) rb.getString("maxPoolSize")));
			dataSource.cpds.setMaxIdleTime(new Integer((String) rb.getString("timeout")));
			dataSource.cpds.setMinPoolSize(new Integer((String) rb.getString("minPoolSize")));

		}
		return dataSource;
	}

	/**
	 * Gets the connection from ComboPooledDataSource
	 *
	 * @return connection
	 */
	public static Connection getConnection() throws Exception {
		return getInstance().cpds.getConnection();

	}

	public static void closeConnection(Connection con, Statement stmt) {
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
			}
		}
	}

	public static void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
			}
		}
	}

	public static void trnRollback(Connection con) throws ApplicationException {
		if (con != null) {
			try {
				con.rollback();
			} catch (Exception e) {
				throw new ApplicationException(e.toString());
			}
		}
	}

}
