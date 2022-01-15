package in.co.sunrays.proj3.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.co.sunrays.proj3.util.ServletUtility;
import in.co.sunrays.project3.dto.UserDTO;

/**
 * Main Controller performs session checking and logging operations before
 * calling any application controller. It prevents any user to access
 * application without login.
 *
 *
 * @author Ravi Rathore
 * @version 1.0
 * @Copyright (c) RaviOS
 */
@WebFilter(filterName = "FrontControllerCtl", urlPatterns = { "/ctl/*", "/doc/*" })
public class FrontControllerCtl implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("Fiter started");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		UserDTO dto = (UserDTO) session.getAttribute("user");
		if (dto == null) {
			String s = req.getRequestURI();
			request.setAttribute("uri", s);
			request.setAttribute("message", "Your session has been expired. Please re-Login.");
			ServletUtility.forward(ORSView.LOGIN_VIEW, req, resp);
		} else {
			chain.doFilter(request, response);
		}

	}

	public void destroy() {
		System.out.println("Fiter Destroyed");
	}

}
