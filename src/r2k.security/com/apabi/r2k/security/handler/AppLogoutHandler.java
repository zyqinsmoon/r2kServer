package com.apabi.r2k.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.apabi.r2k.api.exception.ApiException;
import com.apabi.r2k.common.template.TemplateProcessor;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.HttpParamUtils;
import com.apabi.r2k.common.utils.ShuYuanResult;
import com.apabi.r2k.security.app.AppLogout;
import com.apabi.r2k.security.utils.SessionUtils;

public class AppLogoutHandler extends SimpleUrlLogoutSuccessHandler {

	private AppLogout appLogout;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		try {
			String userAgent = HttpParamUtils.getUserAgent(request);
			String orgId = HttpParamUtils.getOrgid(request);
			String token = SessionUtils.getToken(request);
			String message = "注销成功";
			if(userAgent.equals(GlobalConstant.USER_AGENT_IPAD) || userAgent.equals(GlobalConstant.USER_AGENT_IPHONE)){
				String code = appLogout.iosLogout(orgId, token);
				if(code == ShuYuanResult.LOGOUT_CODE_FAIL){
					message = "注销失败";
				}
				TemplateProcessor.generateResponse("Success.xml", ApiException.makeMode("0", message),response);
			}else{
				//微信注销登录流程
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AppLogout getAppLogout() {
		return appLogout;
	}

	public void setAppLogout(AppLogout appLogout) {
		this.appLogout = appLogout;
	}
}
