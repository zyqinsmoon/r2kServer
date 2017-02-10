package com.apabi.r2k.security.service;

import java.util.List;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.model.AuthRes;



public interface AuthResService {

	
	
	public Page findByPageRequest(PageRequest pr) throws Exception ;
	
	/**
	 * 获取全部资源对应的权限
	 * @return
	 */
	public List<AuthRes> loadAllAuthResRole() throws Exception;
	public List<AuthRes> loadAuthResByRoleIds(String roleIds) throws Exception;
	public List<AuthRes> loadAuthResByRoleId(java.lang.Long roleId)throws Exception;
	public Long generateKey() throws Exception;
	public void save(AuthRes authRes)throws Exception ;

	public AuthRes get(Long id)throws Exception;

	public void saveOrUpdate(AuthRes authRes)throws Exception;

	public void removeById(Long resId) throws Exception;

	public List findAll()throws Exception;
	
}
