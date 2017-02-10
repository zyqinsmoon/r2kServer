package com.apabi.r2k.security.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.model.AuthUserRole;


@Component
public interface AuthUserRoleDao  {

	
	public void saveOrUpdate(AuthUserRole entity) throws Exception;
	
	public Page findByPageRequest(PageRequest pageRequest) throws Exception;
	public List<AuthUserRole> findAuthUserRole(Long userId)throws Exception;
	public void removeByUserId(Long userId) throws Exception;

	public void removeByRoleId(Long roleId)throws Exception;
}
