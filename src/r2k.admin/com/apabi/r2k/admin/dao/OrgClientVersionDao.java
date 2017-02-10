package com.apabi.r2k.admin.dao;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.OrgClientVersion;

public interface OrgClientVersionDao {
	
	/**
	 * 入库
	 */
	public void saveVersion(OrgClientVersion orgClientVersion) throws Exception;
	
	/**
	 * 更新
	 */
	public void updateVersion(OrgClientVersion orgClientVersion) throws Exception;
	
	/**
	 * 根据ID查询
	 */
	public OrgClientVersion getVersionById(int id) throws Exception;
	
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(PageRequest<?> pageRequest) throws Exception;
	//低版本的机构类表
	public Page<?> downVersionPage(PageRequest<?> pageRequest) throws Exception;
	//高版本的机构类表
	public Page<?> upVersionPage(PageRequest<?> pageRequest) throws Exception;
	
	public OrgClientVersion getOrgClientVersion(String orgId,String devType) throws Exception;
	
	public void deleteByVersion(String version,String devType) throws Exception;
	
}
