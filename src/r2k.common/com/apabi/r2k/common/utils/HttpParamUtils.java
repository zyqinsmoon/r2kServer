package com.apabi.r2k.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;

public class HttpParamUtils {
	public static String getUserAgent(HttpServletRequest req){
		String userAgent = req.getHeader(HttpHeaders.USER_AGENT);
		String[] userAgentArr = userAgent.split("#");
		String userAgentStr = userAgentArr[0];
		String myUserAgent ="";
		if(userAgentStr.equals(GlobalConstant.USER_AGENT_ANDROID_LARGE)){
			myUserAgent= GlobalConstant.USER_AGENT_ANDROID_LARGE;
		}else if(userAgentStr.equals(GlobalConstant.USER_AGENT_ANDROID_PORTRAIT)){
			myUserAgent= GlobalConstant.USER_AGENT_ANDROID_PORTRAIT;
		}else if(userAgentStr.equals(GlobalConstant.USER_AGENT_IPAD)){
			myUserAgent= GlobalConstant.USER_AGENT_IPAD;
		}else if(userAgentStr.equals(GlobalConstant.USER_AGENT_IPHONE)){
			myUserAgent= GlobalConstant.USER_AGENT_IPHONE;
		}else if(userAgentStr.equals(GlobalConstant.USER_AGENT_ANDROID_PHONE)){
			myUserAgent= GlobalConstant.USER_AGENT_ANDROID_PHONE;
		}else if(userAgentStr.equals(GlobalConstant.USER_AGENT_SLAVE)){
			myUserAgent= GlobalConstant.USER_AGENT_SLAVE;
		}else if(userAgentStr.toLowerCase().contains("MicroMessenger".toLowerCase())){
			myUserAgent = GlobalConstant.USER_AGENT_WEIXIN;
		}else{
			myUserAgent= GlobalConstant.USER_AGENT_ADMIN;
		}
		return myUserAgent;
	}
	
	
	public static String getSoftVersion(HttpServletRequest request){
		String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
		String version = userAgent.substring(userAgent.lastIndexOf("#")+1);
		return version;
	}
	
	public static String getUserid(HttpServletRequest request) throws Exception{
		String userId = "";
			userId = request.getParameter(GlobalConstant.SECURITY_PARAM_USERID);
			if(StringUtils.isBlank(userId)){
				userId = CookieUtil.getCookie(GlobalConstant.SECURITY_PARAM_USERID, request);
			}
		return userId;
	}
	
	public static String getOrgid(HttpServletRequest request) throws Exception{
		String orgid = "";
			orgid = request.getParameter(GlobalConstant.SECURITY_PARAM_ORGID);
			if(StringUtils.isBlank(orgid)){
				orgid = CookieUtil.getCookie(GlobalConstant.SECURITY_PARAM_ORGID, request);
			}
		return orgid;
	}
	
	public static String getDeviceId(HttpServletRequest request) throws Exception{
		String devId = "";
			devId = request.getParameter(GlobalConstant.SECURITY_PARAM_DEVICEID);
			if(StringUtils.isBlank(devId)){
				devId = CookieUtil.getCookie(GlobalConstant.SECURITY_PARAM_DEVICEID, request);
			}
			
		return devId;
	}
	
	/**
	 * 获取分辨率
	 */
	public static Resolution getResolution(HttpServletRequest request) throws Exception{
		Resolution resolution = new Resolution();
		String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
		int index = userAgent.indexOf("#") + 1;
		String tmpUserAgent = userAgent.substring(index);
		int endInx = tmpUserAgent.indexOf("#");
		String resolutionStr = endInx != -1 ? tmpUserAgent.substring(0, endInx) : tmpUserAgent;
		String[] resolutions = resolutionStr.split("*");
		resolution.setWidth(Integer.parseInt(resolutions[0]));
		resolution.setHeight(Integer.parseInt(resolutions[1]));
		return resolution;
	}
	
	public static String getName(HttpServletRequest req){
		String name = null;
		try {
			name = req.getParameter("name");
			if(StringUtils.isBlank(name)){
				name = CookieUtil.getCookie("name", req);
			}
			if(StringUtils.isNotBlank(name)){
				name = URLDecoder.decode(name, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return name;
	}
	
	public static String getUserId(HttpServletRequest req){
		String userId = null;
		try {
			userId = req.getParameter("userid");
			if(StringUtils.isBlank(userId)){
				userId = CookieUtil.getCookie("userid", req);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return userId;
	}
	
	public static String getPassword(HttpServletRequest req){
		String userId = null;
		try {
			userId = req.getParameter("password");
			if(StringUtils.isBlank(userId)){
				userId = CookieUtil.getCookie("password", req);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return userId;
	}
}
