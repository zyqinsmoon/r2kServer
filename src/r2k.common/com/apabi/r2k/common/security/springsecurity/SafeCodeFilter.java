/**
 *
 */
package com.apabi.r2k.common.security.springsecurity;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * 判断用户输入的验证码是否正确.
 *
 * @author minwh
 */
public class SafeCodeFilter implements Filter {

	private static final long serialVersionUID = -5838154525730151323L;
	
	private String failureUrl;

	public void init(final FilterConfig config) throws ServletException {
		failureUrl = config.getInitParameter("failureUrl");
		if (StringUtils.isBlank(failureUrl)) {
			failureUrl = "/";
		}
	}

	public void destroy() {
	}

	public void doFilter(final ServletRequest servletRequest, 
			final ServletResponse servletResponse, 
			final FilterChain filterChain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		final String code = request.getParameter("j_code");
		final String safeCodeInSession = (String) request.getSession().getAttribute("_validate_code");
		if (StringUtils.isBlank(code) ||StringUtils.isBlank(safeCodeInSession)|| !code.toLowerCase().equals(safeCodeInSession.toLowerCase())) {
			HttpServletRequest req=(HttpServletRequest) servletRequest;
			req.getSession().setAttribute("errorMsg", "验证码错误");
			response.sendRedirect(failureUrl);
			
			return;
		}

		filterChain.doFilter(request, response);
	}
}
