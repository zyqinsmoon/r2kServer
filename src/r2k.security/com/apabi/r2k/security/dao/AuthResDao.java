package com.apabi.r2k.security.dao;



import java.util.List;

import org.springframework.stereotype.Component;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.model.AuthRes;


@Component
public interface AuthResDao {
	public void saveOrUpdate(AuthRes entity)throws Exception;
	
	public Page findByPageRequest(PageRequest pageRequest)throws Exception;
	
	//获取全部资想对应的权限
	public List<AuthRes> loadAllAuthResRole();
	public List<AuthRes> loadAuthResByRoleIds(String roleIds)throws Exception;

	public List<AuthRes> loadAuthResByRoleId(Long roleId)throws Exception ;

	public Long generateKey()throws Exception;

	public int save(AuthRes authRes);
	
}
