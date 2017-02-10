package com.apabi.r2k.common.security.springsecurity;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * 扩展{@link WebAuthenticationDetails}，允许获取request对象和客户端的真实IP（而不是代理IP）.
 * 
 * @author minwh
 *
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

	private static final long serialVersionUID = -8036417745101144253L;
	
	private final HttpServletRequest request;
	
	public CustomWebAuthenticationDetails(final HttpServletRequest request) {
		super(request);
		this.request = request;
	}

	public HttpServletRequest getRequest() {
		return request;
	}
	
	/**
	 * 获取request对象和客户端的真实IP（而不是代理IP）.
	 *//*
	public String getRealRemoteIp() {
		try {
			return IpUtils.getIpAddr(request);
		} catch (final Exception e) {
			return null;
		}
	}*/

	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
