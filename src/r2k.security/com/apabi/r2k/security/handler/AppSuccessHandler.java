package com.apabi.r2k.security.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.protocol.HTTP;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.apabi.r2k.common.security.springsecurity.AuthenticationSuccessCallback;
import com.apabi.r2k.common.template.TemplateException;
import com.apabi.r2k.common.template.TemplateProcessor;
import com.apabi.r2k.common.utils.CookieUtil;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.HttpParamUtils;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.ShuyuanUtil;
import com.apabi.r2k.security.model.AuthUser;
import com.apabi.r2k.security.utils.SessionUtils;

public class AppSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	protected final Log logger = LogFactory.getLog(this.getClass());

    private List<AuthenticationSuccessCallback> callbacks = new ArrayList<AuthenticationSuccessCallback>();
 
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
    		HttpServletResponse response, Authentication authentication)
    		throws IOException, ServletException {
    	// 回调入口
    	for (AuthenticationSuccessCallback c : callbacks) {
    		c.run(request, response, authentication);
    	}
    	String userAgent = HttpParamUtils.getUserAgent(request);
    	try {
			if(userAgent.equals(GlobalConstant.USER_AGENT_IPAD) || userAgent.equals(GlobalConstant.USER_AGENT_IPHONE)){
				//登录成功返回xml
				response.setCharacterEncoding(HTTP.UTF_8);
				TemplateProcessor.process("pad-login-success.xml", createIOSTemplateModel(request), response.getWriter());
			}else{
				//微信登录操作
				CookieUtil.saveCookie("token", SessionUtils.getToken(request), response);
				TemplateProcessor.process("weixin-login-success.xml", createWeixinTemplateModel(request), response.getWriter());
			}
		} catch (TemplateException e) {
			e.printStackTrace();
		}
    }
    
    private Map<String,Object> createIOSTemplateModel(HttpServletRequest request){
    	Map<String,Object> model = new HashMap<String, Object>();
    	AuthUser authUser = SessionUtils.getCurrentUser(request);
    	model.put("orgId", authUser.getAuthOrg().getOrgId());
    	model.put("orgname", authUser.getAuthOrg().getOrgName());
    	model.put("userId", authUser.getUserName());
    	model.put("token", authUser.getToken());
    	model.put("jsessionId", request.getSession().getId());
    	model.put("menuurl", PropertiesUtil.get("url.r2k.api")+"/"+PropertiesUtil.get("path.menu"));
    	model.put("baseurl", ShuyuanUtil.APABI_URL+"/");
    	return model;
    }
    
    private Map<String,Object> createWeixinTemplateModel(HttpServletRequest request){
    	Map<String,Object> model = new HashMap<String, Object>();
    	AuthUser authUser = SessionUtils.getCurrentUser(request);
    	model.put("orgId", authUser.getAuthOrg().getOrgId());
    	model.put("userId", authUser.getUserName());
    	model.put("token", authUser.getToken());
    	model.put("jsessionId", request.getSession().getId());
    	return model;
    }

    private String getReturnUrl(HttpServletRequest request){
    	String returnUrl = request.getParameter("returnUrl");
    	return returnUrl;
    }
    
	public List<AuthenticationSuccessCallback> getCallbacks() {
		return callbacks;
	}

	public void setCallbacks(List<AuthenticationSuccessCallback> callbacks) {
		this.callbacks = callbacks;
	}
}
