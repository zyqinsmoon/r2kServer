package com.apabi.r2k.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.dao.AuthUserRoleDao;
import com.apabi.r2k.security.model.AuthUserRole;
import com.apabi.r2k.security.service.AuthUserRoleService;


@Service("authUserRoleService")
public class AuthUserRoleServiceImpl implements AuthUserRoleService{

	@Resource
	private AuthUserRoleDao authUserRoleDao;
	
	
	public Page findByPageRequest(PageRequest pr) throws Exception {
		return authUserRoleDao.findByPageRequest(pr);
	}
	

	public List<AuthUserRole> findAuthUserRole(Long userId) throws Exception{
		return this.authUserRoleDao.findAuthUserRole(userId);
	}
	public void removeByUserId(Long userId) throws Exception {
		authUserRoleDao.removeByUserId(userId);
		
	}
	public void removeByRoleId(Long roleId) throws Exception {
		authUserRoleDao.removeByRoleId(roleId);
		
	}


	@Override
	public void saveOrUpdate(AuthUserRole authUserRole) throws Exception {
		authUserRoleDao.saveOrUpdate(authUserRole);
		
	}
	
	
}
