package com.apabi.r2k.security.service;

import java.util.List;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.model.AuthEntity;




public interface AuthEntityService{
	public Page findByPageRequest(PageRequest pr) throws Exception;
	public AuthEntity get(Long entityId);
	public List<AuthEntity> findAll();
	
	public List<AuthEntity> getAuthTree(String[] roleIds) throws Exception;
}
