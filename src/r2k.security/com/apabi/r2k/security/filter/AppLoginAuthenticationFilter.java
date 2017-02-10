package com.apabi.r2k.security.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.apabi.r2k.common.utils.Base64;
import com.apabi.r2k.common.utils.CookieUtil;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.HttpParamUtils;
import com.apabi.r2k.security.app.AppLogin;

public class AppLoginAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {

	private Logger log = LoggerFactory.getLogger(AppLoginAuthenticationFilter.class);
	
	private AppLogin appLogin;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		String userAgent = HttpParamUtils.getUserAgent(request);
		//如果为pad用户登录，进入pad用户登录流程，否则进入微信的登录流程
		String userName = obtainUsername(request);
		String password = obtainPassword(request);
		String orgId = obatainOrgId(request);
		boolean isLoginSuccess = false;
		try {
			if(userAgent.equals(GlobalConstant.USER_AGENT_IPAD)){
				//ipad用户登录
				isLoginSuccess = appLogin.iPadLogin(userName, password, orgId, userAgent);
			}else if(userAgent.equals(GlobalConstant.USER_AGENT_IPHONE)){
				//iphone用户登录
				isLoginSuccess = appLogin.iPhoneLogin(userName, password, orgId, userAgent);
			}else{
				//微信用户登录
				isLoginSuccess = appLogin.weixinLogin(userName, Base64.encodeBytes(password.getBytes()), orgId, userAgent);
			}
			if(isLoginSuccess){
				CookieUtil.saveCookie("orgid",orgId,(HttpServletResponse) response);
				CookieUtil.saveCookie("userid",userName,(HttpServletResponse) response);
				return SecurityContextHolder.getContext().getAuthentication();
			}
		} catch (Exception e) {
			log.error("attemptAuthentication:[userName#"+userName+",orgId#"+orgId+",userAgent#"+userAgent+"]:登录失败:"+e.getMessage());
			throw new BadCredentialsException(e.getMessage());
		}
		throw new BadCredentialsException("登录失败");
	}
	
	protected String obatainOrgId(HttpServletRequest request) {
		return request.getParameter("j_orgid");
	}

	public AppLogin getAppLogin() {
		return appLogin;
	}

	public void setAppLogin(AppLogin appLogin) {
		this.appLogin = appLogin;
	}
}
