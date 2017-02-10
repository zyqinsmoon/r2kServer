package com.apabi.r2k.common.security.springsecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.apabi.r2k.security.model.AuthUser;
import com.apabi.r2k.security.userdetails.MyUserDetails;



/**
 * Spring Security的工具类.
 *
 * @author calvin
 */
public abstract class SpringSecurityUtils {

	private SpringSecurityUtils() {
	}

	/**
	 * 取得当前用户的登录名,如果无已登录用户则返回null.
	 */
	public static String getCurrentUserName() {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return null;
		}
		return auth.getName();
	}


	public static AuthUser getCurrentUser() {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if ((auth != null) && (auth.getPrincipal() instanceof MyUserDetails)) {
			final MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
			return userDetails.getAuthUser();
		} else {
			return null;
		}
	}
	
	public static boolean isCurrentAdminSa() {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if ((auth != null) && (auth.getPrincipal() instanceof MyUserDetails)) {
			final MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
			return userDetails.isSa();
		} else {
			return false;
		} 
	}
}
