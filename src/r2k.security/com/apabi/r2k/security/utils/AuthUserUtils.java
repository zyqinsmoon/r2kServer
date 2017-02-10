package com.apabi.r2k.security.utils;

import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.model.AuthUser;

public class AuthUserUtils {

	/**
	 * 根据用户名、密码、机构id、userAgent创建用户信息
	 */
	public static AuthUser createAuthUser(String name,AuthOrg authOrg,String userAgent){
		AuthUser authUser = new AuthUser();
		authUser.setLoginName(name);
		authUser.setPassword(authOrg.getOrgId());
		authUser.setUserName(name);
		authUser.setAuthOrgId(authOrg.getOrgId());
		authUser.setUserAgent(userAgent);
		authUser.setAuthOrg(authOrg);
		authUser.setCurrentOrg(authOrg);
		return authUser;
	}
	

	
	/**
	 * 根据机构id、设备id、userAgent创建设备用户()
	 */
	public static AuthUser createDeviceUser(AuthOrg authOrg, String devId, String userAgent){
		AuthUser user = createAuthUser(authOrg.getOrgId(), authOrg, userAgent);
		user.setDevId(devId);
		return user;
	}
	
	/**
	 * 
	 * @param orgId
	 * @param devId
	 * @param userAgent
	 * @param softVersion
	 * @return
	 */
	public static AuthUser createScreenUser(AuthOrg authOrg, String devId, String userAgent, String softVersion){
		AuthUser user = createDeviceUser(authOrg, devId, userAgent);
		user.setSoftVersion(softVersion);
		return user;
	}
}
