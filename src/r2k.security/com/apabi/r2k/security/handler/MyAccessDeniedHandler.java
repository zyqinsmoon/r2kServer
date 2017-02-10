package com.apabi.r2k.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import com.apabi.r2k.api.exception.ApiException;
import com.apabi.r2k.common.template.TemplateProcessor;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.HttpParamUtils;
import com.apabi.r2k.security.utils.SessionUtils;

public class MyAccessDeniedHandler extends AccessDeniedHandlerImpl {

	private Logger log = LoggerFactory.getLogger(MyAccessDeniedHandler.class);
	
	@Override
	public void setErrorPage(String errorPage) {
		super.setErrorPage(errorPage);
	}
	
	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException,
			ServletException {
		try {
//			String userAgent = HttpParamUtils.getUserAgent(request);
			String userAgent = SessionUtils.getUserAgent(request);
			String orgId = HttpParamUtils.getOrgid(request);
			log.info("handle:[userAgent#"+userAgent+",orgId#"+orgId+"]:权限不足异常处理");
			//后台处理方法
			if(userAgent.equals(GlobalConstant.USER_AGENT_ADMIN)){
				super.handle(request, response, accessDeniedException);
				return;
			}
			/*
			 * 客户端处理流程
			 * 1 先判断机构id是否为空
			 * 2 然后判断是否为触摸屏或者
			 */
			if(StringUtils.isBlank(orgId)){
				TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "机构id为空"),response);
			}
			if(userAgent.equals(GlobalConstant.USER_AGENT_ANDROID_LARGE) || userAgent.equals(GlobalConstant.USER_AGENT_SLAVE)){
				String devId = HttpParamUtils.getDeviceId(request);
				if(StringUtils.isBlank(devId)){
					TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "设备id为空"),response);
				}
			}
			TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "权限不足"),response);
		} catch (Exception e) {
			e.printStackTrace();
		}
//        if(!StringUtils.isBlank(orgId) && orgId.equals(GlobalConstant.ORGID_WEIXIN)){
//        	TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "机构"+orgId+"无权限"),response);
//        }else if(userAgent.equals(GlobalConstant.USER_AGENT_ANDROID_LARGE)){
//        	String devId = SessionUtils.getDeviceId(request);
//        	if(StringUtils.isBlank(orgId)){
//        		TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "机构id为空"),response);
//        	}else if(StringUtils.isBlank(devId)){
//        		TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "设备id为空"),response);
//        	}else{
//        		TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "权限不足"),response);
//        	}
//        }else if(userAgent.equals(GlobalConstant.USER_AGENT_IPAD) || userAgent.equals(GlobalConstant.USER_AGENT_IPHONE)){
//        	if(StringUtils.isBlank(orgId)){
//        		TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "机构id为空"),response);
//        	}else{
//        		TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "权限不足"),response);
//        	}
//        }else if(userAgent.equals(GlobalConstant.USER_AGENT_SLAVE)){
//        	String devId = SessionUtils.getDeviceId(request);
//        	if(StringUtils.isBlank(orgId)){
//        		TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "机构id为空"),response);
//        	}else if(StringUtils.isBlank(devId)){
//        		TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "设备id为空"),response);
//        	}else{
//        		TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "权限不足"),response);
//        	}
//        }else{
//        	super.handle(request, response, accessDeniedException);
//        }
	}
	
	//判断是否为后台访问
//	public boolean isAdminAccess(String userAgent){
//		if(userAgent.equals(GlobalConstant.USER_AGENT_ANDROID_LARGE) || userAgent.equals(GlobalConstant.USER_AGENT_IPAD) || userAgent.equals(GlobalConstant.USER_AGENT_IPHONE)
//				|| userAgent.equals(GlobalConstant.USER_AGENT_SLAVE) || userAgent.equals(GlobalConstant.USER_AGENT_WEIXIN)){
//			return false;
//		}
//		return true;
//	}
}
