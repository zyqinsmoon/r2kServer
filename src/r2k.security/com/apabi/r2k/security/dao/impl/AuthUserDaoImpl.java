package com.apabi.r2k.security.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.BaseDaoImpl;
import com.apabi.r2k.common.security.util.DigestUtils;
import com.apabi.r2k.security.dao.AuthUserDao;
import com.apabi.r2k.security.model.AuthUser;


@Repository("authUserDao")
public class AuthUserDaoImpl extends BaseDaoImpl<AuthUser, Serializable> implements AuthUserDao {

	public void saveOrUpdate(AuthUser entity) throws Exception {
		if (entity.getId() == null) {
			save(entity);
		} else {
			update(entity);
		}
	}

	@Override
	protected void prepareObjectForSaveOrUpdate(final AuthUser authUser){
		String password = authUser.getPassword();
		if (StringUtils.isNotBlank(password)) {
			password = DigestUtils.md5ToHex(password);
			authUser.setPassword(password);
		} else {
			authUser.setPassword(null);
		}
		super.prepareObjectForSaveOrUpdate(authUser);
	}
	
	public Page findByPageRequest(PageRequest pageRequest) throws Exception {
		return basePageQuery(getStatement("pageSelect"),pageRequest);	
	}
	public Page findByPageRequestOrgFiltered(PageRequest pageRequest)throws Exception {
		return basePageQuery("com.apabi.r2k.security.model.AuthUser.pageSelectOrgFiltered",pageRequest,"com.apabi.r2k.security.model.AuthUser.filteredCount");
	}
	
	public AuthUser getByLoginName(String v) {
		return (AuthUser)baseDao.selectOne("com.apabi.r2k.security.model.AuthUser.getByLoginName", v);
	}

	public Long generateKey(){		
		return (Long) baseDao.selectOne("com.apabi.r2k.security.model.AuthUser.generateKey");
	}

	public void updateUserState(Integer state, Long uid) {
		Map param=new HashMap();
		param.put("enabled",state);
		param.put("uid", uid);
		baseDao.update("com.apabi.r2k.security.model.AuthUser.updateUserState", param);
		
	}
	//根据条件获取user对象
	public AuthUser getUserObject(Map<String, Object> map) throws Exception {
		return (AuthUser)baseDao.selectOne(getStatement("getUserObject"),map);
	}
	/*public List<AuthRole> findBy(String roleNameString, String roleName){
		findBy("roleNameString",roleName);
	}*/

	@Override
	public AuthUser getById(Long id) throws Exception {
		return super.getById(id);
	}

	//批量删除用户
	public void removeByIds(String[] ids) throws Exception {
		Map param=new HashMap();
		param.put("ids",ids);
		baseDao.delete(getStatement("batchDelete"), param);
	}

	//删除用户
	public int delete(Map<String, Object> map) throws Exception {
		return baseDao.delete(getStatement("delete"), map);
	}
	
}
