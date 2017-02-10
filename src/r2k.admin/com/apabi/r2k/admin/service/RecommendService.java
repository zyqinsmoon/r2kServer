package com.apabi.r2k.admin.service;

import java.util.Map;

import com.apabi.r2k.admin.model.Recommend;

public interface RecommendService {
	/**
	 * 保存专题推荐信息
	 * @param ta
	 * @throws Exception
	 */
	public boolean saveRecommendByTopicId(Recommend or) throws Exception;
	/**
	 * 保存专题推荐信息
	 * @param ta
	 * @throws Exception
	 */
	public Map saveSolrRecommendByTopicId(Recommend or) throws Exception;
	/**
	 * 更新专题推荐信息
	 * @param topicAuth
	 */
	public boolean updateRecommendByTopicId(Recommend or) throws Exception;
	/**
	 * 更新专题推荐信息
	 * @param topicAuth
	 */
	public Map updateSolrRecommendByTopicId(Recommend or) throws Exception;
	/**
	 * 删除专题推荐信息
	 * @param topicId
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteRecommendByTopicId(String topicId, String orgId)throws Exception;
	/**
	 * 删除专题推荐信息
	 * @param topicId
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public Map deleteSolrRecommendByTopicId(String topicId, String orgId)throws Exception;
	
	/**
	 * 保存报纸推荐信息
	 * @param topicAuth
	 */
	public boolean saveRecommendByPaperId(Recommend or) throws Exception;
	/**
	 * 保存报纸推荐信息
	 * @param topicAuth
	 */
	public Map saveSolrRecommendByPaperId(Recommend or) throws Exception;
	/**
	 * 更新报纸推荐信息
	 * @param topicAuth
	 */
	public boolean updateRecommendByPaperId(Recommend or) throws Exception;
	/**
	 * 更新报纸推荐信息
	 * @param topicAuth
	 */
	public Map updateSolrRecommendByPaperId(Recommend or) throws Exception;
	/**
	 * 删除报纸推荐信息
	 * @param paperId
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteRecommendByPaperId(String paperId, String orgId) throws Exception;
	/**
	 * 删除报纸推荐信息
	 * @param paperId
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public Map deleteSolrRecommendByPaperId(String paperId, String orgId) throws Exception;
	/**
	 * 获取推荐信息
	 * @param resId
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public Recommend getRecommend(String resId, String orgId) throws Exception;
	
	/**
	 * 推荐顺序修改
	 * @param resId
	 * @param orgId 
	 * @param type 资源类型：1报纸，2专题
	 * @sort
	 * @throws Exception
	 */
	public void saveOrgUpdateRecommend(String resId, String orgId, String type, int sort) throws Exception;
}
