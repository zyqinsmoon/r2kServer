package com.apabi.r2k.common.utils;

import javax.servlet.http.HttpServletRequest;

import com.apabi.r2k.security.model.AuthOrg;

public interface BaseSession {

	public String getUserId(HttpServletRequest req);
	
	public String getOrgId(HttpServletRequest req);
	
	public String getDeviceId(HttpServletRequest req);
	
	public String getUserAgent(HttpServletRequest req);
	
	public AuthOrg getAuthOrg(HttpServletRequest req);
	
	public void setCurrentUser(HttpServletRequest request);
}
