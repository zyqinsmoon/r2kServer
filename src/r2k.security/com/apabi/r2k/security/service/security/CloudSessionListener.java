package com.apabi.r2k.security.service.security;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class CloudSessionListener implements HttpSessionListener {

	//创建session设置
	public void sessionCreated(HttpSessionEvent event) {
		// nothing to do.
	}

    //退出设置
	public void sessionDestroyed(HttpSessionEvent event) {
		
	}

}
