package com.apabi.r2k.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class MyLogoutHandler extends SimpleUrlLogoutSuccessHandler {

//	@Resource
//	private AuthenticationTokenServiceImpl authTokenService;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
//		String userAgent = SessionUtils.getUserAgent(request);
//		if(userAgent.equals(GlobalConstant.USER_AGENT_IPAD) || userAgent.equals(GlobalConstant.USER_AGENT_IPHONE)){
//			//登录失败返回xml
//			try {
//				String orgId = SessionUtils.getOrgId(request);
//				String token = SessionUtils.getToken(request);
//				ShuYuanResult syResult = authTokenService.getShuYuanResult(authTokenService.shuYuanLogout(orgId, token));
//				String message = "注销成功";
//				if(syResult != null && !syResult.getCode().equals("0")){
//					message = syResult.getCode();
//				}
//				TemplateProcessor.generateResponse("Success.xml", ApiException.makeMode("0", message),response);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}else{
//		}
		super.onLogoutSuccess(request, response, authentication);
	}
}
