package com.apabi.r2k.admin.service.impl;

import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.RecommendDao;
import com.apabi.r2k.admin.dao.TopicAuthDao;
import com.apabi.r2k.admin.model.Article;
import com.apabi.r2k.admin.model.Recommend;
import com.apabi.r2k.admin.model.Topic;
import com.apabi.r2k.admin.model.TopicAuth;
import com.apabi.r2k.admin.service.TopicService;
import com.apabi.r2k.common.base.ServerModelTrandsform;
import com.apabi.r2k.common.solr.SolrParam;
import com.apabi.r2k.common.solr.SolrResult;
import com.apabi.r2k.common.solr.SolrUtil;
import com.apabi.r2k.common.utils.DateUtil;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.security.dao.AuthOrgDao;
import com.apabi.r2k.security.model.AuthOrg;
@Service("topicServiceImpl")
public class TopicServiceImpl implements TopicService {
	
	@Resource(name="topicAuthDao")
	private TopicAuthDao topicAuthDao;
	@Resource(name="authOrgDao")
	private AuthOrgDao authOrgDao;
	@Resource(name="recommendDao")
	private RecommendDao recommendDao;

	
	private Logger log = LoggerFactory.getLogger(TopicServiceImpl.class);
	
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception{
		String sort = pageRequest.getSortColumns();
		pageRequest.setSortColumns(null);
		Map param = (Map) pageRequest.getFilters();
		String orgId = (String) param.get(GlobalConstant.KEY_ORGID);
		String q = (String) param.get("q");
		SolrParam solrParam = SolrUtil.getSolrPageParam(pageRequest.getPageNumber(), pageRequest.getPageSize());
		solrParam.setQ(q);
		solrParam.setSort(sort);
		Map<String, String> params = new HashMap<String, String>();
		params.put(GlobalConstant.URL_PARAM_ORG, orgId);
		params.put(GlobalConstant.URL_PARAM_PUBLISHED, GlobalConstant.QUERY_TOPIC_PUB_NOTPUB);
		String url = GlobalConstant.URL_TOPIC_GET+"&"+ SolrUtil.joinUrlParam(solrParam,params, null, null);
		log.info("pageQuery:[url#"+url+",pageNumbter#"+pageRequest.getPageNumber()+",pageSize#"+pageRequest.getPageSize()+"]:分页查询专题开始");
		InputStream in = SolrUtil.sendSolrRequest(url, null, GlobalConstant.HTTP_TYPE_GET, null);
		Map result = SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_TOPIC);
		Page page = new Page(pageRequest.getPageNumber(), pageRequest.getPageSize(), (Integer)result.get(GlobalConstant.RESULT_TOTAL),(List) result.get(GlobalConstant.RESULT_LIST));
		return page;
	}

	public Page<?> getContent(PageRequest<?> pageRequest) throws Exception{
		StringBuilder urlParam = new StringBuilder();
		Map param = (Map) pageRequest.getFilters();
		String id = (String) param.get("id");
		int pageNumber = pageRequest.getPageNumber();
		int pageSize = pageRequest.getPageSize();
		SolrParam solrParam = SolrUtil.getSolrPageParam(pageNumber, pageSize);
		solrParam.setSort(pageRequest.getSortColumns());
		Map<String, String> params = new HashMap<String, String>();
		params.put(GlobalConstant.URL_PARAM_ID, id);
		String url = GlobalConstant.URL_TOPIC_CONTENT_GET+"&"+SolrUtil.joinUrlParam(solrParam, params, null, null);
		log.info("getContent:[url#"+url+",pageNumber#"+pageNumber+",pageSize#"+pageSize+"]:分页查询专题内容开始");
		InputStream in = SolrUtil.sendSolrRequest(url, null, GlobalConstant.HTTP_TYPE_GET, null);
		Map result = SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_ARTICLE);
		int total = (Integer) result.get(GlobalConstant.RESULT_TOTAL);
		Page page = new Page(pageNumber, pageSize, total);
		page.setResult((List) result.get(GlobalConstant.RESULT_LIST));
		return page;
	}
	//保存专题
	public Map saveTopic(Topic topic) throws Exception {
		//数据填充
		if(topic.getResults() <= 0){
			topic.setResults(GlobalConstant.DEFUALT_TOPIC_RESULT);
		}
		Date now = new Date();
		topic.setCreateDate(now);
		topic.setUpdateTime(now);
		String xml = ServerModelTrandsform.objectToXml(topic);
		InputStream in = SolrUtil.sendSolrRequest(GlobalConstant.URL_TOPIC_SAVE, xml, GlobalConstant.HTTP_TYPE_POST, GlobalConstant.HTTP_DATA_TYPE_XML);
		return SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
	}

	public Map putTopicContent(String id) throws Exception{
		String url = GlobalConstant.URL_TOPIC_CONTENT_SAVE+"&id="+id;
		log.info("putTopicContent:[url#"+url+"]:生成专题内容开始");
		InputStream in = SolrUtil.sendSolrRequest(url, null, GlobalConstant.HTTP_TYPE_GET, null);
		Map result = SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
		return result;
	}
	
	public Page<?> checkBeforeCreate(PageRequest<?> pageRequest) throws Exception{
		Map param = (Map) pageRequest.getFilters();
		String q = (String) param.get("q");
		SolrParam solrParam = new SolrParam();
		solrParam.setQ(q);
		solrParam.setFrom(SolrParam.DEFAULT_FROM);
		solrParam.setTo(SolrParam.DEFAULT_TO);
		solrParam.setSort(pageRequest.getSortColumns());
		String url = GlobalConstant.URL_TOPIC_CHECK+"&"+SolrUtil.joinUrlParam(solrParam, null, null, null);
		log.info("checkBeforeCreate:[url#"+url+"]:查看专题生成条件开始");
		InputStream in = SolrUtil.sendSolrRequest(url, null, GlobalConstant.HTTP_TYPE_GET, null);
		Map result = SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_ARTICLE);
		int total = (Integer) result.get(GlobalConstant.RESULT_TOTAL);
		Page page = new Page(1, 20, total);
		page.setResult((List) result.get(GlobalConstant.RESULT_LIST));
		return page;
	}
	
	public Map updateContent(List<Article> articles) throws Exception{
		String xml = ServerModelTrandsform.objectToXml(articles);
		InputStream in = SolrUtil.sendSolrRequest(GlobalConstant.URL_TOPIC_CONTENT_UPDATE, xml, GlobalConstant.HTTP_TYPE_POST, GlobalConstant.HTTP_DATA_TYPE_XML);
		Map results = SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
		return results;
	}
	
	// 获取全部可授权专题信息
	public Page<?> getAllTopics(PageRequest<?> pageRequest) throws Exception{
		Map param = (Map) pageRequest.getFilters();
		String orgId = (String)param.get(GlobalConstant.KEY_ORGID);
		String topicAuthOrg = (String)param.get("topicAuthOrg");
		int pageNumber = pageRequest.getPageNumber();
		int pageSize = pageRequest.getPageSize();
		SolrParam solrParam = SolrUtil.getSolrPageParam(pageNumber, pageSize);
		Map<String, Object> topicparams = new HashMap<String, Object>();
		topicparams.put("orgId", topicAuthOrg);
		topicparams.put("topicType", TopicAuth.TOPIC_TYPE_ADVANCED);
		
		StringBuffer idbuf = new StringBuffer("");
		List<TopicAuth> talist = this.topicAuthDao.queryAllTopicAuthByOrgId(topicparams);
		if(talist != null){
			for(int i = 0, len = talist.size(); i < len; i++){
				idbuf.append(talist.get(i).getTopicId() + " OR ");
			}
			idbuf.append("1");
		}
		Map<String, String> params = new HashMap<String, String>();
		orgId = "apabi";			//临时修改，后期定方案
		params.put(GlobalConstant.URL_PARAM_ORG, orgId);
		params.put(GlobalConstant.URL_PARAM_QTYPE, GlobalConstant.QTYPE_ADVANCED);
		params.put(GlobalConstant.URL_PARAM_TOPICTYPE, ""+Topic.TOPIC_TYPE_ADVANCE);
		params.put(GlobalConstant.URL_PARAM_PUBLISHED, GlobalConstant.QUERY_TOPIC_PUB_NOTPUB);
		if(!"".equals(idbuf.toString())){
			params.put(GlobalConstant.URL_PARAM_ID, idbuf.toString());
		}
		String url = GlobalConstant.URL_TOPIC_GET + "&" + SolrUtil.joinUrlParam(solrParam, params, null, null);
		log.info("getAllTopics:[url#"+url+"]:查询机构"+orgId+"下的高级专题");
		InputStream in = SolrUtil.sendSolrRequest(url, null, GlobalConstant.HTTP_TYPE_GET, null);
		Map result = SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_TOPIC);
		int total = (Integer) result.get(GlobalConstant.RESULT_TOTAL);
		List<Topic> solrtlist = (List) result.get(GlobalConstant.RESULT_LIST);
		Page page = new Page(pageNumber, pageSize, solrtlist.size(),solrtlist);
		return page;
	}

	// 批量专题授权
	@Transactional(propagation=Propagation.REQUIRED)
	public int saveTopicAuthBatch(TopicAuth topicAuth, String[] topicAuthIds) throws Exception{
		List<TopicAuth> talist = new ArrayList<TopicAuth>();
		Date now = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String status = null;
		Date startDate = topicAuth.getStartDate();
		if(DateUtil.isToday(startDate)){
			status = TopicAuth.TOPIC_STATUS_AUTH;
		}else{
			status = TopicAuth.TOPIC_STATUS_UNAUTH;
		}
		for(int i = 0, len = topicAuthIds.length; i < len; i++){
			TopicAuth ta = new TopicAuth();
			ta.setOrgId(topicAuth.getOrgId());
			ta.setCrtDate(now);
			ta.setLastDate(now);
			ta.setTopicId(topicAuthIds[i]);
			ta.setStartDate(startDate);
			ta.setEndDate(topicAuth.getEndDate());
			ta.setTopicType(TopicAuth.TOPIC_TYPE_ADVANCED);
			ta.setStatus(status);
			talist.add(ta);
		}
		log.info("saveTopicAuthBatch:[orgId#"+topicAuth.getId()+",size#"+topicAuthIds.length+"]:批量授权专题");
		int count = this.topicAuthDao.saveTopicAuthBatch(talist);
		if(status.equals(TopicAuth.TOPIC_STATUS_AUTH)){
			String xml = ServerModelTrandsform.objectToXml(talist);
			InputStream in = SolrUtil.sendSolrRequest(GlobalConstant.URL_FILTER_TOPIC_CREATE, xml, GlobalConstant.HTTP_TYPE_POST, GlobalConstant.HTTP_DATA_TYPE_XML);
			Map returnMsg =SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
			List<SolrResult> solrResults = (List<SolrResult>)returnMsg.get(GlobalConstant.RESULT_LIST);
			if(GlobalConstant.RESULT_STATUS_SUCCESS.equals(solrResults.get(0).getCode())){
				log.info("saveTopicAuthBatch:"+sf.format(now)+"-机构"+topicAuth.getOrgId()+"的高级专题授权已成功通知solr");
			}else{
				log.info("saveTopicAuthBatch:"+sf.format(now)+"-机构"+topicAuth.getOrgId()+"的高级专题授权通知solr失败");
			}
		}
		return count;
	}
	
	// 通过id获取专题
	public Topic getTopicById(String id) throws Exception{
		String url = GlobalConstant.URL_TOPIC_GET+"&id="+URLEncoder.encode(id,"UTF-8");
		log.info("getTopicById:[url#"+url+"]:通过id获取专题开始");
		InputStream in = SolrUtil.sendSolrRequest(url, null, GlobalConstant.HTTP_TYPE_GET, null);
		Map result = SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_TOPIC);
		List<Topic> topics = (List<Topic>) result.get(GlobalConstant.RESULT_LIST);
		Topic topic = null;
		if(topics != null && topics.size() > 0){
			topic = topics.get(0);
		}
		return topic;
	}
	
	public Map updateTopic(Topic topic) throws Exception{
		if(topic.getResults() <= 0){
			topic.setResults(1000);
		}
		Date now = new Date();
		topic.setUpdateTime(now);
		String xml = ServerModelTrandsform.objectToXml(topic);
		InputStream in = SolrUtil.sendSolrRequest(GlobalConstant.URL_TOPIC_UPDATE, xml, GlobalConstant.HTTP_TYPE_POST, GlobalConstant.HTTP_DATA_TYPE_XML);
		return SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
	}
	
	public Map deleteTopics(List<Topic> topics) throws Exception{
		String xml = ServerModelTrandsform.objectToXml(topics);
		InputStream in = SolrUtil.sendSolrRequest(GlobalConstant.URL_TOPIC_DELETE, xml, GlobalConstant.HTTP_TYPE_POST, GlobalConstant.HTTP_DATA_TYPE_XML);
		return SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
	}
	
	public Map deleteTopicContent(List<Article> articles) throws Exception{
		String xml = ServerModelTrandsform.objectToXml(articles);
		InputStream in = SolrUtil.sendSolrRequest(GlobalConstant.URL_TOPIC_CONTENT_DELETE, xml, GlobalConstant.HTTP_TYPE_POST, GlobalConstant.HTTP_DATA_TYPE_XML);
		return SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
	}
	
	//分页查询专题授权列表
	public Page<?> getTopicAuthList(PageRequest<?> pageRequest, String countQuery) throws Exception {
		Map param = (Map) pageRequest.getFilters();
		String q = (String) param.get("q");
		String orgId = (String)param.get(GlobalConstant.KEY_ORGID);
		param.put("status", TopicAuth.TOPIC_STATUS_AUTH);
		Page page = null;
		if(q != null){
			page = topicAuthDao.getAllByOrg(GlobalConstant.PAGE_QUERY_STATEMENT, pageRequest, countQuery);
		}else{
			page = topicAuthDao.pageQuery(GlobalConstant.PAGE_QUERY_STATEMENT, pageRequest, countQuery);
		}
		StringBuffer idbuf = new StringBuffer("");
		List<TopicAuth> talist = page.getResult();
		if(talist != null){
			for(int i = 0, len = talist.size(); i < len; i++){
				idbuf.append(talist.get(i).getTopicId() + " OR ");
			}
			idbuf.append("1");
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put(GlobalConstant.URL_PARAM_PUBLISHED, GlobalConstant.QUERY_TOPIC_PUB_NOTPUB);
		if(!"".equals(idbuf.toString())){
			params.put(GlobalConstant.URL_PARAM_ID, idbuf.toString());
		}
		if(q != null){
			params.put("q", q);
		}
		String url = GlobalConstant.URL_TOPIC_GET+"&"+SolrUtil.joinUrlParam(params);
		log.info("getTopicAuthList:[url#"+url+"]:分页查询专题授权列表开始");
		InputStream in = SolrUtil.sendSolrRequest(url, null, GlobalConstant.HTTP_TYPE_GET, null);
		Map result = SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_TOPIC);
		List<Topic> tlist = (List) result.get(GlobalConstant.RESULT_LIST);
		List<TopicAuth> authList = new ArrayList<TopicAuth>();
		if(tlist != null && tlist.size() > 0){
			for(int i = 0, ilen = talist.size(); i < ilen; i++){
				TopicAuth ta = talist.get(i);
				for(int j = 0, jlen = tlist.size(); j < jlen; j++){
					Topic t = tlist.get(j);
					if(ta.getTopicId().equals(t.getId())){
						ta.setTopic(t);
						authList.add(ta);
						break;
					}
				}
			}
		}
		
		page = new Page(pageRequest.getPageNumber(), pageRequest.getPageSize(), authList.size(),authList);
//		if(q != null){
//			page = new Page(pageRequest.getPageNumber(), pageRequest.getPageSize(), authList.size(),authList);
//		}else{
//			page.setResult(authList);
//		}
		return page;
	}

	//更新专题授权列表
	public boolean updateTopicAuth(TopicAuth topicAuth) throws Exception{
		return this.topicAuthDao.updateTopicAuth(topicAuth);
	}
	
	//将所有普通专题的权限分配给该机构(如果该机构购买了专题)
	public Map updateTopicAuthByNewOrg(String orgId) throws Exception{
		List<Topic> topiclist = this.getAllGeneralTopicList();
		List<TopicAuth> talist = new ArrayList<TopicAuth>();
		Date now = new Date();
		Date end = GlobalConstant.GENERAL_SERVICE_EXPIRES;
		for (Topic topic : topiclist) {
			TopicAuth ta = new TopicAuth();
			ta.setOrgId(orgId);
			ta.setCrtDate(now);
			ta.setLastDate(now);
			ta.setStartDate(now);
			ta.setEndDate(end);
			ta.setType(TopicAuth.TOPIC_TYPE_GENERAL);
			ta.setTopicId(topic.getId());
			ta.setTopicType(TopicAuth.TOPIC_TYPE_GENERAL);
			ta.setStatus(TopicAuth.TOPIC_STATUS_AUTH);
			talist.add(ta);
		}
		this.topicAuthDao.saveTopicAuthBatch(talist);
		String xml = ServerModelTrandsform.objectToXml(talist);
		InputStream in = SolrUtil.sendSolrRequest(GlobalConstant.URL_FILTER_TOPIC_CREATE, xml, GlobalConstant.HTTP_TYPE_POST, GlobalConstant.HTTP_DATA_TYPE_XML);
		return SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
	}
	
	//将新建的普通专题的权限分配给所有购买了专题的机构
	public Map updateTopicAuthByNewTopic(Topic topic) throws Exception{
		List<AuthOrg> orglist = this.authOrgDao.getAllBuyTopicOrgList();
		List<TopicAuth> talist = new ArrayList<TopicAuth>();
		Date now = new Date();
		Date end = GlobalConstant.GENERAL_SERVICE_EXPIRES;
		String topicId = topic.getId();
		for (AuthOrg org : orglist) {
			TopicAuth ta = new TopicAuth();
			ta.setOrgId(org.getOrgId());
			ta.setCrtDate(now);
			ta.setLastDate(now);
			ta.setStartDate(now);
			ta.setEndDate(end);
			ta.setType(TopicAuth.TOPIC_TYPE_GENERAL);
			ta.setTopicId(topicId);
			ta.setTopicType(TopicAuth.TOPIC_TYPE_GENERAL);
			ta.setStatus(TopicAuth.TOPIC_STATUS_AUTH);
			talist.add(ta);
		}
		this.topicAuthDao.saveTopicAuthBatch(talist);
		String xml = ServerModelTrandsform.objectToXml(talist);
		InputStream in = SolrUtil.sendSolrRequest(GlobalConstant.URL_FILTER_TOPIC_CREATE, xml, GlobalConstant.HTTP_TYPE_POST, GlobalConstant.HTTP_DATA_TYPE_XML);
		return SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
	}
	
	//获取全部普通专题列表
	public List<Topic> getAllGeneralTopicList() throws Exception{
		String url = GlobalConstant.URL_TOPIC_GET + "&org=apabi&topictype=0&from=1&to=10000&published="+GlobalConstant.QUERY_TOPIC_PUB_NOTPUB;
		log.info("getAllGeneralTopicList:请求solr的路径["+url+"]");
		InputStream in = SolrUtil.sendSolrRequest(url, null, GlobalConstant.HTTP_TYPE_GET, null);
		Map result = SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_TOPIC);
		return (List) result.get(GlobalConstant.RESULT_LIST);
	}
	
	//通过机构orgId删除普通专题授权
	public void deleteTopicAuthByOrgId(String orgId) throws Exception{
		//通知solr删除已授权专题
//		List<TopicAuth> talist = this.topicAuthDao.queryAllTopicAuthByOrgId(orgId);
//		String xml = ModelTransformUtil.objectToXml(talist);
		String url = GlobalConstant.URL_FILTER_TOPIC_ALLDELETE + "&orgid="+orgId;
		log.info("deleteTopicAuthByOrgId:[机构修改删除专题权限，通知solr的url："+url+"]");
		InputStream in = SolrUtil.sendSolrRequest(url, null, GlobalConstant.HTTP_TYPE_GET, null);
		Map result = SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
		List<SolrResult> solrResults = (List<SolrResult>)result.get(GlobalConstant.RESULT_LIST);
		if(solrResults != null && solrResults.size() > 0 && solrResults.get(0).getCode().equals(GlobalConstant.RESULT_STATUS_SUCCESS)){
			log.info("deleteTopicAuthByOrgId:通过机构orgId删除专题响应码("+solrResults.get(0).getCode()+")");
			this.topicAuthDao.deleteTopicAuthByOrgId(orgId);
		}
	}

	@Override
	public void deleteTopicAuthByTopicIds(String[] topicIds) throws Exception {
		this.topicAuthDao.deleteTopicAuthByTopicIds(topicIds);
	}

	//查询所有过期专题,并通知solr
	public void updateTopicExpire(String currentDateStr) {
		List<TopicAuth> talist = null;
		try {
			//获取全部当前生效但已过期专题
			talist = this.topicAuthDao.getAllExpireTopicAuth(currentDateStr);
			//更新已过期专题状态
			for (TopicAuth topicAuth : talist) {
				String orgId = topicAuth.getOrgId();
				topicAuth.setStatus(TopicAuth.TOPIC_STATUS_EXPIRE);
				this.topicAuthDao.updateTopicAuth(topicAuth);
				//删除当前机构推荐表中过期专题记录
				this.recommendDao.deleteRecommendByOrgId(orgId, Recommend.RECOMMEND_TYPE_TOPIC);
			}
			//将已过期专题通知solr
			if(talist != null && talist.size() > 0){
				Map updatereturnMsg = deleteTopicAuths(talist);
				List<SolrResult> solrResults = (List<SolrResult>)updatereturnMsg.get(GlobalConstant.RESULT_LIST);
				if(solrResults != null && solrResults.size() > 0){
					log.info("updateTopicExpire:"+currentDateStr+"-专题是否过期定时任务:"+"返回值("+solrResults.get(0).getCode()+")");
				}
			}
		} catch (Exception e) {
			log.error("updateTopicExpire:" + currentDateStr + "-更新过期专题列表发生错误");
			e.printStackTrace();
		}
	}

	//更新所有即将生效专题,并通知solr
	public void updateUnauthTopic(String currentDateStr) {
		try {
			//获取所有即将生效专题
			List<TopicAuth> talist = this.topicAuthDao.getAllUnauthTopicAuth(currentDateStr);
			//更新即将生效专题状态
			for (TopicAuth topicAuth : talist) {
				String orgId = topicAuth.getOrgId();
				topicAuth.setStatus(TopicAuth.TOPIC_STATUS_AUTH);
				this.topicAuthDao.updateTopicAuth(topicAuth);
			}
			if(talist != null && talist.size() > 0){
				Map updatereturnMsg = createTopicAuths(talist);
				List<SolrResult> solrResults = (List<SolrResult>)updatereturnMsg.get(GlobalConstant.RESULT_LIST);
				if(solrResults != null && solrResults.size() > 0){
					log.info("updateTopicExpire:"+currentDateStr+"-专题是否即将生效定时任务:"+"返回值("+solrResults.get(0).getCode()+")");
				}
			}
		} catch (Exception e) {
			log.error("updateUnauthTopic:" + currentDateStr + "-更新即将生效专题列表发生错误");
			e.printStackTrace();
		}
		
	}
	//删除solr中过期的专题
	public Map deleteTopicAuths(List<TopicAuth> topicAuths) throws Exception{
		String xml = ServerModelTrandsform.objectToXml(topicAuths);
		InputStream in = SolrUtil.sendSolrRequest(GlobalConstant.URL_FILTER_TOPIC_DELETE, xml, GlobalConstant.HTTP_TYPE_POST, GlobalConstant.HTTP_DATA_TYPE_XML);
		return SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
	}
	//将即将生效的专题通知solr
	public Map createTopicAuths(List<TopicAuth> topicAuths) throws Exception{
		String xml = ServerModelTrandsform.objectToXml(topicAuths);
		InputStream in = SolrUtil.sendSolrRequest(GlobalConstant.URL_FILTER_TOPIC_CREATE, xml, GlobalConstant.HTTP_TYPE_POST, GlobalConstant.HTTP_DATA_TYPE_XML);
		return SolrUtil.getSolrResponse(in, GlobalConstant.XPATH_RESULT);
	}

	public RecommendDao getRecommendDao() {
		return recommendDao;
	}

	public void setRecommendDao(RecommendDao recommendDao) {
		this.recommendDao = recommendDao;
	}
}
