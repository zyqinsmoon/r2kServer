package com.apabi.r2k.security.service;

import java.util.List;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.model.AuthOrg;

public interface AuthOrgService {

	public Page findByPageRequest(PageRequest pr) throws Exception;

	public List<AuthOrg> findAllChildOrgs(java.lang.Long orgId)
			throws Exception;

	public Boolean isOrgNameExist(String orgName) throws Exception;

	public AuthOrg get(Long orgId) throws Exception;

	public void removeById(Long orgId) throws Exception;
	public List<AuthOrg> getRolesByOrg(Long orgId) throws Exception;
	//返回默认用户密码
	public String saveOrg(AuthOrg org, List<String> enumCodes) throws Exception;
	
	public void updateOrg(AuthOrg org, String[] oldauth, String[] newauth) throws Exception;
	
	public boolean deleteOrg(AuthOrg org) throws Exception;
	
	public AuthOrg getOrgObject(String orgId) throws Exception;
	
	//注册验证：检查orgId在数据库中是否存在，1成功(不存在)，0失败(已存在)
	public String checkOrgId(String orgId) throws Exception;
	//注册验证：检查orgName在数据库中是否存在，1成功(不存在)，0失败(已存在)
	public String checkOrgName(String orgName) throws Exception;
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception;
	//根据id和name模糊查询org
	public List<AuthOrg> fuzzySearchOrg(String name_startsWith) throws Exception;

	//获取所有购买专题的机构列表
	public List<AuthOrg> queryAllTopciOrg() throws Exception;
}
