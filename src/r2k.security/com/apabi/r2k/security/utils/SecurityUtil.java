package com.apabi.r2k.security.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.model.AuthRole;
import com.apabi.r2k.security.model.AuthUser;
import com.apabi.r2k.security.userdetails.MyUserDetails;

public class SecurityUtil {

	public static AuthUser getSecurityUser(){
		MyUserDetails userDetails = getUserDetails();
		if(userDetails != null){
			return userDetails.getAuthUser();
		}else{
			return null;
		}
	}
	
	public static MyUserDetails getUserDetails(){
		SecurityContext sc = SecurityContextHolder.getContext();
		if(sc == null){
			return null;
		}
		Authentication auth = sc.getAuthentication();
		if(auth == null){
			return null;
		}
		Object principal = auth.getPrincipal();
		if(principal instanceof MyUserDetails){
			return (MyUserDetails)principal;
		}else{
			return null;
		}
	}
	
	public static void setAuthRoles(List<AuthRole> authRoles){
		SecurityContext sc = SecurityContextHolder.getContext();
		if(sc != null){
			Authentication auth = sc.getAuthentication();
			if(auth != null){
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), obtainGrantedAuthorities(authRoles));
				sc.setAuthentication(token);
			}
		}
	}
	
	public static Collection<GrantedAuthority> obtainGrantedAuthorities(List<AuthRole> authRoles) {
		final Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
		for (final AuthRole authRole : authRoles) {
			authSet.add(new GrantedAuthorityImpl(authRole.getRoleCode()));
		}
		return authSet;
	}
	
	public static void setCurrentOrg(AuthOrg currOrg){
		AuthUser authUser = getSecurityUser();
		if(authUser != null){
			authUser.setCurrentOrg(currOrg);
		}
	}
}
