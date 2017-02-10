package com.apabi.r2k.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpHeaders;

import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.security.utils.HttpUrlRequest;

public class UrlFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		//当权限为任意用户时，尚未有有效的用户登录
		HttpServletRequest req = (HttpServletRequest) arg0;
	    String servletUrl = req.getServletPath();
		String userAgentTemp = req.getHeader(HttpHeaders.USER_AGENT);
		HttpServletRequest myrequest = null;
	   if(servletUrl.indexOf("/wx/")>-1&&(!userAgentTemp.toLowerCase().contains(GlobalConstant.WX_USER_AGENT.toLowerCase()))){
		    myrequest = new HttpUrlRequest(req);
	   }else{
		   myrequest = req;
	   }
	   arg2.doFilter(myrequest, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
