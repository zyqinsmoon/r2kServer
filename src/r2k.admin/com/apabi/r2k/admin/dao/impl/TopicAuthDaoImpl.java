package com.apabi.r2k.admin.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.TopicAuthDao;
import com.apabi.r2k.admin.model.TopicAuth;
import com.apabi.r2k.common.base.BaseDaoImpl;
@Repository("topicAuthDao")
public class TopicAuthDaoImpl extends BaseDaoImpl<TopicAuth, Serializable> implements TopicAuthDao {

	private Logger log = LoggerFactory.getLogger(TopicAuthDaoImpl.class);

	//分页查询
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception {
		return basePageQuery(getStatement(statementName), pageRequest);
	}
	//保存专题授权信息
	public void saveTopicAuth(TopicAuth ta) throws Exception{
		baseDao.insert(getStatement("insert"),ta);
	}
	//批量保存专题授权信息
	public int saveTopicAuthBatch(List<TopicAuth> talist) throws Exception{
		return baseDao.insert(getStatement("insertBatch"),talist);
	}
	//获取全部专题授权信息
	public List<TopicAuth> getAllTopicAuth(Map param) throws Exception{
		return baseDao.selectList(getStatement("pageSelect"),param);
	}
	
	//获取相应机构的授权信息
	public Page<?> getAllByOrg(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception{
		Number totalCount = (Number) baseDao.selectOne(getCountQuery(), pageRequest.getFilters());
		if(totalCount == null || totalCount.intValue() == 0){
			return new Page(pageRequest,0);
		}
		Page<?> page = new Page(pageRequest, totalCount.intValue());
		Map<Object, Object> otherFilters = new HashMap<Object, Object>();
		otherFilters.putAll((Map)pageRequest.getFilters());
		List list = baseDao.selectList(getStatement(statementName), otherFilters);
		page.setResult(list);
		return page;
	}
	
	//更新专题授权列表
	public boolean updateTopicAuth(TopicAuth topicAuth) throws Exception{
		boolean flag = false;
		int	num = baseDao.update(getStatement("updateByTopicAuthId"), topicAuth);
		if(num > 0){
			flag = true;
		}
		return flag;
	}
	//查询全部推荐列表
	public List<TopicAuth> getRecommendTopicList(String orgId, String type) throws Exception{
		Map param = new HashMap<String, String>();
		param.put("orgId", orgId);
		param.put("type", type);
		return baseDao.selectList(getStatement("pageSelect"),param);
	}
	//查询授权专题对象
	public TopicAuth getTopicAuthByTopicId(String orgId, String topicId) throws Exception {
		Map param = new HashMap<String, String>();
		param.put("orgId", orgId);
		param.put("topicId", topicId);
		return baseDao.selectOne(getStatement("getTopicAuthByTopicId"),param);
	}
	
	//通过机构orgId删除专题
	public void deleteTopicAuthByOrgId(String orgId) throws Exception{
		this.baseDao.delete(getStatement("deleteTopicAuthByOrgId"),orgId);
	}
	
	//通过机构orgId获取全部专题
	public List<TopicAuth> queryAllTopicAuthByOrgId(Map param) throws Exception{
		return baseDao.selectList(getStatement("queryAllTopicAuthByOrgId"),param);
	}
	@Override
	public int deleteTopicAuthByTopicIds(String[] topicIds) throws Exception {
		Map param = new HashMap<String, String>();
		param.put("topicIds", topicIds);
		return this.baseDao.delete(getStatement("deleteTopicAuthByTopicIds"), param);
	}
	//查询所有过期的专题列表
	public List<TopicAuth> getAllExpireTopicAuth(String currentDateStr) throws Exception {
		Map param = new HashMap<String, String>();
		param.put("endDate", currentDateStr);
		return this.baseDao.selectList(getStatement("queryAllExpireTopicAuth"), param);
	}
	//查询所有即将生效的专题列表
	public List<TopicAuth> getAllUnauthTopicAuth(String currentDateStr) throws Exception {
		Map param = new HashMap<String, String>();
		param.put("startDate", currentDateStr);
		return this.baseDao.selectList(getStatement("queryAllUnauthTopicAuth"), param);
	}
}
