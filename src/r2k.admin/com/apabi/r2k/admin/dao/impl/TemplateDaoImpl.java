package com.apabi.r2k.admin.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.TemplateDao;
import com.apabi.r2k.admin.model.InfoTemplate;
import com.apabi.r2k.common.base.BaseDaoImpl;

@Repository("templateDao")
public class TemplateDaoImpl extends BaseDaoImpl<InfoTemplate, Serializable> implements TemplateDao {

	@Override
	public int saveTemplate(InfoTemplate template) throws Exception {
		return baseDao.insert(getStatement("insertSelective"), template);
	}

	@Override
	public List<InfoTemplate> batchSaveTemplate(List<InfoTemplate> templist) throws Exception {
		return batchSave(getStatement("insertSelective"), templist);
	}
	
	@Override
	public int updateTemplate(InfoTemplate template) throws Exception {
		return baseDao.update(getStatement("update"), template);
	}
	
	public List<InfoTemplate> batchUpdateTemplate(List<InfoTemplate> templist) throws Exception {
		return batchUpdate(getStatement("update"), templist);
	}

	@Override
	public int deleteById(Integer id) throws Exception {
		return baseDao.delete(getStatement("delete"), id);
	}
	@Override
	public int deleteBySetNo(String orgId, String setNo) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("orgId", orgId);
		params.put("setNo", setNo);
		return baseDao.delete(getStatement("deleteBySetNo"), params);
	}
	@Override
	public int batchDeleteTemplate(List<java.lang.Integer> ids) throws Exception {
		Map<String, List<java.lang.Integer>> params = new HashMap<String, List<java.lang.Integer>>();
		params.put("ids", ids);
		return baseDao.delete(getStatement("batchDelete"), params);
	}

	@Override
	public InfoTemplate getTemplateById(Integer id) throws Exception {
		return getById(id);
	}

	@Override
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception {
		return basePageQuery(getStatement(statementName), pageRequest);
	}
	//根据模板套id返回模板列表
	public List<InfoTemplate> findTemplateListBySetNo(String orgId, String setNo) throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		params.put("orgId", orgId);
		params.put("setNo", setNo);
		return baseDao.selectList(getStatement("queryAll"), params);
	}

	//根据设备类型查询机构下模板
	public List<InfoTemplate> findOrgTemplateByDevType(String orgId, String deviceType) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("orgId", orgId);
		params.put("deviceType", deviceType);
		return baseDao.selectList(getStatement("findOrgTemplateByDevType"),params);
	}
	//根据设备类型查询机构下模板
	public List<InfoTemplate> findOrgAllTemplateByDevType(String orgId, String deviceType) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("orgId", orgId);
		params.put("deviceType", deviceType);
		return baseDao.selectList(getStatement("findOrgAllTemplateByDevType"),params);
	}

	@Override
	public InfoTemplate findPublishHomeTemplate(Map parameters) throws Exception {
		return baseDao.selectOne(getStatement("findHomeTemplate"), parameters);
	}

	@Override
	public List<InfoTemplate> findBySetNo(String setNo) throws Exception {
		Map params = new HashMap();
		params.put("setNo", setNo);
		return baseDao.selectList(getStatement("queryAll"), params);
	}

	@Override
	public InfoTemplate findUsedTemplate(Map parameters) throws Exception {
		return baseDao.selectOne(getStatement("findUsedTemplate"), parameters);
	}

	@Override
	public List<InfoTemplate> findColAllTemplates(String setNo, String type) throws Exception {
		Map params = new HashMap();
		params.put("setNo", setNo);
		params.put("type", type);
		return baseDao.selectList(getStatement("findColTemplates"), params);
	}

	@Override
	public List<InfoTemplate> findDefaultTemplates(String orgId,
			String deviceType) throws Exception {
		Map params = new HashMap();
		params.put("orgId", orgId);
		params.put("deviceType", deviceType);
		return baseDao.selectList(getStatement("findDefaultTemplate"),params);
	}
}

