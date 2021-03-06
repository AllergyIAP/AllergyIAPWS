package com.allergy.servlet.filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RequestLoggingFilter implements Filter {

	private ServletContext context;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String uri = req.getRequestURI();
		if (uri.endsWith("XarxaImportServlet")) {
			chain.doFilter(request, response);
			return;
		}

		Enumeration<String> params = req.getParameterNames();
		while (params.hasMoreElements()) {
			String name = params.nextElement();
			String value = request.getParameter(name);
			this.context.log(req.getRemoteAddr() + "::Request Params::{" + name + "=" + value + "}");
		}

		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				this.context
						.log(req.getRemoteAddr() + "::Cookie::{" + cookie.getName() + "," + cookie.getValue() + "}");
			}
		}
		this.context.log("Requested Resource::" + uri);

		HttpSession session = req.getSession(false);
		boolean customer = false;
		if (session != null && session.getAttribute("User") != null) {
			customer = true;
		}

		this.context.log(String.valueOf(customer));
		if ((session == null && !(uri.endsWith("jsp") || uri.endsWith("LoginServlet")))
				|| (!customer && !uri.endsWith("LoginServlet"))) {
			this.context.log("Unauthorized access request");
			((HttpServletResponse) response).sendRedirect("LoginServlet");
		} else {
			// pass the request along the filter chain
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filter) throws ServletException {
		this.context = filter.getServletContext();
		this.context.log("RequestLoggingFilter initialized");
	}

}
