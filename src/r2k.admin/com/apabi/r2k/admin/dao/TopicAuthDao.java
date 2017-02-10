package com.apabi.r2k.admin.dao;

import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.TopicAuth;

public interface TopicAuthDao {

	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception;
	/**
	 * 保存专题授权信息
	 * @param ta
	 * @throws Exception
	 */
	public void saveTopicAuth(TopicAuth ta) throws Exception;
	/**
	 * 保存专题授权信息
	 * @param talist
	 * @throws Exception
	 */
	public int saveTopicAuthBatch(List<TopicAuth> talist) throws Exception;
	/**
	 * 获取全部专题授权信息
	 * @return
	 * @throws Exception
	 */
	public List<TopicAuth> getAllTopicAuth(Map param) throws Exception;
	
	/**
	 * 获取相应机构的授权信息
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public Page<?> getAllByOrg(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception;
	/**
	 * 更新专题授权列表
	 * @param topicAuth
	 */
	public boolean updateTopicAuth(TopicAuth topicAuth) throws Exception;
	/**
	 * 查询全部推荐列表
	 * @param orgId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<TopicAuth> getRecommendTopicList(String orgId, String type) throws Exception;
	/**
	 * 查询专题授权对象
	 * @param orgId
	 * @param topicId
	 * @return
	 */
	public TopicAuth getTopicAuthByTopicId(String orgId, String topicId) throws Exception;
	/**
	 * 通过机构orgId删除专题
	 * @param orgId
	 */
	public void deleteTopicAuthByOrgId(String orgId) throws Exception;
	/**
	 * 通过机构orgId获取全部专题
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public List<TopicAuth> queryAllTopicAuthByOrgId(Map param) throws Exception;
	
	/**
	 * 根据专题id删除授权
	 */
	public int deleteTopicAuthByTopicIds(String[] topicIds) throws Exception;
	//查询所有过期的专题列表
	public List<TopicAuth> getAllExpireTopicAuth(String currentDateStr) throws Exception ;
	//查询所有即将生效的专题列表
	public List<TopicAuth> getAllUnauthTopicAuth(String currentDateStr) throws Exception ;
}
