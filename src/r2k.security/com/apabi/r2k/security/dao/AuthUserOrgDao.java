package com.apabi.r2k.security.dao;

import org.springframework.stereotype.Component;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.model.AuthUserOrg;


@Component
public interface AuthUserOrgDao  {

	public void saveOrUpdate(AuthUserOrg entity)throws Exception ;
	
	public Page findByPageRequest(PageRequest pageRequest) throws Exception ;
}
