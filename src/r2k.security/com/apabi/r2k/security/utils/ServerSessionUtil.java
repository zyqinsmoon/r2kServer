package com.apabi.r2k.security.utils;

import javax.servlet.http.HttpServletRequest;

import com.apabi.r2k.common.utils.BaseSession;
import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.utils.SessionUtils;

public class ServerSessionUtil implements BaseSession {

	@Override
	public String getUserId(HttpServletRequest req) {
		return SessionUtils.getUserId(req);
	}

	@Override
	public String getOrgId(HttpServletRequest req) {
		return SessionUtils.getOrgId(req);
	}

	@Override
	public String getDeviceId(HttpServletRequest req) {
		return SessionUtils.getDeviceId(req);
	}

	@Override
	public String getUserAgent(HttpServletRequest req) {
		return SessionUtils.getUserAgent(req);
	}

	@Override
	public AuthOrg getAuthOrg(HttpServletRequest req) {
		return SessionUtils.getAuthOrg(req);
	}

	@Override
	public void setCurrentUser(HttpServletRequest request) {
		SessionUtils.setCurrentUser(request, SecurityUtil.getSecurityUser());
	}

}
