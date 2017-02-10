package com.apabi.r2k.security.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.http.protocol.HTTP;

import com.apabi.r2k.common.utils.GlobalConstant;

public class HttpUrlRequest extends HttpServletRequestWrapper{
	
	public HttpUrlRequest(HttpServletRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
	}
	
//	public HttpUrlRequest(HttpServletRequest request, String rolePrefix) {
//		super(request, rolePrefix);
//		// TODO Auto-generated constructor stub
//	}

	@Override
	public String getHeader(String name) {
		if(name.equals(HTTP.USER_AGENT)){
			 return super.getHeader(name)+";"+GlobalConstant.WX_USER_AGENT;		
		   }else{
			 return super.getHeader(name);
		}
	}
  }
