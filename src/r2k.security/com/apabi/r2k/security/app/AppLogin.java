package com.apabi.r2k.security.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

import com.apabi.r2k.common.utils.ShuYuanResult;
import com.apabi.r2k.common.utils.ShuyuanUtil;
import com.apabi.r2k.security.model.AuthUser;
import com.apabi.r2k.security.userdetails.MyUserDetails;
import com.apabi.r2k.security.userdetails.UserDetailFactory;
import com.apabi.r2k.security.utils.AuthenticationTokenUtil;

public class AppLogin {

	private Logger log = LoggerFactory.getLogger(AppLogin.class);
	
	private UserDetailFactory userDetailFactory;
	
	/**
	 * 触摸屏登录
	 */
	public boolean androidLargeLogin(String orgId, String devId, String userAgent, String softVersion) throws Exception{
		log.info("androidLargeLogin:[orgId#"+orgId+",devId#"+devId+",userAgent#"+userAgent+"]:创建触摸屏用户userdetails");
		MyUserDetails userDetails = userDetailFactory.createScreenUserDeatil(orgId, devId, userAgent, softVersion);		//创建触摸屏UserDetails
		if(userDetails == null){
			return false;
		}
		return AuthenticationTokenUtil.createAndAddToContext(userDetails);
	}
	
	/**
	 * ipad登录
	 */
	public boolean iPadLogin(String userName, String password, String orgId, String userAgent) throws Exception{
//		String token = ShuyuanUtil.getShuYuanToken(userName, password, orgId);
		ShuYuanResult syResult = ShuyuanUtil.getResultAfterLogin(userName, password, orgId);
		if(syResult == null || !syResult.getCode().equals(ShuYuanResult.LOGIN_CODE_SUCCESS)){
			throw new BadCredentialsException(syResult.getCode());
		}
		orgId = orgId.toLowerCase();
		log.info("iPadLogin:[userName#"+userName+",password#"+password+",orgId#"+orgId+",token#"+syResult.getToken()+"]:构造ipad用户userdetails");
		MyUserDetails userDetails = userDetailFactory.createIPadUserDetails(userName, password, orgId, userAgent);
		if(userDetails == null){
			return false;
		}
		AuthUser user = userDetails.getAuthUser();
		user.setUserName(syResult.getUid());
		user.setToken(syResult.getToken());
		user.getCurrentOrg().setOrgId(syResult.getOrgidentifier());
		user.getCurrentOrg().setOrgName(syResult.getOrgname());
		return AuthenticationTokenUtil.createAndAddToContext(userDetails);
	}
	
	/**
	 * iphone登录
	 */
	public boolean iPhoneLogin(String userName, String password, String orgId, String userAgent) throws Exception{
//		String token = ShuyuanUtil.getShuYuanToken(userName, password, orgId);
		ShuYuanResult syResult = ShuyuanUtil.getResultAfterLogin(userName, password, orgId);
		if(syResult == null || !syResult.getCode().equals(ShuYuanResult.LOGIN_CODE_SUCCESS)){
			throw new BadCredentialsException(syResult.getCode());
		}
		orgId = orgId.toLowerCase();
		log.info("iPhoneLogin:[userName#"+userName+",password#"+password+",orgId#"+orgId+",token#"+syResult.getToken()+"]:构造iphone用户userdetails");
		MyUserDetails userDetails = userDetailFactory.createIPhoneUserDetails(userName, password, orgId, userAgent);
		if(userDetails == null){
			return false;
		}
		AuthUser user = userDetails.getAuthUser();
		user.setUserName(syResult.getUid());
		user.setToken(syResult.getToken());
		user.getAuthOrg().setOrgId(syResult.getOrgidentifier());
		user.getAuthOrg().setOrgName(syResult.getOrgname());
		return AuthenticationTokenUtil.createAndAddToContext(userDetails);
	}
	
	/**
	 * 微信登录
	 */
	public boolean weixinLogin(String userName, String password, String orgId, String userAgent) throws Exception{
		//String token = ShuyuanUtil.getShuYuanToken(userName, password, orgId);
		orgId = orgId.toLowerCase();
		log.info("weixinLogin:[orgId#"+orgId+"]:创建微信用户userdetails");
		MyUserDetails userDetails = userDetailFactory.createWeiXinUserDetails(userName, password, orgId, userAgent);
		if(userDetails == null){
			return false;
		}
		//userDetails.getAuthUser().setToken(token);
		return AuthenticationTokenUtil.createAndAddToContext(userDetails);
	}
	
	/**
	 * 缓存服务器登录
	 */
	public boolean slaveLogin(String orgId, String devId, String userAgent) throws Exception{
		log.info("slaveLogin:[orgId#"+orgId+"]:创建缓存服务器userdetails");
		MyUserDetails userDetails = userDetailFactory.createSlaveUserDetails(orgId, devId, userAgent);
		if(userDetails == null){
			return false;
		}
		return AuthenticationTokenUtil.createAndAddToContext(userDetails);
	}

	public UserDetailFactory getUserDetailFactory() {
		return userDetailFactory;
	}

	public void setUserDetailFactory(UserDetailFactory userDetailFactory) {
		this.userDetailFactory = userDetailFactory;
	}
}
