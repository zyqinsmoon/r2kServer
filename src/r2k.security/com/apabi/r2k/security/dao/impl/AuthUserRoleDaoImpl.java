package com.apabi.r2k.security.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.BaseDaoImpl;
import com.apabi.r2k.security.dao.AuthUserRoleDao;
import com.apabi.r2k.security.model.AuthUserRole;


@Repository("authUserRoleDao")
public class AuthUserRoleDaoImpl extends BaseDaoImpl<AuthUserRole, Serializable> implements AuthUserRoleDao{
	
	public void saveOrUpdate(AuthUserRole entity) throws Exception {
		if (entity.getId() == null) {
			save(entity);
		} else {
			update(entity);
		}
	}
	
	public Page findByPageRequest(PageRequest pageRequest) throws Exception {
		return basePageQuery("com.apabi.r2k.security.model.AuthUserRole.pageSelect",pageRequest);
	}
	
	
	public List<AuthUserRole> findAuthUserRole(Long userId){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("userId", userId);
		return baseDao.selectList("com.apabi.r2k.security.model.AuthUserRole.pageSelect",paraMap);
	}

	public void removeByUserId(Long userId) throws Exception {
		deleteBy("userId", userId);
	}

	public void removeByRoleId(Long roleId) throws Exception {
		
		deleteBy("roleId", roleId);
	}

	
}
