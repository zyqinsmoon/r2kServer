package com.apabi.r2k.security.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.dao.AuthRoleDao;
import com.apabi.r2k.security.model.AuthRole;
import com.apabi.r2k.security.service.AuthRoleService;


@Service("authRoleService")
public class AuthRoleServiceImpl implements AuthRoleService{

	@Resource
	private AuthRoleDao authRoleDao;
	
	
	public AuthRoleDao getAuthRoleDao() {
		return authRoleDao;
	}
	/*public Boolean isRoleNameExist(String roleName){
		return authRoleDao.findBy("roleName",roleName).size()>0;
	}*/
	public Page findByPageRequest(PageRequest pr) throws Exception {
		return authRoleDao.findByPageRequest(pr);
	}
	public Page findByPageRequestOrgFiltered(PageRequest pageRequest) throws Exception {
		return authRoleDao.findByPageRequestOrgFiltered(pageRequest);
	}
	public List<AuthRole> getRolesByUserId(Long userId) throws Exception {
		return authRoleDao.getRolesByUserId(userId);
		
	}
	public List<AuthRole> getRolesByOrgId(Long orgId) throws Exception{
		return authRoleDao.getRolesByOrgId(orgId);
	}
	public java.lang.Long generateKey() throws Exception{
		return authRoleDao.generatorKey();
	}
	/*public void save(AuthRole authRole){
		authRoleDao.save(authRole);
	}*/
	@Override
	public Boolean isRoleNameExist(String roleName) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void save(AuthRole authRole) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void saveOrUpdate(AuthRole authRole) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public AuthRole get(Long roleId) throws Exception{
		return authRoleDao.getAuthRole(roleId);
	}
	@Override
	public void removeByIds(String[] ids) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<AuthRole> queryByEnum(String enumCode) throws Exception {
		Map param = new HashMap();
		param.put("menuType", enumCode);
		return this.authRoleDao.queryByEnum(param);
	}
	public List<AuthRole> queryAllByEnum(List<String> enumCodes) throws Exception {
		return this.authRoleDao.queryAllByEnum(enumCodes);
	}
	@Override
	public List<AuthRole> getRolesByOrg(String orgId) throws Exception {
		if(orgId == null){
			return null;
		}
		return authRoleDao.getRolesByOrg(orgId);
	}
	
	public List<AuthRole> getRoles(String orgId,String deviceType,int type) throws Exception{
		return authRoleDao.getRoles(orgId, deviceType, type);
	}
	
}
