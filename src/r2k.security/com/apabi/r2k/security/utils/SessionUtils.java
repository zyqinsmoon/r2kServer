package com.apabi.r2k.security.utils;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.model.AuthUser;

public class SessionUtils{

	public static final String SESSION_ORG = "orgId";
	public static final String SESSION_USER = "user";
	public static final String SESSION_DETAILS = "Principal";
	public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
	public static final String SESSION_CURRENT_USER = "CurrentUser";
	
	public static void setCurrentUser(HttpServletRequest request, AuthUser user){
		request.getSession(true).setAttribute(SESSION_CURRENT_USER, user);
	}
	
	public static AuthUser getCurrentUser(HttpServletRequest request){
		return (AuthUser) request.getSession().getAttribute(SESSION_CURRENT_USER);
	}
	
	public static AuthOrg getAuthOrg(HttpServletRequest request){
		AuthUser authUser =(AuthUser) request.getSession().getAttribute(SESSION_CURRENT_USER);
		if(authUser != null){
			return authUser.getAuthOrg();
		}else{
			return null;
		}
	}
	
	public static AuthOrg getCurrentOrg(HttpServletRequest req){
		AuthUser authUser = (AuthUser) req.getSession().getAttribute(SESSION_CURRENT_USER);
		AuthOrg currOrg = null;
		if(authUser != null){
			currOrg = authUser.getCurrentOrg();
			if(currOrg == null){
				currOrg = authUser.getAuthOrg();
			}
		}
		return currOrg;
	}
	
	public static String getAuthOrgId(HttpServletRequest req){
		AuthUser authUser = (AuthUser) req.getSession().getAttribute(SESSION_CURRENT_USER);
		AuthOrg currentOrg = authUser.getCurrentOrg();
		if(currentOrg == null){
			currentOrg = authUser.getAuthOrg();
			authUser.setAuthOrg(currentOrg);
		}
		return currentOrg.getOrgId();
	}
	
	public static String getUserAgent(HttpServletRequest req){
		HttpSession session = req.getSession();
		AuthUser user = (AuthUser) session.getAttribute(SESSION_CURRENT_USER);
		return user.getUserAgent();
	}
	
	public static String getSoftVersion(HttpServletRequest request){
		String userAgent = request.getHeader("User-Agent");
		String version = userAgent.substring(userAgent.lastIndexOf("#")+1);
		return version;
	}
	
	public static String getUserId(HttpServletRequest req){
		AuthUser user = (AuthUser) req.getSession().getAttribute(SESSION_CURRENT_USER);
		return user.getUserName();
	}
	
	public static String getOrgId(HttpServletRequest req){
		AuthUser user = (AuthUser) req.getSession().getAttribute(SESSION_CURRENT_USER);
		return user.getCurrentOrg().getOrgId();
	}
	
	public static String getDeviceId(HttpServletRequest req){
		AuthUser user = (AuthUser) req.getSession().getAttribute(SESSION_CURRENT_USER);
		return user.getDevId();
	}
	
	public static String getToken(HttpServletRequest req){
		String token = null;
		AuthUser authUser = (AuthUser) req.getSession().getAttribute(SESSION_CURRENT_USER);
		if(authUser != null){
			token = authUser.getToken();
		}
		return token;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
	}
}
