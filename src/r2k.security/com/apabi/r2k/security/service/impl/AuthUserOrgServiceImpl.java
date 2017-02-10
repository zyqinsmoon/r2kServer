package com.apabi.r2k.security.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.dao.AuthUserOrgDao;
import com.apabi.r2k.security.service.AuthUserOrgService;


@Service("authUserOrgService")
public class AuthUserOrgServiceImpl implements AuthUserOrgService{

	@Resource
	private AuthUserOrgDao authUserOrgDao;
	public Page findByPageRequest(PageRequest pr) throws Exception {
		return authUserOrgDao.findByPageRequest(pr);
	}
	
}
