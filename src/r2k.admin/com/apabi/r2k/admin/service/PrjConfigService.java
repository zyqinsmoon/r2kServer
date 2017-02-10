package com.apabi.r2k.admin.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.PrjConfig;

public interface PrjConfigService {

	/**
	 * 入库
	 */
	public int save(String orgId, String deviceType, String deviceId, String configKey, String value, String enable) throws Exception;
	public int save(String orgId, String deviceType, String deviceId, String configKey, String value, String enable, Date crtDate, Date lastDate, String configName, String remark) throws Exception;
	public int save(PrjConfig prjConfig) throws Exception;
	/**
	 * 批量入库
	 */
	public boolean batchSave(List<PrjConfig> configlist) throws Exception;
	/**
	 * 更新
	 */
	public int update(PrjConfig prjConfig) throws Exception;
	/**
	 * 批量更新
	 */
	public boolean batchUpdate(List<PrjConfig> configlist) throws Exception;
	
	/**
	 * 根据ID删除
	 */
	public int deleteById(java.lang.Integer id) throws Exception;
	
	/**
	 * 批量删除
	 */
	public int batchDelete(List<java.lang.Integer> ids) throws Exception;
	/**
	 * 通过设备id删除设备配置信息
	 */
	public int deleteByDevId(String orgId, String devId) throws Exception;
	
	/**
	 * 根据ID查询
	 */
	public PrjConfig getById(java.lang.Integer id) throws Exception;
	/**
	 * 根据orgId查询
	 */
	public PrjConfig getPrjConfigByDevType(String orgId, String deviceType, String configKey, String enable) throws Exception;
	/**
	 * 根据deviceId查询
	 */
	public PrjConfig getPrjConfigByDevId(String orgId, String deviceType, String deviceId, String configKey, String enable) throws Exception;
	/**
	 * 通过orgId、deviceType、enable和List<configKey>获取列表
	 */
	public List<PrjConfig> getPrjConfigListByDevType(String orgId, String deviceType, String enable, List<String> configKeys) throws Exception;
	/**
	 * 通过orgId、deviceType、deviceId、enable和List<configKey>获取列表
	 */
	public List<PrjConfig> getPrjConfigListByDevId(String orgId, String deviceType, String deviceId, String enable, List<String> configKeys) throws Exception;
	/**
	 * 通过orgId、deviceType、enable和List<configKey>获取map
	 */
	public Map<String, PrjConfig> getPrjConfigMapByDevType(String orgId, String deviceType, String enable, List<String> configKeys) throws Exception;
	/**
	 * 通过orgId、deviceType、deviceId、enable和List<configKey>获取map
	 */
	public Map<String, PrjConfig> getPrjConfigMapByDevId(String orgId, String deviceType, String deviceId, String enable, List<String> configKeys) throws Exception;
	/**
	 * 通过deviceType、enable和List<configKey>获取list
	 */
	public List<PrjConfig> getAllPrjConfigList(String deviceType, List<String> configKeys) throws Exception;
	/**
	 * 通过deviceType、enable和List<configKey>获取list
	 */
	public List<PrjConfig> getAllPrjConfigList(String deviceType, String enable, List<String> configKeys) throws Exception;
	/**
	 * 通过deviceType、enable和List<configKey>获取map
	 */
	public Map<String, PrjConfig> getAllPrjConfigMap(String deviceType, String enable, List<String> configKeys) throws Exception;
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception; 
}
