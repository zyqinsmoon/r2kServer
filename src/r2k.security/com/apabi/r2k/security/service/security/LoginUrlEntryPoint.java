package com.apabi.r2k.security.service.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor.GOLD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.apabi.r2k.api.exception.ApiException;
import com.apabi.r2k.common.template.TemplateProcessor;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.HttpParamUtils;


/**
 * 自定义登陆跳转.
 * 
 *
 */
public class LoginUrlEntryPoint implements AuthenticationEntryPoint { 
	
	private Logger log = LoggerFactory.getLogger(LoginUrlEntryPoint.class);
	
    public void commence(HttpServletRequest request, HttpServletResponse response,
              AuthenticationException authException) throws IOException, ServletException {
       /* String url = request.getRequestURI();
        if (url.indexOf("/publisher/") != -1) {
        	//未登录而访问出版商受控资源时，跳转到后台登录页面
            targetUrl = "/publisher/login.jsp";
        } else if (url.indexOf("/seller/") != -1) {
            //未登录而访问渠道控资源时，跳转到后台登录页面
            targetUrl = "/seller/login.jsp";
        } else {
            //未登录而访问前台受控资源时，跳转到前台登录页面
            targetUrl = "/admin/login.jsp";
        }*/
    	try {
			String targetUrl = null;
			String url = request.getRequestURI();
			String userAgent = HttpParamUtils.getUserAgent(request);
			String orgId = HttpParamUtils.getOrgid(request);
			log.info("commence:[userAgent#"+userAgent+",orgId#"+orgId+"]:无权限处理器");
			//判断是否为后台用户登录
			if(url.indexOf(GlobalConstant.PREFIX_LOGIN_ADMIN) != -1||userAgent.equals(GlobalConstant.USER_AGENT_ADMIN)){
				targetUrl = "/admin/login.jsp";
				targetUrl = request.getContextPath() + targetUrl;
				response.sendRedirect(targetUrl);
				return;
			} 
			/*
			 * 下面的流程判断客户端及错误类型，判断流程：
			 * 1 先判断机构id是否为空
			 * 2 判断是否为ipad、iphone或者微信登录（三者错误类型一致，且除了触摸屏和缓存服务器外，只有这三种设备类型）
			 * 3 判断触摸屏和缓存服务器的错误类型
			 */
			if(StringUtils.isBlank(orgId)){
				TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "机构id为空"),response);
				return;
			}
			if(!userAgent.equals(GlobalConstant.USER_AGENT_ANDROID_LARGE) && !userAgent.equals(GlobalConstant.USER_AGENT_SLAVE)){
				TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "用户名或密码错误"),response);
				return;
			}
			String devId = HttpParamUtils.getDeviceId(request);
			if(StringUtils.isBlank(devId)){
				TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "设备id为空"),response);
			}else{
				TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "设备未注册或在线设备已达上限"),response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//        if(userAgent.equals(GlobalConstant.USER_AGENT_ANDROID_LARGE)){
//        	if(StringUtils.isBlank(orgId)){
//        		TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "机构id为空"),response);
//        	}else if(StringUtils.isBlank(devId)){
//        		TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "设备id为空"),response);
//        	}else{
//        		TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "设备未注册或在线设备已达上限"),response);
//        	}
//        }else if(userAgent.equals(GlobalConstant.USER_AGENT_IPAD) || userAgent.equals(GlobalConstant.USER_AGENT_IPHONE)){
//        	if(StringUtils.isBlank(orgId)){
//        		TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "机构id为空"),response);
//        	}else{
//        		TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "用户名或密码错误"),response);
//        	}
//        }else if(userAgent.equals(GlobalConstant.USER_AGENT_SLAVE)){
//        	String devId = SessionUtils.getDeviceId(request);
//        	if(StringUtils.isBlank(orgId)){
//        		TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "机构id为空"),response);
//        	}else if(StringUtils.isBlank(devId)){
//        		TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "设备id为空"),response);
//        	}else{
//        		TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", authException.getMessage()),response);
//        	}
//        }else{
//        	targetUrl = "/admin/login.jsp";
//        	targetUrl = request.getContextPath() + targetUrl;
//        	response.sendRedirect(targetUrl);
//        }
    }

}
