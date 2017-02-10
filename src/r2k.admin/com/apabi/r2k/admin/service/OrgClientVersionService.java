package com.apabi.r2k.admin.service;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.OrgClientVersion;

public interface OrgClientVersionService {

	/**
	 * 入库
	 */
	public void save(OrgClientVersion orgClientVersion) throws Exception;
	
	/**
	 * 更新
	 */
	public void update(OrgClientVersion orgClientVersion) throws Exception;
	
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(PageRequest<?> pageRequest) throws Exception;
	
	public OrgClientVersion getVersionById(int id) throws Exception;
	//获取机构版本为低版本的列表
	public Page<?> downVersionPage(PageRequest<?> pageRequest, String version, String devType) throws Exception;
	//获取机构版本为高版本的列表
	public Page<?> upVersionPage(PageRequest<?> pageRequest, String version, String devType) throws Exception;
	//根据机构ID和设备类型获取信息
	public OrgClientVersion getOrgClientVersion(String orgId, String devType) throws Exception;
	//删除某一版本
	public void deleteByVersion(String version, String devType) throws Exception;
	
}
