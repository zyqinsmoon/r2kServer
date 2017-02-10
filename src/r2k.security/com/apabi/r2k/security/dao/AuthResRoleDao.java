package com.apabi.r2k.security.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.model.AuthResRole;


@Component
public interface AuthResRoleDao {

	
	public void saveOrUpdate(AuthResRole entity)throws Exception;
	
	public Page findByPageRequest(PageRequest pageRequest)throws Exception;
	public void removeByRoleId(Long roleId)throws Exception;

	public List<AuthResRole> getByRoleId(Long roleId)throws Exception;

	public List<AuthResRole> findBy(String string, Long resId);

	public void deleteBy(String string, Long id);
	
	
}
