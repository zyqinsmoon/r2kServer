package com.apabi.r2k.security.dao;

import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.model.AuthOrgRole;

public interface AuthOrgRoleDao {
	
	/**
	 * 入库
	 */
	public int save(AuthOrgRole authOrgRole) throws Exception;

	/**
	 * 批量保存
	 * @param authOrgRoles
	 * @return
	 * @throws Exception
	 */
	public int batchSaveOrgRole(List<AuthOrgRole> authOrgRoles) throws Exception; 
	
	/**
	 * 更新
	 */
	public int update(AuthOrgRole authOrgRole) throws Exception;
	
	/**
	 * 根据ID删除
	 */
	public int deleteById(java.lang.Integer id) throws Exception;
	
	/**
	 * 根据ID批量删除
	 */
	public int batchDeleteByIds(List<java.lang.Integer> ids) throws Exception;
	
	/**
	 * 根据ID查询
	 */
	public AuthOrgRole getById(java.lang.Integer id) throws Exception;
	
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception;

	/**
	 * 通过机构主键删除ENUM表信息
	 * @param orgid
	 */
	public int deleteByOrgId(String orgid) throws Exception;

	/**
	 * 通过机构主键和menutype删除角色中间表表信息
	 * @param map
	 * @return
	 */
	public void deleteByEnum(Map<String, Object> map) throws Exception;

	
	/**
	 * 全部查询
	
	public List<AuthOrgRole> queryAll() throws Exception; */
}
