package com.apabi.r2k.security.utils;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticationTokenUtil {

	//创建用户认证token
	public static UsernamePasswordAuthenticationToken createAuthToken(UserDetails userDetails) throws Exception{
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		authToken.setAuthenticated(false);
		return authToken;
	}
	
	//将用户认证信息添加到session中
	public static boolean addAuthenticationToContext(AbstractAuthenticationToken token) throws Exception{
		SecurityContext sc = SecurityContextHolder.getContext();		//获取spring security认证信息上下文范围
		if(sc != null){
			//将验证成功的用户信息放入上下文环境中
			sc.setAuthentication(token);
			return true;
		}
		return false;
	}
	
	//创建Authentication并将其放入上下文环境中
	public static boolean createAndAddToContext(UserDetails userDetails) throws Exception{
		AbstractAuthenticationToken authToken = createAuthToken(userDetails);
		return addAuthenticationToContext(authToken);
	}
	
	//清理用户认证信息
	public static void clearAuthentication(){
		SecurityContext sc = SecurityContextHolder.getContext();		//获取spring security认证信息上下文范围
		if(sc != null){
			sc.setAuthentication(null);
		}
	}
}
