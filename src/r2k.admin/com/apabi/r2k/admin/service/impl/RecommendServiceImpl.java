package com.apabi.r2k.admin.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.apabi.r2k.admin.dao.PaperAuthDao;
import com.apabi.r2k.admin.dao.RecommendDao;
import com.apabi.r2k.admin.dao.TopicAuthDao;
import com.apabi.r2k.admin.model.PaperAuth;
import com.apabi.r2k.admin.model.Recommend;
import com.apabi.r2k.admin.model.TopicAuth;
import com.apabi.r2k.admin.service.RecommendService;
import com.apabi.r2k.common.base.ServerModelTrandsform;
import com.apabi.r2k.common.solr.SolrUtil;
import com.apabi.r2k.common.utils.GlobalConstant;

@Service("recommendService")
public class RecommendServiceImpl implements RecommendService {

	@Resource(name="recommendDao")
	private RecommendDao recommendDao;
	@Resource(name="topicAuthDao")
	private TopicAuthDao topicAuthDao;
	@Resource(name="paperAuthDao")
	private PaperAuthDao paperAuthDao;
	
	private Logger log = LoggerFactory.getLogger(RecommendServiceImpl.class);
	
	
	//保存专题推荐信息
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean saveRecommendByTopicId(Recommend or) throws Exception {
		return this.recommendDao.saveRecommend(or);
	}
	//保存专题推荐信息
	@Transactional(propagation=Propagation.REQUIRED)
	public Map saveSolrRecommendByTopicId(Recommend or) throws Exception {
		TopicAuth topicAuth = this.topicAuthDao.getTopicAuthByTopicId(or.getOrgId(), or.getResId());
		topicAuth.setPosition(or.getSort());
		List<TopicAuth> talist = new ArrayList<TopicAuth>();
		talist.add(topicAuth);
		Map results = null;
		if(topicAuth != null){
			results = this.createTopicFilterInfo(GlobalConstant.URL_FILTER_TOPIC_CREATE, talist);
		}
		return results;
	}
	//更新专题推荐信息
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean updateRecommendByTopicId(Recommend or) throws Exception {
		return this.recommendDao.updateRecommend(or);
	}
	//更新专题推荐信息
	@Transactional(propagation=Propagation.REQUIRED)
	public Map updateSolrRecommendByTopicId(Recommend or) throws Exception {
		TopicAuth topicAuth = this.topicAuthDao.getTopicAuthByTopicId(or.getOrgId(), or.getResId());
		topicAuth.setPosition(or.getSort());
		List<TopicAuth> talist = new ArrayList<TopicAuth>();
		talist.add(topicAuth);
		Map results = null;
		if(topicAuth != null){
			results = this.createTopicFilterInfo(GlobalConstant.URL_FILTER_TOPIC_UPDATE, talist);
		}
		return results;
	}
	
	
	//保存报纸推荐信息
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean saveRecommendByPaperId(Recommend or) throws Exception {
		return this.recommendDao.saveRecommend(or);
	}
	//保存报纸推荐信息
	@Transactional(propagation=Propagation.REQUIRED)
	public Map saveSolrRecommendByPaperId(Recommend or) throws Exception {
		PaperAuth paperAuth = this.paperAuthDao.getPaperAuthObject(or.getOrgId(), or.getResId());
		paperAuth.setPosition(or.getSort());
		List<PaperAuth> palist = new ArrayList<PaperAuth>();
		palist.add(paperAuth);
		Map results = null;
		if(paperAuth != null){
			results = this.createPaperFilterInfo(GlobalConstant.URL_FILTER_PAPER_CREATE, palist);
		}
		return results;
	}
	//更新报纸推荐信息
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean updateRecommendByPaperId(Recommend or) throws Exception {
		return this.recommendDao.updateRecommend(or);
	}
	//更新报纸推荐信息
	public Map updateSolrRecommendByPaperId(Recommend or) throws Exception {
		PaperAuth paperAuth = this.paperAuthDao.getPaperAuthObject(or.getOrgId(), or.getResId());
		paperAuth.setPosition(or.getSort());
		List<PaperAuth> palist = new ArrayList<PaperAuth>();
		palist.add(paperAuth);
		Map results = null;
		if(paperAuth != null){
			results = this.createPaperFilterInfo(GlobalConstant.URL_FILTER_PAPER_UPDATE, palist);
		}
		return results;
	}

	//删除专题推荐信息
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean deleteRecommendByTopicId(String topicId, String orgId) throws Exception {
		return this.recommendDao.deleteRecommend(topicId, orgId);
	}
	//删除专题推荐信息
	public Map deleteSolrRecommendByTopicId(String topicId, String orgId) throws Exception {
		TopicAuth topicAuth = this.topicAuthDao.getTopicAuthByTopicId(orgId, topicId);
		topicAuth.setPosition(Recommend.RECOMMEND_DEFAULT_SORT);
		List<TopicAuth> talist = new ArrayList<TopicAuth>();
		talist.add(topicAuth);
		Map results = null;
		if(topicAuth != null){
			results = this.createTopicFilterInfo(GlobalConstant.URL_FILTER_TOPIC_DELETE, talist);
		}
		return results;
	}
	//删除报纸推荐信息
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean deleteRecommendByPaperId(String paperId, String orgId) throws Exception {
		return this.recommendDao.deleteRecommend(paperId, orgId);
	}
	//删除报纸推荐信息
	public Map deleteSolrRecommendByPaperId(String paperId, String orgId) throws Exception {
		PaperAuth paperAuth = this.paperAuthDao.getPaperAuthObject(orgId, paperId);
		paperAuth.setPosition(Recommend.RECOMMEND_DEFAULT_SORT);
		List<PaperAuth> talist = new ArrayList<PaperAuth>();
		talist.add(paperAuth);
		Map results = null;
		if(paperAuth != null){
			results = this.createPaperFilterInfo(GlobalConstant.URL_FILTER_PAPER_DELETE, talist);
		}
		return results;
	}

	//获取推荐信息
	public Recommend getRecommend(String resId, String orgId) throws Exception {
		return this.recommendDao.getRecommend(resId, orgId);
	}
	
	//向solr传递推荐列表信息创建专题filter
	public Map createTopicFilterInfo(String url, TopicAuth ta) throws Exception {
		String xml = ServerModelTrandsform.objectToXml(ta);
		InputStream in = SolrUtil.sendSolrRequest(url, xml, GlobalConstant.HTTP_TYPE_POST, GlobalConstant.HTTP_DATA_TYPE_XML);
		return SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
	}
	//向solr传递推荐列表信息创建专题filter
	public Map createTopicFilterInfo(String url, List<TopicAuth> talist) throws Exception {
		String xml = ServerModelTrandsform.objectToXml(talist);
		InputStream in = SolrUtil.sendSolrRequest(url, xml, GlobalConstant.HTTP_TYPE_POST, GlobalConstant.HTTP_DATA_TYPE_XML);
		return SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
	}
	//向solr传递推荐列表信息创建报纸filter
	public Map createPaperFilterInfo(String url, List<PaperAuth> talist) throws Exception {
		String xml = ServerModelTrandsform.objectToXml(talist);
		InputStream in = SolrUtil.sendSolrRequest(url, xml, GlobalConstant.HTTP_TYPE_POST, GlobalConstant.HTTP_DATA_TYPE_XML);
		return SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
	}

	//推荐顺序修改
	public synchronized void saveOrgUpdateRecommend(String resId, String orgId, String type, int sort) throws Exception{
		Recommend rec = this.getRecommend(resId, orgId);
		Date now = new Date();
		if(rec == null){
			Recommend recommend = new Recommend();
			recommend.setResId(resId);
			recommend.setOrgId(orgId);
			recommend.setCrtDate(now);
			recommend.setLastDate(now);
			recommend.setSort(sort);
			recommend.setType(type);
			this.saveRecommendByTopicId(recommend);
		}else{
			rec.setSort(sort);
			rec.setLastDate(now);
			rec.setType(type);
			this.updateRecommendByTopicId(rec);
		}
	}
	
	//查询授权专题对象
	public TopicAuth getTopicAuthByTopicId(String orgId, String topicId) throws Exception {
		return this.topicAuthDao.getTopicAuthByTopicId(orgId, topicId);
	}
	public RecommendDao getRecommendDao() {
		return recommendDao;
	}
	public void setRecommendDao(RecommendDao recommendDao) {
		this.recommendDao = recommendDao;
	}
	public TopicAuthDao getTopicAuthDao() {
		return topicAuthDao;
	}
	public void setTopicAuthDao(TopicAuthDao topicAuthDao) {
		this.topicAuthDao = topicAuthDao;
	}
	public PaperAuthDao getPaperAuthDao() {
		return paperAuthDao;
	}
	public void setPaperAuthDao(PaperAuthDao paperAuthDao) {
		this.paperAuthDao = paperAuthDao;
	}
}
