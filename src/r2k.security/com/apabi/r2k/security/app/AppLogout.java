package com.apabi.r2k.security.app;

import java.io.InputStream;

import com.apabi.r2k.common.utils.ShuYuanResult;
import com.apabi.r2k.common.utils.ShuyuanUtil;

public class AppLogout {

	/**
	 * ipad注销登录
	 */
	public String ipadLogout(String orgid, String token) throws Exception{
		return iosLogout(orgid, token);
	}
	
	/**
	 * iphone注销登录
	 */
	public String iphoneLogout(String orgid, String token) throws Exception{
		return iosLogout(orgid, token);
	}
	
	/**
	 * ios系统通用注销登录方法
	 */
	public String iosLogout(String orgid, String token) throws Exception{
		InputStream in = ShuyuanUtil.shuYuanLogout(orgid, token);
		ShuYuanResult syResult = ShuyuanUtil.getShuYuanResult(in);
		if(syResult == null){
			return ShuYuanResult.LOGOUT_CODE_FAIL;
		}
		return syResult.getCode();
	}
}
