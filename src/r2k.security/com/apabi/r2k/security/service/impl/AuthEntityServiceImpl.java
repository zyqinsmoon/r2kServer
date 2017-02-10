package com.apabi.r2k.security.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.dao.AuthEntityDao;
import com.apabi.r2k.security.model.AuthEntity;
import com.apabi.r2k.security.service.AuthEntityService;


@Service("authEntityService")
public class AuthEntityServiceImpl implements AuthEntityService{

	
	@Resource
	private AuthEntityDao authEntityDao;
	
	

	public AuthEntityDao getAuthEntityDao() {
		return authEntityDao;
	}

	public void setAuthEntityDao(AuthEntityDao authEntityDao) {
		this.authEntityDao = authEntityDao;
	}

	public Page findByPageRequest(PageRequest pr) throws Exception {
		return authEntityDao.findByPageRequest(pr);
	}

	@Override
	public AuthEntity get(Long entityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AuthEntity> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AuthEntity> getAuthTree(String[] roleIds) throws Exception {
		Map params = new HashMap();
		params.put("roleIds", roleIds);
		return authEntityDao.getAuthTree(params);
	}
	
}
