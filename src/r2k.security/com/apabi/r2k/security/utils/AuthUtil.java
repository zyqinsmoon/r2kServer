package com.apabi.r2k.security.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.apabi.r2k.security.model.AuthRole;

public class AuthUtil {

	public static Collection<GrantedAuthority> obtainGrantedAuthorities(List<AuthRole> authRoles) {
		final Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
		for (final AuthRole authRole : authRoles) {
			authSet.add(new GrantedAuthorityImpl(authRole.getRoleCode()));
		}
		return authSet;
	}
	
	public static Authentication getAuthentication(){
		SecurityContext sc = SecurityContextHolder.getContext();
		if(sc != null){
			return sc.getAuthentication();
		}
		return null;
	}
	
	public static boolean isAnonymous(){
		Authentication authentication = getAuthentication();
		if(authentication instanceof AnonymousAuthenticationToken){
			return true;
		}
		return false;
	}
}
