package com.apabi.r2k.security.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.BaseDaoImpl;
import com.apabi.r2k.security.dao.AuthOrgRoleDao;
import com.apabi.r2k.security.model.AuthOrgRole;

@Repository("authOrgRoleDao")
public class AuthOrgRoleDaoImpl extends BaseDaoImpl<AuthOrgRole, Serializable> implements AuthOrgRoleDao {

	private Logger log = LoggerFactory.getLogger(AuthOrgRoleDaoImpl.class);
	
	//入库
	public int save(AuthOrgRole authOrgRole) throws Exception {
		return baseDao.insert(getStatement("insert"), authOrgRole);
	}
	
	//批量保存
	public int batchSaveOrgRole(List<AuthOrgRole> authOrgRoles) throws Exception{
		Map params = new HashMap();
		params.put("authOrgRoles", authOrgRoles);
		return baseDao.insert(getStatement("batchInsert"), params);
	}
	
	//更新
	public int update(AuthOrgRole authOrgRole) throws Exception {
		return baseDao.update(getStatement("update"), authOrgRole);
	}
	
	//根据ID删除
	public int deleteById(java.lang.Integer id) throws Exception {
		return baseDao.delete(getStatement("delete"), id);
	}
	
	//根据ID批量删除
	public int batchDeleteByIds(List<java.lang.Integer> ids) throws Exception {
		return baseDao.delete(getStatement("batchDelete"), ids);
	}
	
	//根据ID查询
	public AuthOrgRole getById(java.lang.Integer id) throws Exception {
		return baseDao.selectOne(getStatement("getById"), id);
	}
	
	//分页查询
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception {
		return basePageQuery(statementName, pageRequest, countQuery);
	} 
	
	//通过机构主键删除ENUM表信息
	public int deleteByOrgId(String orgid) throws Exception{
		Map params = new HashMap();
		params.put("orgid", orgid);
		return baseDao.delete(getStatement("deleteByOrgId"), params);
	}
	/**
	 * 全部查询
	 
	public List<AuthOrgRole> queryAll() throws Exception {
		return baseDao.queryAll();
	}*/

	@Override
	public void deleteByEnum(Map<String, Object> map) throws Exception{
		baseDao.delete(getStatement("deleteByRoleId"), map);;
	}
}
