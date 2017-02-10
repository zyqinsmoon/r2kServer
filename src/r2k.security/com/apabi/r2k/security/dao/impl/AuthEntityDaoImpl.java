package com.apabi.r2k.security.dao.impl;



import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.BaseDaoImpl;
import com.apabi.r2k.security.dao.AuthEntityDao;
import com.apabi.r2k.security.model.AuthEntity;


@Repository("authEntityDao")
public class AuthEntityDaoImpl extends BaseDaoImpl<AuthEntity, Serializable> implements AuthEntityDao{
	
	public void saveOrUpdate(AuthEntity entity) throws Exception {
		if (entity.getId() == null) {
			save(entity);
		} else {
			update(entity);
		}
	}
	
	public Page findByPageRequest(PageRequest pageRequest) throws Exception {
		return basePageQuery("com.apabi.r2k.security.model.AuthEntity.pageSelect",pageRequest);
	}

	@Override
	public List<AuthEntity> getAuthTree(Map params) throws Exception {
		return baseDao.selectList(getStatement("authTree"),params);
	}
}
