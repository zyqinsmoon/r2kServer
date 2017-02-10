package com.apabi.r2k.admin.dao;

import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.PrjConfig;

public interface PrjConfigDao {
	
	/**
	 * 入库
	 */
	public int save(PrjConfig prjConfig) throws Exception;
	/**
	 * 批量保存
	 */
	public List<PrjConfig> batchSaveConfig(List<PrjConfig> configlist) throws Exception;
	/**
	 * 更新
	 */
	public int update(PrjConfig prjConfig) throws Exception;
	/**
	 * 批量更新
	 */
	public List<PrjConfig> batchUpdateConfig(List<PrjConfig> configlist) throws Exception;
	
	/**
	 * 根据ID删除
	 */
	public int deleteById(java.lang.Integer id) throws Exception;
	
	/**
	 * 根据ID批量删除
	 */
	public int batchDelete(List<java.lang.Integer> ids) throws Exception;
	
	/**
	 * 通过设备id删除设备配置信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int deleteByDevId(Map<String, String> params) throws Exception;
	/**
	 * 根据ID查询
	 */
	public PrjConfig getById(java.lang.Integer id) throws Exception;
	
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception;
	/**
	 * 获取PrjConfig对象
	 */
	public PrjConfig getPrjConfig(Map<String, String> params) throws Exception;
	/**
	 * 通过orgId、deviceType、deviceId和List<configKey>获取列表
	 */
	public List<PrjConfig> getPrjConfigList(Map<String, Object> params) throws Exception;
	/**
	 * 通过deviceType、List<configKey>获取列表
	 */
	public List<PrjConfig> getAllPrjConfigList(Map<String, Object> params) throws Exception;
}
