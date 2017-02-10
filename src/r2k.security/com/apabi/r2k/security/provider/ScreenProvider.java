package com.apabi.r2k.security.provider;

import javax.annotation.Resource;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.apabi.r2k.admin.model.Device;
import com.apabi.r2k.admin.service.DeviceService;
import com.apabi.r2k.security.model.AuthUser;
import com.apabi.r2k.security.userdetails.MyUserDetails;
import com.apabi.r2k.security.userdetails.UserDetailFactory;

public class ScreenProvider extends AbstractUserDetailsAuthenticationProvider {

	@Resource
	private DeviceService deviceService;
	@Resource
	private UserDetailFactory userDetailFactory;
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		Object devObj = authentication.getPrincipal();
		Object orgObj = authentication.getCredentials();
		if (orgObj == null) {
            logger.debug("Authentication failed: 机构id为空");

            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }


        if (devObj == null) {
            logger.debug("Authentication failed: 设备id为空");

            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
	}

	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		Object devObj = authentication.getPrincipal();
		Object orgObj = authentication.getCredentials();
		String userAgent = "";
		String softVersion = "";
		Object userDetailsObj = authentication.getDetails();
		if(userDetailsObj instanceof MyUserDetails){
			MyUserDetails tmpUserDetails = (MyUserDetails) authentication.getDetails();
			if(tmpUserDetails != null){
				AuthUser user = tmpUserDetails.getAuthUser();
				userAgent = user.getUserAgent();
				softVersion = user.getSoftVersion();
			}
		}
		MyUserDetails userDetails = null;
		if(devObj instanceof UserDetails){
			return (UserDetails) devObj;
		}else if(devObj instanceof String && orgObj instanceof String){
			try {
				String orgId = (String)orgObj;
				String deviceId = (String)devObj;
				Device dev = deviceService.getDeviceByDeviceId(deviceId, orgId);
				if(dev != null){
					userDetails = userDetailFactory.createScreenUserDeatil(orgId, deviceId, userAgent, softVersion);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (userDetails == null) {
            throw new AuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return userDetails;
	}
	
	

}
