package com.apabi.r2k.admin.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.PrjConfigDao;
import com.apabi.r2k.admin.model.PrjConfig;
import com.apabi.r2k.common.base.BaseDaoImpl;

@Repository("prjConfigDao")
public class PrjConfigDaoImpl extends BaseDaoImpl<PrjConfig, Serializable> implements PrjConfigDao {

	//入库
	public int save(PrjConfig prjConfig) throws Exception {
		return baseDao.insert(getStatement("insert"), prjConfig);
	}
	//批量保存
	public List<PrjConfig> batchSaveConfig(List<PrjConfig> configlist) throws Exception {
		return batchSave(getStatement("insert"), configlist);
	}
	//更新
	public int update(PrjConfig prjConfig) throws Exception {
		return baseDao.update(getStatement("update"),prjConfig);
	}
	//批量更新
	public List<PrjConfig> batchUpdateConfig(List<PrjConfig> configlist) throws Exception {
		return batchUpdate(getStatement("update"), configlist);
	}
	
	//根据ID删除
	public int deleteById(java.lang.Integer id) throws Exception {
		return baseDao.delete(getStatement("delete"), id);
	}
	
	//根据ID批量删除
	public int batchDelete(List<java.lang.Integer> ids) throws Exception {
		return baseDao.delete(getStatement("batchDelete"),ids);
	}
	//通过设备id删除设备配置信息
	public int deleteByDevId(Map<String, String> params) throws Exception {
		return baseDao.delete(getStatement("deleteByDevId"), params);
	}
	
	//根据ID查询
	public PrjConfig getById(java.lang.Integer id) throws Exception {
		return baseDao.selectOne(getStatement("getById"), id);
	}
	
	//分页查询
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception {
		return basePageQuery(statementName, pageRequest, countQuery);
	} 
	//获取PrjConfig对象
	public PrjConfig getPrjConfig(Map<String, String> params) throws Exception{
		return baseDao.selectOne(getStatement("getPrjConfig"), params);
	}
	//通过orgId、deviceType、deviceId和List<configKey>获取列表
	public List<PrjConfig> getPrjConfigList(Map<String, Object> params) throws Exception {
		return baseDao.selectList(getStatement("getPrjConfigList"), params);
	}
	//通过deviceType、List<configKey>获取列表
	public List<PrjConfig> getAllPrjConfigList(Map<String, Object> params) throws Exception {
		return baseDao.selectList(getStatement("getAllPrjConfigList"), params);
	}
}
