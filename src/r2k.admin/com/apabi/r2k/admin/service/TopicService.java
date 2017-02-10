package com.apabi.r2k.admin.service;

import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.Article;
import com.apabi.r2k.admin.model.Topic;
import com.apabi.r2k.admin.model.TopicAuth;

public interface TopicService {
	/**
	 * 保存专题
	 * @param topic
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> saveTopic(Topic topic) throws Exception;
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception;
	
	public Page<?> getContent(PageRequest<?> pageRequest) throws Exception;
	
	public Map putTopicContent(String id) throws Exception;
	
	public Page<?> checkBeforeCreate(PageRequest<?> pageRequest) throws Exception;
	
	public Map updateContent(List<Article> articles) throws Exception;
	/**
	 * 获取全部专题信息
	 * @return 
	 */
	public Page<?> getAllTopics(PageRequest<?> pageRequest) throws Exception;
	
	/**
	 * 批量专题授权
	 * @param topicAuth 	授权专题公用信息
	 * @param topicAuthIds	专题id数组
	 * @return
	 * @throws Exception
	 */
	public int saveTopicAuthBatch(TopicAuth topicAuth, String[] topicAuthIds) throws Exception;
	/**
	 * 通过id获取专题
	 * @param id
	 */
	public Topic getTopicById(String id) throws Exception;
	
	public Map updateTopic(Topic topic) throws Exception;
	
	public Map deleteTopics(List<Topic> topics) throws Exception;
	
	public Map deleteTopicContent(List<Article> articles) throws Exception;

	/**
	 * 分页查询专题授权列表
	 * @param pageRequest
	 * @param countQuery
	 * @return
	 * @throws Exception
	 */
	public Page<?> getTopicAuthList(PageRequest<?> pageRequest, String countQuery) throws Exception;
	
	/**
	 * 更新专题授权列表
	 * @param topicAuth
	 */
	public boolean updateTopicAuth(TopicAuth topicAuth) throws Exception;
	
	/**
	 * 将所有普通专题的权限分配给该机构(如果该机构购买了专题)
	 * @param orgId
	 * @throws Exception
	 */
	public Map updateTopicAuthByNewOrg(String orgId) throws Exception;
		
	/**
	 * 将新建的普通专题的权限分配给所有购买了专题的机构
	 * @param topic
	 * @throws Exception
	 */
	public Map updateTopicAuthByNewTopic(Topic topic) throws Exception;
	
	//通过机构orgId删除普通专题授权
	public void deleteTopicAuthByOrgId(String orgId) throws Exception;
	
	//根据专题id删除专题授权
	public void deleteTopicAuthByTopicIds(String[] topicIds) throws Exception;
	
	//查询所有过期专题,并通知solr
	public void updateTopicExpire(String currentDateStr);
	//更新所有即将生效专题,并通知solr
	public void updateUnauthTopic(String currentDateStr);
	
}
