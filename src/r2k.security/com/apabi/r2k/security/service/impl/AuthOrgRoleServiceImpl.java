package com.apabi.r2k.security.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.dao.AuthOrgRoleDao;
import com.apabi.r2k.security.model.AuthOrgRole;
import com.apabi.r2k.security.service.AuthOrgRoleService;

@Service("authOrgRoleService")
public class AuthOrgRoleServiceImpl implements AuthOrgRoleService {

	private Logger log = LoggerFactory.getLogger(AuthOrgRoleServiceImpl.class);
	
	public static final String PAGE_QUERY_STATEMENT = ".pageSelect";
	
	@Resource(name="authOrgRoleDao")
	private AuthOrgRoleDao authOrgRoleDao;
	
	public void setAuthOrgRoleDao(AuthOrgRoleDao authOrgRoleDao){
		this.authOrgRoleDao = authOrgRoleDao;
	}
	
	public AuthOrgRoleDao getAuthOrgRoleDao(){
		return authOrgRoleDao;
	}
	
	//入库
	@Transactional(propagation = Propagation.REQUIRED)
	public int save(AuthOrgRole authOrgRole) throws Exception {
		return authOrgRoleDao.save(authOrgRole);
	}
	
	//批量保存
	@Transactional(propagation=Propagation.REQUIRED)
	public int batchSave(List<AuthOrgRole> authOrgRoles) throws Exception{
		return this.authOrgRoleDao.batchSaveOrgRole(authOrgRoles);
	}
	
	//更新
	@Transactional(propagation = Propagation.REQUIRED)
	public int update(AuthOrgRole authOrgRole) throws Exception {
		return authOrgRoleDao.update(authOrgRole);
	}
	
	//根据ID删除
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteById(java.lang.Integer id) throws Exception {
		return authOrgRoleDao.deleteById(id);
	}
	
	//批量删除
	@Transactional(propagation = Propagation.REQUIRED)
	public int batchDelete(List<java.lang.Integer> ids) throws Exception {
		return authOrgRoleDao.batchDeleteByIds(ids);
	}
	
	//根据ID查询
	public AuthOrgRole getById(java.lang.Integer id) throws Exception {
		return authOrgRoleDao.getById(id);
	}
	
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception {
		return authOrgRoleDao.pageQuery(PAGE_QUERY_STATEMENT, pageRequest, countQuery);
	} 
	//通过机构主键删除ENUM表信息
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteByOrgId(String orgid) throws Exception{
		return this.authOrgRoleDao.deleteByOrgId(orgid);
	}
//	public List<AuthOrgRole> queryAll() throws Exception{
//		return authOrgRoleDao.queryAll();
//	}

	//通过机构主键和menutype删除角色中间表表信息
	public void deleteByEnum(String orgId, List<Long> roleIds) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("orgId", orgId);
		map.put("roleIds", roleIds);
		this.authOrgRoleDao.deleteByEnum(map);
	}
}
