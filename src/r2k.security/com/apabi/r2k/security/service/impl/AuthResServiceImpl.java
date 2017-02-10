package com.apabi.r2k.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.dao.AuthResDao;
import com.apabi.r2k.security.model.AuthRes;
import com.apabi.r2k.security.service.AuthResService;


@Service("authResService")
public class AuthResServiceImpl implements AuthResService{

	@Resource
	private AuthResDao authResDao;
	
	public Page findByPageRequest(PageRequest pr) throws Exception {
		return authResDao.findByPageRequest(pr);
	}
	
	/**
	 * 获取全部资源对应的权限
	 * @return
	 */
	public List<AuthRes> loadAllAuthResRole() {
		return authResDao.loadAllAuthResRole();
	}
	public List<AuthRes> loadAuthResByRoleIds(String roleIds) throws Exception{
		return authResDao.loadAuthResByRoleIds(roleIds);
	}
	public List<AuthRes> loadAuthResByRoleId(java.lang.Long roleId) throws Exception{
		return authResDao.loadAuthResByRoleId(roleId);
	}
	public Long generateKey() throws Exception {
		return authResDao.generateKey();
	}
	public void save(AuthRes authRes) {
		 authResDao.save(authRes);
	}

	@Override
	public AuthRes get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOrUpdate(AuthRes authRes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeById(Long resId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
