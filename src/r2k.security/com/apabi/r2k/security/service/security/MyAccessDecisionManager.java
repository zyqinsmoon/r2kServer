package com.apabi.r2k.security.service.security;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

//扩展AccessDecisionManager,以执行自己的Decide决策处理
public class MyAccessDecisionManager implements AccessDecisionManager {
	
	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		 if(configAttributes == null) {  
			  return;  
			}  
		 //所请求的资源拥有的权限(一个资源对多个权限) 
		 Iterator<ConfigAttribute> iterator = configAttributes.iterator();  
			 while(iterator.hasNext()) {  
			     ConfigAttribute configAttribute = iterator.next();  
		           //访问所请求资源所需要的权限   
			     String needPermission = configAttribute.getAttribute(); 
			            //用户所拥有的权限authentication   
			     for(GrantedAuthority ga : authentication.getAuthorities()) {  
			        if(needPermission.equals(ga.getAuthority())) {  
			           return;  
			          }  
			       }  
		 }
		throw new AccessDeniedException(" 没有权限访问！ ");

	}
	//默认支持
	public boolean supports(ConfigAttribute attribute) {
		
		return true;
	}
	//默认支持
	public boolean supports(Class<?> clazz) {
		
		return true;
	}

}
