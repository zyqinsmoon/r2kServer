/**
 *
 */
package com.apabi.r2k.security.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;

import com.apabi.r2k.common.security.springsecurity.CustomWebAuthenticationDetails;
import com.apabi.r2k.security.userdetails.MyUserDetails;

/**
 * 扩展{@link DaoAuthenticationProvider}，支持IP范围验证.
 * 
 *
 */
public class MyAuthenticationProvider extends DaoAuthenticationProvider {

	private static Logger log = LoggerFactory.getLogger(MyAuthenticationProvider.class);
	
	@Override
	protected void additionalAuthenticationChecks(final UserDetails userDetails,
            final UsernamePasswordAuthenticationToken authentication) {
		super.additionalAuthenticationChecks(userDetails, authentication);
		
//		if ("anonymousUser".equals(authentication.getName())) {
//			return;
//		}

		if (!(userDetails instanceof MyUserDetails)) {
			throw new BadCredentialsException(messages.getMessage(
                    "MyAuthenticationProvider.badCredentials", "Bad Credentials"),
                    userDetails);
		}
		
		final CustomWebAuthenticationDetails authDetails = (CustomWebAuthenticationDetails) authentication.getDetails();
		/*final String requestIp = authDetails.getRealRemoteIp();
		log.debug("请求的IP地址为： " + requestIp);*/
		
	
	}
}
