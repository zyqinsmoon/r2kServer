package com.apabi.r2k.security.dao.impl;



import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.BaseDaoImpl;
import com.apabi.r2k.security.dao.AuthResDao;
import com.apabi.r2k.security.model.AuthRes;


@Repository("authResDao")
public class AuthResDaoImpl extends BaseDaoImpl<AuthRes, Serializable> implements AuthResDao {

	public void saveOrUpdate(AuthRes entity) throws Exception {
		if (entity.getId() == null) {
			save(entity);
		} else {
			update(entity);
		}
	}
	
	public Page findByPageRequest(PageRequest pageRequest) throws Exception {
		return basePageQuery("com.apabi.r2k.security.model.AuthRes.pageSelect",pageRequest);
	}
	
	//获取全部资想对应的权限
	public List<AuthRes> loadAllAuthResRole() {
		return baseDao.selectList("com.apabi.r2k.security.model.AuthRes.authResRole");
	}
	public List<AuthRes> loadAuthResByRoleIds(String roleIds){
		return baseDao.selectList("com.apabi.r2k.security.model.AuthRes.getAuthResByRoleIds", roleIds);
	}

	public List<AuthRes> loadAuthResByRoleId(Long roleId) {
		// TODO Auto-generated method stub
		return baseDao.selectList("com.apabi.r2k.security.model.AuthRes.getAuthResByRoleId", roleId);
	}

	public Long generateKey() {
		// TODO Auto-generated method stub
		return (Long) baseDao.selectOne("com.apabi.r2k.security.model.AuthRes.generateKey");
	}
	public int save(AuthRes authRes){
		return save(authRes);
	}
	
}
