package com.apabi.r2k.security.service;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;



public interface AuthUserOrgService {

	
	public Page findByPageRequest(PageRequest pr) throws Exception ;
}
