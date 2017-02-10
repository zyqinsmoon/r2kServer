package com.apabi.r2k.common.security.springsecurity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

/**
 * 登录验证成功的回调接口，供各个模块扩展.
 * 
 * @author minwh
 */
public interface AuthenticationSuccessCallback {

	void run(HttpServletRequest request, HttpServletResponse response,
           Authentication authentication);
}
