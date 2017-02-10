package com.apabi.r2k.admin.dao;

import java.util.List;

import com.apabi.r2k.admin.model.Recommend;

public interface RecommendDao {
	/**
	 * 保存专题推荐信息
	 * @param ta
	 * @throws Exception
	 */
	public boolean saveRecommend(Recommend or) throws Exception;
	/**
	 * 更新专题推荐信息
	 * @param topicAuth
	 */
	public boolean updateRecommend(Recommend or) throws Exception;
	/**
	 * 删除推荐信息
	 * @param resId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteRecommend(String resId, String orgId) throws Exception;
	/**
	 * 获取推荐信息
	 * @param resId
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public Recommend getRecommend(String resId, String orgId) throws Exception;
	/**
	 * 获取推荐列表
	 * @return
	 * @throws Exception
	 */
	public List<Recommend> getRecommendList() throws Exception;
	/**
	 * 通过orgId和推荐类型删除推荐信息
	 * @param orgId
	 * @param type 推荐类型：1报纸2专题
	 * @return
	 * @throws Exception
	 */
	public int deleteRecommendByOrgId(String orgId, String type) throws Exception;
}
