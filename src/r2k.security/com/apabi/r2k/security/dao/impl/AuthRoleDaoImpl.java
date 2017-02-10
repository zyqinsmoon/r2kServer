package com.apabi.r2k.security.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.BaseDaoImpl;
import com.apabi.r2k.security.dao.AuthRoleDao;
import com.apabi.r2k.security.model.AuthRole;


@Repository("authRoleDao")
public class AuthRoleDaoImpl extends BaseDaoImpl<AuthRole, Long> implements AuthRoleDao {
	
	public void saveOrUpdate(AuthRole entity) throws Exception{
		if (entity.getId() == null) {
			save(entity);
		} else {
			update(entity);
		}
	}
	
	public Page findByPageRequest(PageRequest pageRequest) throws Exception{
		return basePageQuery("com.apabi.r2k.security.model.AuthRole.pageSelect",pageRequest);
	}
	public Page findByPageRequestOrgFiltered(PageRequest pageRequest) throws Exception {
		return basePageQuery("com.apabi.r2k.security.model.AuthRole.pageSelectOrgFiltered",pageRequest,"com.apabi.r2k.security.model.AuthRole.filteredCount");
	}
	public List<AuthRole> getRolesByUserId(Long userId) {
		// TODO Auto-generated method stub
		Map m=new HashMap();
		m.put("userId", userId);
		return baseDao.selectList("com.apabi.r2k.security.model.AuthRole.getRolesByUserId", m);
	}

	public List<AuthRole> getRolesByOrgId(Long orgId) {
		// TODO Auto-generated method stub
		return baseDao.selectList("com.apabi.r2k.security.model.AuthRole.getRolesByOrgId",orgId);
		
	}
	public java.lang.Long generatorKey(){
		return (Long)baseDao.selectOne("com.apabi.r2k.security.model.AuthRole.generateKey");
	}

	@Override
	public List<AuthRole> queryAllByEnum(List<String> enumCodes) throws Exception{
		Map m=new HashMap();
		m.put("enumCodes", enumCodes);
		return baseDao.selectList(getStatement("queryAllByEnum"), m);
	}

	@Override
	public List<AuthRole> getRolesByOrg(String orgId) throws Exception {
		Map m=new HashMap();
		m.put("orgId", orgId);
		return baseDao.selectList("com.apabi.r2k.security.model.AuthRole.getRolesByOrg", m);
	}
	
	public List<AuthRole> getRoles(String orgId,String deviceType,int type) throws Exception {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("orgId", orgId);
		map.put("deviceType", deviceType);
		map.put("type", type);
		return baseDao.selectList(getStatement("getRolesByOrg"), map);
	}
	
	public AuthRole getAuthRole(long id) throws Exception{
		return getById(id);
	}

	//通过menutype选择角色
	public List<AuthRole> queryByEnum(Map<String,Object> map) throws Exception {
		return baseDao.selectList("com.apabi.r2k.security.model.AuthRole.getRolesByMenuType", map);
	}
	
	
}
