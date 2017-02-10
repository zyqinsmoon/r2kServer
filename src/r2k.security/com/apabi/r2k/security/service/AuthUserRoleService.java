package com.apabi.r2k.security.service;

import java.util.List;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.model.AuthUserRole;


public interface AuthUserRoleService {
	public Page findByPageRequest(PageRequest pr)throws Exception ;
	public List<AuthUserRole> findAuthUserRole(Long userId) throws Exception;
	public void removeByUserId(Long userId) throws Exception;
	public void removeByRoleId(Long roleId) throws Exception;
	public void saveOrUpdate(AuthUserRole authUserRole )throws Exception;
	
	
}
