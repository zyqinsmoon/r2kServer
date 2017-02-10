package com.apabi.r2k.security.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.model.AuthRole;


@Component
public interface AuthRoleDao{

	
	public void saveOrUpdate(AuthRole entity)throws Exception ;
	
	public Page findByPageRequest(PageRequest pageRequest)throws Exception;
	public Page findByPageRequestOrgFiltered(PageRequest pageRequest)throws Exception;
	public List<AuthRole> getRolesByUserId(Long userId) throws Exception;
	public List<AuthRole> getRolesByOrgId(Long orgId)throws Exception;
	public java.lang.Long generatorKey()throws Exception;
	public List<AuthRole> queryAllByEnum(List<String> enumCodes)throws Exception;

	public List<AuthRole> getRolesByOrg(String orgId) throws Exception;
	//根据查询条件获取authrole
	public List<AuthRole> getRoles(String orgId,String deviceType,int type) throws Exception;
	//public List<AuthRole> findBy(String roleNameString, String roleName);

	//public void save(AuthRole authRole);
	//根据ID查询AuthRole
	public AuthRole getAuthRole(long id) throws Exception;

	public List<AuthRole> queryByEnum(Map<String,Object> map) throws Exception;
	
}
