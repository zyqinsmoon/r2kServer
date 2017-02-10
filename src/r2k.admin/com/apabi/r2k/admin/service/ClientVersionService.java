package com.apabi.r2k.admin.service;

import java.util.List;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.ClientVersion;

public interface ClientVersionService {

	/**
	 * 入库
	 */
	public void save(ClientVersion clientVersion) throws Exception;
	
	/**
	 * 更新
	 */
	public void update(ClientVersion clientVersion) throws Exception;
	
	/**
	 * 根据ID查询
	 */
	public ClientVersion getById(int id) throws Exception;
	
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(PageRequest<?> pageRequest, String devType) throws Exception;
	
	public ClientVersion getClientVersion(String version,String devType) throws Exception;
	//返回最新版本信息
	public ClientVersion getLatestVersion(String devType) throws Exception;
	//返回所有类型最新版本信息
	public List<ClientVersion> getAllTypeLatestVersion() throws Exception;
	//删除某一版本
	public void deleteById(int id) throws Exception;
}
