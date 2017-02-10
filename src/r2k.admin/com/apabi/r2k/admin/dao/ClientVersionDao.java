package com.apabi.r2k.admin.dao;

import java.util.List;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.ClientVersion;

public interface ClientVersionDao {
	
	/**
	 * 入库
	 */
	public void saveVersion(ClientVersion clientVersion) throws Exception;
	
	/**
	 * 更新
	 */
	public void updateVersion(ClientVersion clientVersion) throws Exception;
	
	/**
	 * 根据ID查询
	 */
	public ClientVersion getVersionById(int id) throws Exception;
	
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(PageRequest<?> pageRequest) throws Exception;
	//通过版本号和设备类型查询
	public ClientVersion getClientVersion(String version, String devType) throws Exception;
	//获取最新版本
	public ClientVersion getLatestVersion(String devType) throws Exception;
	
	public void deleteById(int id) throws Exception;
	//返回所有类型最新版本信息
	public List<ClientVersion> getAllTypeLatestVersion() throws Exception;
}
