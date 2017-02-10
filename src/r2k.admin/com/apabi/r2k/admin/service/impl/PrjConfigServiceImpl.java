package com.apabi.r2k.admin.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.PrjConfigDao;
import com.apabi.r2k.admin.model.PrjConfig;
import com.apabi.r2k.admin.service.PrjConfigService;

@Service("prjConfigService")
public class PrjConfigServiceImpl implements PrjConfigService {

	public static final String PAGE_QUERY_STATEMENT = ".pageSelect";
	
	@Resource(name="prjConfigDao")
	private PrjConfigDao prjConfigDao;
	
	public void setPrjConfigDao(PrjConfigDao prjConfigDao){
		this.prjConfigDao = prjConfigDao;
	}
	public PrjConfigDao getPrjConfigDao(){
		return prjConfigDao;
	}
	
	//入库
	@Transactional(propagation = Propagation.REQUIRED)
	public int save(PrjConfig prjConfig) throws Exception {
		return prjConfigDao.save(prjConfig);
	}
	public int save(String orgId, String deviceType, String deviceId, String configKey, String value, String enable) throws Exception{
		PrjConfig prjConfig = new PrjConfig(orgId, deviceType, deviceId, configKey, value, enable);
		return save(prjConfig);
	}
	@Override
	public int save(String orgId, String deviceType, String deviceId, String configKey, String value, String enable, 
			 Date crtDate, Date lastDate, String configName, String remark) throws Exception {
		PrjConfig prjConfig = new PrjConfig(orgId, deviceType, deviceId, configKey, value, enable, crtDate, lastDate, configName, remark);
		return save(prjConfig);
	}
	//批量入库
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean batchSave(List<PrjConfig> configlist) throws Exception {
		boolean flag = true;
		List<PrjConfig> failList = prjConfigDao.batchSaveConfig(configlist);
		if (failList != null && failList.size() > 0) {
			flag = false;
		}
		return flag;
	}
	
	//更新
	@Transactional(propagation = Propagation.REQUIRED)
	public int update(PrjConfig prjConfig) throws Exception {
		return prjConfigDao.update(prjConfig);
	}
	//批量更新
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean batchUpdate(List<PrjConfig> configlist) throws Exception {
		boolean flag = true;
		List<PrjConfig> failList = prjConfigDao.batchUpdateConfig(configlist);
		if (failList != null && failList.size() > 0) {
			flag = false;
		}
		return flag;
	} 
	//根据ID删除
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteById(java.lang.Integer id) throws Exception {
		return prjConfigDao.deleteById(id);
	}
	
	//批量删除
	@Transactional(propagation = Propagation.REQUIRED)
	public int batchDelete(List<java.lang.Integer> ids) throws Exception {
		return prjConfigDao.batchDelete(ids);
	}
	//通过设备id删除设备配置信息
	public int deleteByDevId(String orgId, String devId) throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		params.put("orgId", orgId);
		params.put("deviceId", devId);
		return prjConfigDao.deleteByDevId(params);
	}
	
	//根据ID查询
	public PrjConfig getById(java.lang.Integer id) throws Exception {
		return prjConfigDao.getById(id);
	}
	
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception {
		return prjConfigDao.pageQuery(PAGE_QUERY_STATEMENT, pageRequest, countQuery);
	}

	//根据orgId查询
	public PrjConfig getPrjConfigByDevType(String orgId, String deviceType, String configKey, String enable) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("orgId", orgId);
		params.put("deviceType", deviceType);
		params.put("configKey", configKey);
		params.put("enable", enable);
		return prjConfigDao.getPrjConfig(params);
	}
	//根据deviceId查询
	public PrjConfig getPrjConfigByDevId(String orgId, String deviceType, String deviceId, String configKey, String enable) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("orgId", orgId);
		params.put("deviceType", deviceType);
		params.put("deviceId", deviceId);
		params.put("configKey", configKey);
		params.put("enable", enable);
		return prjConfigDao.getPrjConfig(params);
	} 
	//通过orgId、deviceType、enable和List<configKey>获取列表
	public List<PrjConfig> getPrjConfigListByDevType(String orgId, String deviceType, String enable, List<String> configKeys) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgId", orgId);
		params.put("deviceType", deviceType);
		params.put("enable", enable);
		params.put("configKeys", configKeys);
		return prjConfigDao.getPrjConfigList(params);
	}
	//通过orgId、deviceType、deviceId、enable和List<configKey>获取列表
	public List<PrjConfig> getPrjConfigListByDevId(String orgId, String deviceType, String deviceId, String enable, List<String> configKeys) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgId", orgId);
		params.put("deviceType", deviceType);
		params.put("deviceId", deviceId);
		params.put("enable", enable);
		params.put("configKeys", configKeys);
		return prjConfigDao.getPrjConfigList(params);
	}
	//通过orgId、deviceType、enable和List<configKey>获取map
	public Map<String, PrjConfig> getPrjConfigMapByDevType(String orgId, String deviceType, String enable, List<String> configKeys) throws Exception{
		Map<String, PrjConfig> configmap = new HashMap<String, PrjConfig>();
		List<PrjConfig> configlist = getPrjConfigListByDevType(orgId, deviceType, enable, configKeys);
		for (PrjConfig prjConfig : configlist) {
			configmap.put(prjConfig.getConfigKey(), prjConfig);
		}
		return configmap;
	}
	//通过orgId、deviceType、deviceId、enable和List<configKey>获取map
	public Map<String, PrjConfig> getPrjConfigMapByDevId(String orgId, String deviceType, String deviceId, String enable, List<String> configKeys) throws Exception{
		Map<String, PrjConfig> configmap = new HashMap<String, PrjConfig>();
		List<PrjConfig> configlist = getPrjConfigListByDevId(orgId, deviceType, deviceId, enable, configKeys);
		for (PrjConfig prjConfig : configlist) {
			configmap.put(prjConfig.getConfigKey(), prjConfig);
		}
		return configmap;
	}
	//通过deviceType、enable和List<configKey>获取list
	public List<PrjConfig> getAllPrjConfigList(String deviceType, List<String> configKeys) throws Exception {
		return getAllPrjConfigList(deviceType, null, configKeys);
	}
	//通过deviceType、enable和List<configKey>获取list
	public List<PrjConfig> getAllPrjConfigList(String deviceType, String enable, List<String> configKeys) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceType", deviceType);
		params.put("enable", enable);
		params.put("configKeys", configKeys);
		return prjConfigDao.getAllPrjConfigList(params);
	}
	//通过deviceType、enable和List<configKey>获取map
	public Map<String, PrjConfig> getAllPrjConfigMap(String deviceType, String enable, List<String> configKeys) throws Exception {
		Map<String, PrjConfig> configmap = new HashMap<String, PrjConfig>();
		List<PrjConfig> configlist = getAllPrjConfigList(deviceType, enable, configKeys);
		for (PrjConfig prjConfig : configlist) {
			configmap.put(prjConfig.getConfigKey(), prjConfig);
		}
		return configmap;
	}
}
