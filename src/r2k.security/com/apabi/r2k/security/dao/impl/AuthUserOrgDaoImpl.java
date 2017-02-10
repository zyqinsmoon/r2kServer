package com.apabi.r2k.security.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.BaseDaoImpl;
import com.apabi.r2k.security.dao.AuthUserOrgDao;
import com.apabi.r2k.security.model.AuthUserOrg;


@Repository("authUserOrgDao")
public class AuthUserOrgDaoImpl extends BaseDaoImpl<AuthUserOrg, Serializable> implements AuthUserOrgDao {
	public void saveOrUpdate(AuthUserOrg entity) throws Exception {
		if (entity.getId() == null) {
			save(entity);
		} else {
			update(entity);
		}
	}
	
	public Page findByPageRequest(PageRequest pageRequest) throws Exception {
		return basePageQuery("com.apabi.r2k.security.model.AuthUserOrg.pageSelect",pageRequest);
	}
	
}
