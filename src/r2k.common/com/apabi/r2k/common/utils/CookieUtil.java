package com.apabi.r2k.common.utils;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
	public static int maxAge = 24 * 3600 * 30;
	public static String path = "/r2k";
	
	/**
	 * 
	 * @param name
	 * @param value
	 * @param response
	 * @param maxAge
	 * @throws UnsupportedEncodingException
	 */
	public static void saveCookie(String name,String value,HttpServletResponse response, String path, int maxAge) throws UnsupportedEncodingException{
		Cookie cookie = new Cookie(name,value);
		cookie.setMaxAge(maxAge);
		cookie.setPath(path);
		response.addCookie(cookie);
	}
	   
	public static void saveCookie(String name,String value,HttpServletResponse response) throws UnsupportedEncodingException{
		saveCookie(name,value,response,path, maxAge);
	}
	
	public static String getCookie(String name,HttpServletRequest request) throws UnsupportedEncodingException{
		String value = null;
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for(int i=0;i<cookies.length;i++){
				Cookie cookie = cookies[i];
				if(cookie.getName().equals(name)){
					value = cookie.getValue();
				}
			}	
		}
		return value;
	}
	
	public static void removeCookie(String name,HttpServletResponse response){
		removeCookie(name, response, path);
	}

	public static void removeCookie(String name,HttpServletResponse response, String path){
		if(name == "" || name == null) return;
		Cookie cookie = new Cookie(name,"");
		cookie.setMaxAge(0);
		cookie.setPath(path);
		response.addCookie(cookie);
	}

	public static String getOrgId(HttpServletRequest request) throws UnsupportedEncodingException{
		return getCookie("orgid", request);
	}
	
	public static String getDeviceId(HttpServletRequest request) throws UnsupportedEncodingException{
		return getCookie("deviceid", request);
	}
}
