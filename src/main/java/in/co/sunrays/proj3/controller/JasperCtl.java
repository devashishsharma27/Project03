package in.co.sunrays.proj3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.internal.SessionImpl;

import in.co.sunrays.proj3.util.HibDataSource;
import in.co.sunrays.proj3.util.JDBCDataSource;
import in.co.sunrays.project3.dto.UserDTO;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@WebServlet(name="JasperCtl",urlPatterns = "/ctl/JasperCtl")
public class JasperCtl extends BaseCtl{
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		

		try {

			/*Compilation of jrxml file*/
			JasperReport jasperReport = JasperCompileManager
					.compileReport("G:\\SpringToolSuite\\ORSProject_3\\src\\main\\webapp\\Blank_A4.jrxml");
			
			HttpSession session = req.getSession(true);
			UserDTO dto = (UserDTO) session.getAttribute("user");
			dto.getFirstName();
			dto.getLastName();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user", dto.getFirstName() + " " + dto.getLastName());
			java.sql.Connection conn = null;
			

			ResourceBundle rb = ResourceBundle.getBundle("in.co.sunrays.proj3.bundle.system");
			
			String Database = rb.getString("DATABASE");
			if ("Hibernate".equalsIgnoreCase(Database)) {
				conn = ((SessionImpl) HibDataSource.getSession()).connection();
			}

			if ("JDBC".equalsIgnoreCase(Database)) {
				conn = JDBCDataSource.getConnection();
			}

		/*	Filling data into the report*/
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, conn);
		
			/*Export Jasper report*/
			byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
			
			resp.setContentType("application/pdf");
			resp.getOutputStream().write(pdf);
			resp.getOutputStream().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return null;
	}

}
