package com.apabi.r2k.security.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.BaseDaoImpl;
import com.apabi.r2k.security.dao.AuthOrgDao;
import com.apabi.r2k.security.model.AuthOrg;


@Repository("authOrgDao")
public class AuthOrgDaoImpl extends BaseDaoImpl<AuthOrg , Serializable> implements AuthOrgDao {
	
	public Page findByPageRequest(PageRequest pageRequest) throws Exception {
		return basePageQuery("com.apabi.r2k.security.model.AuthOrg.pageSelect",pageRequest);
	}

	public List<AuthOrg> findAllChildOrgs(Long orgId) throws Exception {
		// TODO Auto-generated method stub
		Map map=new HashMap();
		map.put("startWith", orgId);
		return baseDao.selectList(getStatement("pageSelect"),map);
	}

	@Override
	public AuthOrg getAuthOrgById(Long orgId) throws Exception {
		Map map=new HashMap();
		map.put("id", orgId);
		return baseDao.selectOne(getStatement("getById"),map);
	}

	public List<AuthOrg> getRolesByOrg(Long orgId) throws Exception {
		Map map=new HashMap();
		map.put("orgId", orgId);
		return baseDao.selectOne(getStatement("getRolesByOrg"),map);
	}

	//
	public boolean saveOrg(Map<String,Object> map) throws Exception{
		boolean flag = false;
		int num = baseDao.insert(getStatement("insert"), map);
		if(num > 0){
			flag = true;
		}
		return flag;
	}
	
	public boolean updateOrg(Map<String,Object> map) throws Exception{
		boolean flag = false;
		int num = baseDao.update(getStatement("updateByOrgId"), map);
		if(num > 0){
			flag = true;
		}
		return flag;
	}

	public boolean deleteOrg(Map<String,Object> map) throws Exception{
		boolean flag = false;
		int num = baseDao.delete(getStatement("deleteByOrgId"), map);
		if(num > 0){
			flag = true;
		}
		return flag;
	}
	
	public AuthOrg getOrgById(Map<String,Object> map) throws Exception{
		return baseDao.selectOne(getStatement("getOrgById"), map);
	}
	
	public AuthOrg getOrgByName(Map<String,Object> map) throws Exception{
		return baseDao.selectOne(getStatement("getOrgByName"), map);
	}
	
	//分页查询
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception {
		return basePageQuery(getStatement(statementName), pageRequest);
	}
	
	//模糊查询
	public List<AuthOrg> fuzzySearchOrg(Map<String,Object> map) throws Exception{
		return baseDao.selectList(getStatement("fuzzySearchOrg"), map);
	}

	//获取所有购买专题的机构列表
	public List<AuthOrg> getAllBuyTopicOrgList() throws Exception {
		return baseDao.selectList(getStatement("allOrgList"));
	}
	
}
