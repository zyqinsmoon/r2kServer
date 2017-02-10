package com.apabi.r2k.security.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.BaseDaoImpl;
import com.apabi.r2k.security.model.AuthResRole;


@Repository("authResRoleDao")
public class AuthResRoleDaoImpl extends BaseDaoImpl<AuthResRole, Serializable> implements com.apabi.r2k.security.dao.AuthResRoleDao {

	public void saveOrUpdate(AuthResRole entity)throws Exception {
		if (entity.getId() == null) {
			save(entity);
		} else {
			update(entity);
		}
	}
	
	public Page findByPageRequest(PageRequest pageRequest)throws Exception {
		return basePageQuery("com.apabi.r2k.security.model.AuthResRole.pageSelect",pageRequest);
	}
	public void removeByRoleId(Long roleId){
		Map map=new HashMap();
		map.put("roleId",roleId);
		baseDao.delete("com.apabi.r2k.security.model.AuthResRole.delete",map);
	}

	public List<AuthResRole> getByRoleId(Long roleId) {
		Map param=new HashMap();
		param.put("roleId",roleId);
		return baseDao.selectList("com.apabi.r2k.security.model.AuthResRole.getForList",param);
	}

	@Override
	public List<AuthResRole> findBy(String string, Long resId) {
		
		return findBy(string,resId);
	}

	@Override
	public void deleteBy(String string, Long id) {
		deleteBy(string, id);
		
	}
	
	
}
