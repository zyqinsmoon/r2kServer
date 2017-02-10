package com.apabi.r2k.security.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.model.AuthEntity;




@Component
public interface AuthEntityDao {

	public void saveOrUpdate(AuthEntity entity) throws Exception ;
	
	public Page findByPageRequest(PageRequest pageRequest) throws Exception;
	
	public List<AuthEntity> getAuthTree(Map params) throws Exception;
}
