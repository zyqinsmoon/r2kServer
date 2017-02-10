package com.apabi.r2k.admin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.TemplateSetDao;
import com.apabi.r2k.admin.model.InfoTemplateSet;
import com.apabi.r2k.common.base.BaseDaoImpl;

@Repository("templateSetDao")
public class TemplateSetDaoImpl extends BaseDaoImpl<InfoTemplateSet, java.io.Serializable> implements TemplateSetDao {

	@Override
	public int saveTemplateSet(InfoTemplateSet templateSet) throws Exception {
		return  baseDao.insert(getStatement("insertSelective"), templateSet);
	}
	public List<InfoTemplateSet> batchSaveTemplateSet(List<InfoTemplateSet> tempsetlist) throws Exception {
		return batchSave(getStatement("insertSelective"), tempsetlist);
	}

	@Override
	public int updateTemplateSet(InfoTemplateSet templateSet) throws Exception {
		return baseDao.update(getStatement("update"), templateSet);
	}
	public List<InfoTemplateSet> batchUpdateTemplateSet(List<InfoTemplateSet> tempsetlist) throws Exception {
		return batchSave(getStatement("insertSelective"), tempsetlist);
	}

	@Override
	public int deleteById(Integer id) throws Exception {
		return baseDao.delete(getStatement("delete"), id);
	}

	@Override
	public InfoTemplateSet getTemplateSetById(Integer id) throws Exception {
		return getById(id);
	}

	@Override
	public InfoTemplateSet getTemplateSetBySetNo(String orgId, String setNo) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("orgId", orgId);
		params.put("setNo", setNo);
		return baseDao.selectOne(getStatement("getBySetNo"), params);
	}
	
	public List<InfoTemplateSet> checkDefaultType(Map<String, Object> params) throws Exception{
		return baseDao.selectList(getStatement("checkDefaultType"), params);
	}
	@Override
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception {
		return basePageQuery(getStatement(statementName), pageRequest);
	}

	@Override
	public List<InfoTemplateSet> queryAll(Map<String, String> params)throws Exception {
		return baseDao.selectList(getStatement("queryAll"), params);
	}
	public List<InfoTemplateSet> getTemplateSetByDevType(Map<String, String> params) throws Exception{
		return baseDao.selectList(getStatement("getTemplateSetByDevType"), params);
	}
}
