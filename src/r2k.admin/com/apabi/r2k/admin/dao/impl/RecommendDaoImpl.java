package com.apabi.r2k.admin.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.apabi.r2k.admin.dao.RecommendDao;
import com.apabi.r2k.admin.model.Recommend;
import com.apabi.r2k.common.base.BaseDaoImpl;

@Repository("recommendDao")
public class RecommendDaoImpl extends BaseDaoImpl<Recommend, Serializable>implements RecommendDao {

	private Logger log = LoggerFactory.getLogger(RecommendDaoImpl.class);
	//保存专题推荐信息
	public boolean saveRecommend(Recommend or) throws Exception {
		boolean flag = false;
		int	num = baseDao.insert(getStatement("insert"), or);
		if(num > 0){
			flag = true;
		}
		return flag;
	}

	//更新专题推荐信息
	public boolean updateRecommend(Recommend or) throws Exception {
		boolean flag = false;
		int	num = baseDao.update(getStatement("updateByRecommendId"), or);
		if(num > 0){
			flag = true;
		}
		return flag;
	}

	//删除推荐信息
	public boolean deleteRecommend(String resId, String orgId) throws Exception {
		boolean flag = false;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resId", resId);
		map.put("orgId", orgId);
		int	num = baseDao.delete(getStatement("deleteByRecommendId"), map);
		if(num > 0){
			flag = true;
		}
		return flag;
	}
	//获取推荐信息
	public Recommend getRecommend(String resId, String orgId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resId", resId);
		map.put("orgId", orgId);
		return baseDao.selectOne(getStatement("getRecommend"), map);
	}
	//获取推荐列表
	public List<Recommend> getRecommendList() throws Exception {
		return baseDao.selectList(getStatement("getRecommendList"));
	}
	//通过orgId和推荐类型删除推荐信息
	public int deleteRecommendByOrgId(String orgId, String type) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", orgId);
		map.put("type", type);
		return  baseDao.delete(getStatement("deleteByOrgId"), map);
	}
}
