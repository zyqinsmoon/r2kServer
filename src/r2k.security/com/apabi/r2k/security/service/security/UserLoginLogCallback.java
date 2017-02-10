package com.apabi.r2k.security.service.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import com.apabi.r2k.common.security.springsecurity.AuthenticationSuccessCallback;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.model.AuthUser;
import com.apabi.r2k.security.userdetails.MyUserDetails;
import com.apabi.r2k.security.utils.SessionUtils;




/**
 * 
 */
public class UserLoginLogCallback implements AuthenticationSuccessCallback {
	
	public void run(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		Object obj = authentication.getPrincipal();
		if (obj instanceof MyUserDetails) {
			MyUserDetails userDetail = (MyUserDetails) obj;
		    AuthUser authUser = userDetail.getAuthUser();
		    authUser.setLastUpdate(new Date());
		    authUser.setPassword(null);
		    AuthOrg authOrg = authUser.getAuthOrg();
		    SessionUtils.setCurrentUser(request, authUser);
		    request.getSession().setAttribute(GlobalConstant.CURRENT_ORG, authOrg);
			
		}
	}
	
}
