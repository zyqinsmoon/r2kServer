package com.apabi.r2k.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.dao.AuthResRoleDao;
import com.apabi.r2k.security.model.AuthResRole;
import com.apabi.r2k.security.service.AuthResRoleService;


@Service("authResRoleService")
public class AuthResRoleServiceImpl implements AuthResRoleService{

	@Resource
	private AuthResRoleDao authResRoleDao;
	
	
	public AuthResRoleDao getAuthResRoleDao() {
		return authResRoleDao;
	}
	public void setAuthResRoleDao(AuthResRoleDao authResRoleDao) {
		this.authResRoleDao = authResRoleDao;
	}
	public Page findByPageRequest(PageRequest pr) throws Exception {
		return authResRoleDao.findByPageRequest(pr);
	}
	public void removeByRoleId(Long roleId) throws Exception{		
		authResRoleDao.removeByRoleId(roleId);
	}
	public List<AuthResRole> getByRoleId(Long roleId) throws Exception{
		return authResRoleDao.getByRoleId(roleId);
	}
	public void removeByResId(Long id) {
		// TODO Auto-generated method stub
		authResRoleDao.deleteBy("resId",id);
	}
	public List<AuthResRole> getByResId(Long resId) {
		// TODO Auto-generated method stub
		return authResRoleDao.findBy("resId",resId);
	}
	@Override
	public void saveOrUpdate(AuthResRole authResRole) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeById(Long id) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
