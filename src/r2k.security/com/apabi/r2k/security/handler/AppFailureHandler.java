package com.apabi.r2k.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.apabi.r2k.api.exception.ApiException;
import com.apabi.r2k.common.template.TemplateProcessor;
import com.apabi.r2k.common.utils.HttpParamUtils;

public class AppFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private Logger log = LoggerFactory.getLogger(AppFailureHandler.class);
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		try {
			String userAgent = HttpParamUtils.getUserAgent(request);
			String orgId = HttpParamUtils.getOrgid(request);
			log.info("onAuthenticationFailure:[userAgent#"+userAgent+",orgId#"+orgId+"]:登录失败处理器");
			TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1004", exception.getMessage()),response);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		if((orgId != null && orgId.equals(GlobalConstant.ORGID_WEIXIN)) || userAgent.contains(GlobalConstant.USER_AGENT_IPAD) || userAgent.contains(GlobalConstant.USER_AGENT_ANDROID_LARGE) || userAgent.contains(GlobalConstant.USER_AGENT_SLAVE) || userAgent.equals(GlobalConstant.USER_AGENT_IPHONE)){
//		}else{
//			super.onAuthenticationFailure(request, response, exception);
//		}
	}
}
