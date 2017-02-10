package com.apabi.r2k.security.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import com.apabi.r2k.common.security.springsecurity.AuthenticationSuccessCallback;
import com.apabi.r2k.security.utils.SecurityUtil;
import com.apabi.r2k.security.utils.SessionUtils;

public class AdminSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	protected final Log logger = LogFactory.getLog(this.getClass());

    private RequestCache requestCache = new HttpSessionRequestCache();

    private List<AuthenticationSuccessCallback> callbacks = new ArrayList<AuthenticationSuccessCallback>();
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
    		HttpServletResponse response, Authentication authentication)
    		throws IOException, ServletException {
    	// 回调入口
    	for (AuthenticationSuccessCallback c : callbacks) {
    		c.run(request, response, authentication);
    	}
    	
    	SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		if (savedRequest == null) {
			super.onAuthenticationSuccess(request, response, authentication);
			return;
		}
		
		if (isAlwaysUseDefaultTargetUrl() || StringUtils.hasText(request.getParameter(getTargetUrlParameter()))) {
			requestCache.removeRequest(request, response);
			super.onAuthenticationSuccess(request, response, authentication);
			return;
		}
		
		// Use the DefaultSavedRequest URL
		String targetUrl = savedRequest.getRedirectUrl();
		logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
    
    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }

	public void setCallbacks(List<AuthenticationSuccessCallback> callbacks) {
		this.callbacks = callbacks;
	}
}
