package com.apabi.r2k.security.interceptor;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.apabi.r2k.admin.service.DeviceService;
import com.apabi.r2k.common.utils.CookieUtil;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.HttpParamUtils;
import com.apabi.r2k.security.app.AppLogin;
import com.apabi.r2k.security.utils.AuthUtil;
import com.apabi.r2k.security.utils.AuthenticationTokenUtil;
import com.apabi.r2k.security.utils.SecurityUtil;
import com.apabi.r2k.security.utils.SessionUtils;

public class MyFilterSecurityInterceptor extends FilterSecurityInterceptor {
	
	private Logger log = LoggerFactory.getLogger(MyFilterSecurityInterceptor.class);
	
	private FilterInvocationSecurityMetadataSource securityMetadataSource;
	@Resource
	private AppLogin appLogin;
	@Resource
	private DeviceService deviceService;

	@Override
	public Class<? extends Object> getSecureObjectClass() {
		return FilterInvocation.class;
	}

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		FilterInvocation fi = new FilterInvocation(request, response, chain);
		if (AuthUtil.getAuthentication() == null || AuthUtil.isAnonymous()) {		//当权限为任意用户时，尚未有有效的用户登录
			HttpServletRequest req = (HttpServletRequest) request;
			String userAgent = HttpParamUtils.getUserAgent(req);
			String orgId = "";
			try {
				orgId = HttpParamUtils.getOrgid(req);
				String userAgentTemp = req.getHeader(HttpHeaders.USER_AGENT);
				if(StringUtils.isBlank(orgId)){
					AuthenticationTokenUtil.clearAuthentication();
				}else{
					orgId = orgId.toLowerCase();
					if(userAgent.equals(GlobalConstant.USER_AGENT_ANDROID_LARGE)||userAgent.equals(GlobalConstant.USER_AGENT_ANDROID_PORTRAIT)){
						String devId = HttpParamUtils.getDeviceId(req);
						if (!StringUtils.isBlank(devId)) {
							if (deviceService.getDeviceByDeviceId(devId, orgId) != null) {
								//触摸屏用户自动登录
								String softVersion = HttpParamUtils.getSoftVersion(req);
								appLogin.androidLargeLogin(orgId, devId, userAgent, softVersion);
								SessionUtils.setCurrentUser(req, SecurityUtil.getSecurityUser());
								//将jseesionid放入cookie
//								CookieUtil.saveCookie("JSESSIONID",req.getSession().getId(),(HttpServletResponse) response);
								CookieUtil.saveCookie("orgid",orgId,(HttpServletResponse) response);
								CookieUtil.saveCookie("deviceid",devId,(HttpServletResponse) response);
							}
						}else{
							AuthenticationTokenUtil.clearAuthentication();
						}
					} else if(userAgent.equals(GlobalConstant.USER_AGENT_SLAVE)){
						String devId = HttpParamUtils.getDeviceId(req);
						if (!StringUtils.isBlank(devId)) {
							//从服务器自动登录
							appLogin.slaveLogin(orgId, devId, userAgent);
							SessionUtils.setCurrentUser(req, SecurityUtil.getSecurityUser());
						}else{
							AuthenticationTokenUtil.clearAuthentication();
						}
					}else if(userAgentTemp.toLowerCase().contains(GlobalConstant.WX_USER_AGENT.toLowerCase())&&orgId.equals("swhy")){//解决微信登录问题临时添加
						appLogin.weixinLogin(orgId, orgId, orgId, GlobalConstant.USER_AGENT_WEIXIN);
						SessionUtils.setCurrentUser(req, SecurityUtil.getSecurityUser());
					}
				}
			} catch (Exception e) {
				log.error("doFilter:[orgId#"+orgId+"]:登录异常");
				e.printStackTrace();
			}
		}
		InterceptorStatusToken token = super.beforeInvocation(fi);
		try {
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		} finally {
			super.afterInvocation(token, null);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

	public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
		return securityMetadataSource;
	}

	public void setSecurityMetadataSource(
			FilterInvocationSecurityMetadataSource securityMetadataSource) {
		this.securityMetadataSource = securityMetadataSource;
	}

}
