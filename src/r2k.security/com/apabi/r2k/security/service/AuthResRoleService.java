package com.apabi.r2k.security.service;

import java.util.List;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.model.AuthResRole;

public interface AuthResRoleService {

	public Page findByPageRequest(PageRequest pr) throws Exception;

	public void removeByRoleId(Long roleId) throws Exception;

	public List<AuthResRole> getByRoleId(Long roleId) throws Exception;

	public void removeByResId(Long id) throws Exception;

	public List<AuthResRole> getByResId(Long resId) throws Exception;

	public void saveOrUpdate(AuthResRole authResRole) throws Exception;

	public void removeById(Long id) throws Exception;

}
