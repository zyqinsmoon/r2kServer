package com.apabi.r2k.security.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.model.AuthOrg;


@Component
public interface AuthOrgDao {
	
	public Page findByPageRequest(PageRequest pageRequest)  throws Exception;
	public List<AuthOrg> findAllChildOrgs(Long orgId)  throws Exception;
	
	public AuthOrg getAuthOrgById(Long orgId) throws Exception;
	public List<AuthOrg> getRolesByOrg(Long orgId) throws Exception;
	
	

	public boolean saveOrg(Map<String,Object> map) throws Exception;
	
	public boolean updateOrg(Map<String,Object> map) throws Exception;
	
	public boolean deleteOrg(Map<String,Object> map) throws Exception;
	
	public AuthOrg getOrgById(Map<String,Object> map) throws Exception;
	
	public AuthOrg getOrgByName(Map<String,Object> map) throws Exception;
	
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception; 
	/**
	 * name、id模糊查询
	 */
	public List<AuthOrg> fuzzySearchOrg(Map<String,Object> map) throws Exception;
	/**
	 * 获取所有购买专题的机构列表
	 * @return
	 */
	public List<AuthOrg> getAllBuyTopicOrgList() throws Exception;
}
