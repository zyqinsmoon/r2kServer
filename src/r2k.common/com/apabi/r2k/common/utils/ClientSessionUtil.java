package com.apabi.r2k.common.utils;

import javax.servlet.http.HttpServletRequest;

import com.apabi.r2k.security.model.AuthOrg;


public class ClientSessionUtil implements BaseSession {

	private String orgId = PropertiesUtil.get("client.orgid");
	
	@Override
	public String getUserId(HttpServletRequest req) {
		return null;
	}

	@Override
	public String getOrgId(HttpServletRequest req) {
		return orgId;
	}

	@Override
	public String getDeviceId(HttpServletRequest req) {
		return orgId;
	}

	@Override
	public String getUserAgent(HttpServletRequest req) {
		String userAgent = req.getHeader("User-Agent");
		if(userAgent.contains(GlobalConstant.USER_AGENT_ANDROID_LARGE)){
			return GlobalConstant.USER_AGENT_ANDROID_LARGE;
		}else if(userAgent.contains(GlobalConstant.USER_AGENT_IPAD)){
			return GlobalConstant.USER_AGENT_IPAD;
		}
		return userAgent;
	}

	@Override
	public AuthOrg getAuthOrg(HttpServletRequest req) {
		return null;
	}

	@Override
	public void setCurrentUser(HttpServletRequest request) {
	}

}
