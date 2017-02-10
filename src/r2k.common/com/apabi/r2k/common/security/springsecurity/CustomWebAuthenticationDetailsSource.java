package com.apabi.r2k.common.security.springsecurity;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;


/**
 * 使用{@link CustomWebAuthenticationDetails}替换{@link WebAuthenticationDetails}.
 *
 */
public class CustomWebAuthenticationDetailsSource extends
		WebAuthenticationDetailsSource {
	
	public CustomWebAuthenticationDetailsSource() {
		setClazz(CustomWebAuthenticationDetails.class);
	}
}
