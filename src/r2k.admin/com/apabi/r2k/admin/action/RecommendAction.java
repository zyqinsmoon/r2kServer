package com.apabi.r2k.admin.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.apabi.r2k.admin.model.PaperAuth;
import com.apabi.r2k.admin.model.Recommend;
import com.apabi.r2k.admin.model.Topic;
import com.apabi.r2k.admin.model.TopicAuth;
import com.apabi.r2k.admin.service.PaperAuthService;
import com.apabi.r2k.admin.service.RecommendService;
import com.apabi.r2k.admin.service.TopicService;
import com.apabi.r2k.common.solr.SolrResult;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.paper.service.SyncMessageService;
import com.apabi.r2k.security.utils.SessionUtils;

@Controller("recommendAction")
@Scope("prototype")
public class RecommendAction {
	private Logger log = LoggerFactory.getLogger(RecommendAction.class);
	
	@Resource
	private TopicService topicService;
	@Autowired
	private RecommendService recommendService;
	@Resource
	private PaperAuthService paperAuthService;
	@Resource
	private SyncMessageService syncMessageService;
	
	private Topic topic;
	private TopicAuth topicAuth;
	private Recommend recommend;
	private PaperAuth paperAuth;
	
	private String topicId;
	private String paperId;
	private String orgId;
	private String type;
	private int sort;
	private String flag;
	
	
	//专题置顶处理
	public String setTopTopic(){
		String result = "settoptopic";
		HttpServletRequest req = ServletActionContext.getRequest();
		orgId = SessionUtils.getAuthOrgId(req);
		Date now = new Date();
		Map returnMsg = null;
		try {
			if(orgId != null && type != null && !"".equals(type)){
				this.topicAuth = new TopicAuth();
				Recommend rec = null;
				//普通专题或取消推荐
				if(TopicAuth.TOPIC_TYPE_GENERAL.equals(type)){
					topicAuth.setType(TopicAuth.TOPIC_TYPE_GENERAL);
					//删除solr中专题推荐记录
					returnMsg = this.recommendService.deleteSolrRecommendByTopicId(topicId, orgId);
				}//推荐专题
				else if(TopicAuth.RECOMMEND_TYPE_RECOMMEND.equals(type)){
					topicAuth.setType(TopicAuth.RECOMMEND_TYPE_RECOMMEND);
					topicAuth.setPosition(Recommend.RECOMMEND_DEFAULT_SORT);
					flag = GlobalConstant.STATUS_SUCCESS;
					return result;
				}
				if(returnMsg != null){
					List<SolrResult> solrResults = (List<SolrResult>)returnMsg.get(GlobalConstant.RESULT_LIST);
					if(solrResults != null && solrResults.size() > 0){
						if(GlobalConstant.RESULT_STATUS_SUCCESS.equals(solrResults.get(0).getCode())){
							if(TopicAuth.TOPIC_TYPE_GENERAL.equals(type)){
								this.recommendService.deleteRecommendByTopicId(topicId, orgId);
							}
							flag = GlobalConstant.STATUS_SUCCESS;
						}else if(GlobalConstant.RESULT_STATUS_FAILURE.equals(solrResults.get(0).getCode())){
							flag = GlobalConstant.STATUS_ERROR;
						}
					}else{
						flag = GlobalConstant.STATUS_ERROR;
					}
				}else{
					log.error("setTopTopic:专题置顶,solr返回值为空");
					flag = GlobalConstant.STATUS_ERROR;
				}
			}
		} catch (Exception e) {
			flag = GlobalConstant.STATUS_ERROR;
			log.error("setTopTopic:授权专题置顶错误");
			log.error(e.getMessage(),e);
		}
		return result;
	}

	//更新专题推荐顺序
	public String updateTopicSort(){
		String result = "updatesort";
		HttpServletRequest req = ServletActionContext.getRequest();
		orgId = SessionUtils.getAuthOrgId(req);
		Map returnMsg;
		try { 
			if(orgId != null && sort > 0 && topicId != null){
				Date now = new Date();
				recommend = new Recommend();
				recommend.setOrgId(orgId);
				recommend.setSort(sort);
				recommend.setResId(topicId);
				recommend.setLastDate(now);
				returnMsg = this.recommendService.updateSolrRecommendByTopicId(recommend);
				
				if(returnMsg != null){
					List<SolrResult> solrResults = (List<SolrResult>)returnMsg.get(GlobalConstant.RESULT_LIST);
					if(solrResults != null && solrResults.size() > 0){
						if(GlobalConstant.RESULT_STATUS_SUCCESS.equals(solrResults.get(0).getCode())){
							topicAuth = new TopicAuth();
							topicAuth.setType(TopicAuth.RECOMMEND_TYPE_RECOMMEND);
							//向推荐表添加数据
							this.recommendService.saveOrgUpdateRecommend(topicId, orgId, Recommend.RECOMMEND_TYPE_TOPIC, sort);
							flag = GlobalConstant.STATUS_SUCCESS;
						}else if(GlobalConstant.RESULT_STATUS_FAILURE.equals(solrResults.get(0).getCode())){
							flag = GlobalConstant.STATUS_FAILURE;
						}
					}else{
						flag = GlobalConstant.STATUS_ERROR;
					}
				}else{
					log.error("updateTopicSort:推荐专题调整顺序错误");
					flag = GlobalConstant.STATUS_ERROR;
				}
			}else{
				flag = GlobalConstant.STATUS_ERROR;
			}
		} catch (Exception e) {
			log.error("updateTopicSort:推荐专题调整顺序错误");
			flag = GlobalConstant.STATUS_ERROR;
			log.error(e.getMessage(),e);
		}
		return result;
	}
	
	//报纸置顶处理
	public String setTopPaper(){
		String result = "settoppaper";
		HttpServletRequest req = ServletActionContext.getRequest();
		orgId = SessionUtils.getAuthOrgId(req);
		Date now = new Date();
		Map returnMsg = null;
		try {
			if(orgId != null && type != null && !"".equals(type)){
				this.paperAuth = new PaperAuth();
				Recommend rec = null;
				//普通报纸或取消推荐
				if(TopicAuth.TOPIC_TYPE_GENERAL.equals(type)){
					paperAuth.setType(PaperAuth.PAPER_TYPE_GENERAL);
					//删除solr中报纸推荐记录
					returnMsg = this.recommendService.deleteSolrRecommendByPaperId(paperId, orgId);
				}//推荐专题
				else if(TopicAuth.RECOMMEND_TYPE_RECOMMEND.equals(type)){
					paperAuth.setType(PaperAuth.PAPER_TYPE_RECOMMEND);
					paperAuth.setPosition(Recommend.RECOMMEND_DEFAULT_SORT);
					flag = GlobalConstant.STATUS_SUCCESS;
					return result;
				}
				if(returnMsg != null){
					List<SolrResult> solrResults = (List<SolrResult>)returnMsg.get(GlobalConstant.RESULT_LIST);
					if(solrResults != null && solrResults.size() > 0){
						if(GlobalConstant.RESULT_STATUS_SUCCESS.equals(solrResults.get(0).getCode())){
							if(TopicAuth.TOPIC_TYPE_GENERAL.equals(type)){
								this.recommendService.deleteRecommendByPaperId(paperId, orgId);
							}
							log.info("setTopPaper:[orgId#"+orgId+"]:分发报纸filter消息");
							syncMessageService.distributeFilterMsg(orgId);
							flag = GlobalConstant.STATUS_SUCCESS;
						}else if(GlobalConstant.RESULT_STATUS_FAILURE.equals(solrResults.get(0).getCode())){
							flag = GlobalConstant.STATUS_ERROR;
						}
					}else{
						flag = GlobalConstant.STATUS_ERROR;
					}
				}else{
					log.error("setTopPaper:报纸置顶,solr返回值为空");
					flag = GlobalConstant.STATUS_ERROR;
				}
			}
		} catch (Exception e) {
			log.error("setTopTopic:授权报纸置顶错误");
			log.error(e.getMessage(),e);
		}
		return result;
	}
	
	//更新专题推荐顺序
	public String updatePaperSort(){
		String result = "updatesort";
		HttpServletRequest req = ServletActionContext.getRequest();
		orgId = SessionUtils.getAuthOrgId(req);
		Map returnMsg;
		try { 
			if(orgId != null && sort > 0 && paperId != null){
				Date now = new Date();
				recommend = new Recommend();
				recommend.setOrgId(orgId);
				recommend.setSort(sort);
				recommend.setResId(paperId);
				recommend.setLastDate(now);
				returnMsg = this.recommendService.updateSolrRecommendByPaperId(recommend);
				
				if(returnMsg != null){
					List<SolrResult> solrResults = (List<SolrResult>)returnMsg.get(GlobalConstant.RESULT_LIST);
					if(solrResults != null && solrResults.size() > 0){
						if(GlobalConstant.RESULT_STATUS_SUCCESS.equals(solrResults.get(0).getCode())){
							paperAuth = new PaperAuth();
							paperAuth.setType(PaperAuth.PAPER_TYPE_RECOMMEND);
							//更新推荐表信息
							this.recommendService.saveOrgUpdateRecommend(paperId, orgId, Recommend.RECOMMEND_TYPE_PAPER, sort);
							log.info("updatePaperSort:[orgId#"+orgId+"]:分发报纸filter消息");
							syncMessageService.distributeFilterMsg(orgId);
							flag = GlobalConstant.STATUS_SUCCESS;
						}else if(GlobalConstant.RESULT_STATUS_FAILURE.equals(solrResults.get(0).getCode())){
							flag = GlobalConstant.STATUS_FAILURE;
						}
					}else{
						flag = GlobalConstant.STATUS_ERROR;
					}
				}else{
					log.error("updatePaperSort:推荐报纸调整顺序错误");
					flag = GlobalConstant.STATUS_ERROR;
				}
			}else{
				flag = GlobalConstant.STATUS_ERROR;
			}
		} catch (Exception e) {
			flag = GlobalConstant.STATUS_ERROR;
			log.error("updatePaperSort:推荐报纸调整顺序错误");
			log.error(e.getMessage(),e);
		}
		return result;
	}
	

	// getter and setter
	
	public TopicService getTopicService() {
		return topicService;
	}
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	public TopicAuth getTopicAuth() {
		return topicAuth;
	}
	public void setTopicAuth(TopicAuth topicAuth) {
		this.topicAuth = topicAuth;
	}
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public RecommendService getRecommendService() {
		return recommendService;
	}
	public void setRecommendService(RecommendService recommendService) {
		this.recommendService = recommendService;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public Recommend getRecommend() {
		return recommend;
	}
	public void setRecommend(Recommend recommend) {
		this.recommend = recommend;
	}
	public String getPaperId() {
		return paperId;
	}
	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}
	public PaperAuth getPaperAuth() {
		return paperAuth;
	}
	public void setPaperAuth(PaperAuth paperAuth) {
		this.paperAuth = paperAuth;
	}
	public PaperAuthService getPaperAuthService() {
		return paperAuthService;
	}
	public void setPaperAuthService(PaperAuthService paperAuthService) {
		this.paperAuthService = paperAuthService;
	}
}
