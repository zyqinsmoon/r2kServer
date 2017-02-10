package com.apabi.r2k.security.dao;

import java.util.Map;

import org.springframework.stereotype.Component;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.model.AuthUser;


@Component
public interface AuthUserDao  {


	public void saveOrUpdate(AuthUser entity)throws Exception ;

	
	public Page findByPageRequest(PageRequest pageRequest)throws Exception;
	public Page findByPageRequestOrgFiltered(PageRequest pageRequest)throws Exception ;
	public AuthUser getByLoginName(String v) throws Exception;
	public Long generateKey()throws Exception;
	public void updateUserState(Integer state, Long uid)throws Exception;
	//根据条件获取user对象
	public AuthUser getUserObject(Map<String, Object> map) throws Exception;
	public AuthUser getById(Long id) throws Exception ;

	public void removeByIds(String[] ids) throws Exception;
	public int delete(Map<String, Object> map) throws Exception;
}
