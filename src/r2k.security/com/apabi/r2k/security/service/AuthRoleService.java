package com.apabi.r2k.security.service;

import java.util.List;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.model.AuthRole;


public interface AuthRoleService {

	
	public Boolean isRoleNameExist(String roleName)throws Exception;
	public Page findByPageRequest(PageRequest pr) throws Exception;
	public Page findByPageRequestOrgFiltered(PageRequest pageRequest)throws Exception ;
	public List<AuthRole> getRolesByUserId(Long userId)throws Exception;
	public List<AuthRole> getRolesByOrgId(Long orgId)throws Exception;
	public java.lang.Long generateKey()throws Exception;
	public void save(AuthRole authRole)throws Exception;
	public void saveOrUpdate(AuthRole authRole)throws Exception;
	public AuthRole get(Long roleId)throws Exception;
	public void removeByIds(String[] ids)throws Exception;
	public List<AuthRole> queryAllByEnum(List<String> enumCodes)throws Exception;
	public List<AuthRole> getRolesByOrg(String orgId) throws Exception;
	//根据特定查询条件获取authrole
	public List<AuthRole> getRoles(String orgId,String deviceType,int type) throws Exception;
	//通过menutype获取角色
	public List<AuthRole> queryByEnum(String enumCode) throws Exception;
}
