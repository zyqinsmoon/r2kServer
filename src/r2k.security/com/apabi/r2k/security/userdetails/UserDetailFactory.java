package com.apabi.r2k.security.userdetails;

import org.springframework.security.core.AuthenticationException;


public interface UserDetailFactory {

	public MyUserDetails createScreenUserDeatil(String orgId, String devId, String userAgent, String softVersion) throws AuthenticationException;
	
	public MyUserDetails createIPadUserDetails(String userName, String password, String orgId, String userAgent) throws AuthenticationException;
	
	public MyUserDetails createIPhoneUserDetails(String userName, String password, String orgId, String userAgent) throws AuthenticationException;
	
	public MyUserDetails createWeiXinUserDetails(String userName, String password, String orgId, String userAgent) throws AuthenticationException;
	
	public MyUserDetails createSlaveUserDetails(String orgId, String devId, String userAgent) throws AuthenticationException;
	
	public MyUserDetails createNormalUserDetails(String userName, String password, String orgId, String userAgent) throws AuthenticationException;
}
