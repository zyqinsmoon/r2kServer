package com.apabi.r2k.admin.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.Article;
import com.apabi.r2k.admin.model.Topic;
import com.apabi.r2k.admin.model.TopicAuth;
import com.apabi.r2k.admin.service.FreeMarkerService;
import com.apabi.r2k.admin.service.TopicService;
import com.apabi.r2k.common.base.PageRequestFactory;
import com.apabi.r2k.common.solr.SolrResult;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.common.utils.HttpParamUtils;
import com.apabi.r2k.security.utils.SessionUtils;

@Controller("topicAction")
@Scope("prototype")
public class TopicAction {
	private Logger log = LoggerFactory.getLogger(TopicAction.class);
	
	@Resource
	private TopicService topicService;
	@Autowired
	private FreeMarkerService freeMarkerService;
	
	private List<Topic> topiclist;
	private List<TopicAuth> topicAuthlist;
	private Topic topic;
	private Article article;
	private Page page;
	private String pageType;
	private String[] topicAuthIds = null;
	private String[] topicIds = null;
	private String[] topicContentIds = null;
	private TopicAuth topicAuth;
	private File icon;
	private String topicId;
	private String flag;
	private String orgId;
	private String topicName;
	private String articleId;
	private Map returnMsg;
	private String topicAuthOrg;
	
	public final String PAGE_TYPE_SHOWCONTENT = "show";
	public final String PAGE_TYPE_CHECKQUERY = "check";
	public final String RESULT_LIST = "result";
	public final String RESULT_STATUS_SUCCESS = "1000";
	public final String RESULT_STATUS_FAILURE = "1001";
	
	public final String DEFAULT_SORT = "Position asc";
	
	
	//查询专题列表
	public String showTopics(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				PageRequest pageRequest= new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest,req);
				Map param = (Map) pageRequest.getFilters();
				param.put("orgId", orgId);
				if(pageRequest.getSortColumns() == null){
					pageRequest.setSortColumns(DEFAULT_SORT);
				}
				page = this.topicService.pageQuery(pageRequest, null);
			}
		} catch (Exception e) {
			log.error("showTopics:查询专题列表异常");
			log.error(e.getMessage(),e);
			return "error";
		}
		return "list";
	}
	
	public String showContent(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				PageRequest pageRequest= new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest,req);
				Map param = (Map) pageRequest.getFilters();
				param.put("id", topic.getId());
				if(pageRequest.getSortColumns() == null){
					pageRequest.setSortColumns(DEFAULT_SORT);
				}
				page = this.topicService.getContent(pageRequest);
			}
		} catch (Exception e) {
			log.error("showContent:查询专题内容列表异常");
			log.error(e.getMessage(),e);
			return "error";
		}
		return "content";
	}
	
	//保存专题
	public String save(){
		String result = null;
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				if(topic != null){
					topic.setOrg(orgId);			
					String filename = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+".jpg"; 
					//上传图片
					if(icon != null){
						uploadImage(filename, icon, orgId);
						topic.setIcon(filename);
					}
					returnMsg = this.topicService.saveTopic(topic);
					List<SolrResult> solrResults = (List<SolrResult>)returnMsg.get(RESULT_LIST);
					if(solrResults != null && solrResults.size() > 0){
						if(RESULT_STATUS_SUCCESS.equals(solrResults.get(0).getCode())){
							if(topic.getTopicType() == 0){
								String message = solrResults.get(0).getMessage();
								if(message != null && !"".equals(message)){
									String[] strs = message.split(",");
									String[] topicId = strs[0].split(":");
									topic.setId(topicId[1]);
									this.topicService.updateTopicAuthByNewTopic(topic);
								}
							}
							result = "save";
						}else if(RESULT_STATUS_FAILURE.equals(solrResults.get(0).getCode())){
							result = "error";
						}
					}else{
						result = "error";
					}
				}else{
					result = "error";
					log.info("save:[result#" + result + "]:保存专题失败");
				}
			}
		} catch (Exception e) {
			log.error("save:[result#" + result + "]:保存专题失败");
			log.error(e.getMessage(),e);
		}
		return result;
	}
	
	//返回webapps路径地址
	private String getRootPath(){
		// 计算目录位置
		String dir = System.getProperty("user.dir");
		dir = dir.substring(0, dir.lastIndexOf("bin"));
		String root = dir + "webapps";
		return root;
	}
	
	//上传图片
	private void uploadImage(String filename, File icon, String orgId) throws Exception{
		String root = getRootPath();
		String path = root + "/r2kFile/upload/image/" + orgId + "/topic/" + filename;
		log.info("专题图片保存路径：" + path);
		File destFile = new File(path);
		destFile.getParentFile().mkdirs();
		FileUtils.copyFile(icon, destFile);
	}
	
	public String putTopicContent(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				returnMsg = this.topicService.putTopicContent(topic.getId());
			}
		} catch (Exception e) {
			log.error("putTopicContent:生成专题内容异常");
			log.error(e.getMessage(),e);
		}
		return "put";
	}
	
	public String checkBeforeCreate(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				PageRequest pageRequest= new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest,req);
				Map param = (Map) pageRequest.getFilters();
				param.put("q", topic.getCondition().getQuery());
				page = this.topicService.checkBeforeCreate(pageRequest);
			}
		} catch (Exception e) {
			log.error("checkBeforeCreate:查看生成条件异常");
			log.error(e.getMessage(),e);
			return "error";
		}
		return "content";
	}
	
	//显示全部专题信息
	public String showAllTopics(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null && this.topicAuthOrg != null){
				PageRequest pageRequest= new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest,req);
				Map params = (Map) pageRequest.getFilters();
				params.put("orgId", orgId);
				params.put("topicAuthOrg", topicAuthOrg);
				page = this.topicService.getAllTopics(pageRequest);
			}
		} catch (Exception e) {
			log.error("showAllTopics:查看所有可授权专题信息出错");
			log.error(e.getMessage(),e);
		}
		return "allTopics";
	}
	
	//保存专题授权
	public String saveTopicAuth(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				if(topicAuthIds != null && topicAuth != null){
					int result = this.topicService.saveTopicAuthBatch(topicAuth, topicAuthIds);
					if(result == 0){
						this.flag = "0";
						return "error";
					}else{
						this.flag = "1";
					}
				}else{
					this.flag = "0";
					return "error";
				}
			}
		} catch (Exception e) {
			log.error("saveTopicAuth:批量授权专题出错");
			this.flag = "0";
			log.error(e.getMessage(),e);
		}
		return "authlist";
	}
	
	//跳转到保存专题页面
	public String toSaveTopic(){
		return "toSaveTopic";
	}
	
	//跳转到修改专题页面
	public String toUpdateTopic(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				if(topic != null ){
					topic = this.topicService.getTopicById(topic.getId());
				}else{
					log.info("toUpdateTopic:[topic#"+topic.getId()+"]:没有专题id");
					return "error";
				}
			}else{
				log.info("toUpdateTopic:[orgId#"+orgId+"]:没有机构id");
			}
		} catch (Exception e) {
			log.error("toUpdateTopic:跳转修改专题页面出错");
			log.error(e.getMessage(),e);
		}
		return "toUpdateTopic";
	}
	
	public String updateTopic(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				if(topic != null){
					//上传图片
					if(icon != null){
						String filename = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+".jpg"; 
						log.info("updateTopic:[filename#"+filename+"]:更新专题，上传图片");
						uploadImage(filename, icon, topic.getOrg());
						topic.setIcon(filename);
					}
					returnMsg = this.topicService.updateTopic(topic);
					List<SolrResult> solrResults = (List<SolrResult>)returnMsg.get(RESULT_LIST);
					if(solrResults != null && solrResults.size() > 0){
						log.info("updateTopic:更新专题响应码("+solrResults.get(0).getCode()+")");
					}
				}
			}
		} catch (Exception e) {
			log.error("updateTopic:更新专题出错");
			log.error(e.getMessage(),e);
		}
		return "update";
	}
	
	public String updateContent(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				List<Article> articles = new ArrayList<Article>();
				articles.add(article);
				log.info("updateContent:[id#"+article.getId()+",position#"+article.getPosition()+"]:更新专题内容顺序");
				returnMsg = this.topicService.updateContent(articles);
				List<SolrResult> solrResults = (List<SolrResult>)returnMsg.get(RESULT_LIST);
				if(solrResults != null && solrResults.size() > 0){
					log.info("updateContent:更新专题内容响应码("+solrResults.get(0).getCode()+")");
				}
			}
		} catch (Exception e) {
			log.error("updateContent:更新专题内容异常");
			log.error(e.getMessage(),e);
		}
		return "updateContent";
	}
	
	public String deleteTopics(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				if(topicIds != null){
					int size = topicIds.length;
					List<Topic> topics = new ArrayList<Topic>();
					for(int i = 0; i < size; i++){
						Topic tpc = new Topic();
						tpc.setId(topicIds[i]);
						tpc.setIsPublished(Topic.STATUS_DEL);
						topics.add(tpc);
					}
					log.info("deleteTopics:[topic size#"+size+"]:删除专题");
					returnMsg = this.topicService.deleteTopics(topics);
					List<SolrResult> solrResults = (List<SolrResult>)returnMsg.get(RESULT_LIST);
					if(solrResults != null && solrResults.size() > 0){
						SolrResult solrResult = solrResults.get(0);
						log.info("deleteTopics:删除专题响应码("+solrResult.getCode()+")");
						if(solrResult.getCode().equals("1000")){
							log.info("deleteTopics:删除专题授权");
							this.topicService.deleteTopicAuthByTopicIds(topicIds);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("deleteTopics:"+e.getMessage());
			log.error(e.getMessage(),e);
		}
		return "delete";
	}
	
	public String deleteTopicContent(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				if(topicContentIds != null){
					int size = topicContentIds.length;
					List<Article> articles = new ArrayList<Article>();
					for(int i = 0; i < size; i++){
						String id = topicContentIds[i];
						Article art = new Article();
						art.setId(id);
						articles.add(art);
					}
					returnMsg = this.topicService.deleteTopicContent(articles);
					List<SolrResult> solrResults = (List<SolrResult>)returnMsg.get(RESULT_LIST);
					if(solrResults != null && solrResults.size() > 0){
						log.info("deleteTopicContent:删除专题内容响应码("+solrResults.get(0).getCode()+")");
					}
				}
			}
		} catch (Exception e) {
			log.error("deleteTopicContent:"+e.getMessage());
			log.error(e.getMessage(),e);
		}
		return "delContent";
	}
	
	//显示授权专题列表
	public String showTopicAuthList(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				PageRequest pageRequest= new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest,req);
				Map param = (Map) pageRequest.getFilters();
				param.put("orgId", orgId);
				page = this.topicService.getTopicAuthList(pageRequest, null);
			}
		} catch (Exception e) {
			log.error("showTopicAuthList:显示已授权专题列表");
			log.error(e.getMessage(),e);
		}
		return "topicAuthList";
	}
	
	//单个专题列表页（瀑布流页面）
	public String showTopicList(){
		return "topiclist";
	}
	//单个专题详细页
	public String showTopicDetail(){
		return "topicdetail";
	}
	//专题整体列表页
	public String showTopicIndex(){
		HttpServletRequest req = ServletActionContext.getRequest();
		String devType = HttpParamUtils.getUserAgent(req);
		if(DevTypeEnum.isLarge(devType)){
			return "topicindex";
		}else{
			return "verticalindex";
		}
	}
	//竖屏
	public String showVerticalIndxe(){
		return "verticalindex";
	}
	
	
	//getters and setters
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
	public List<Topic> getTopiclist() {
		return topiclist;
	}
	public void setTopiclist(List<Topic> topiclist) {
		this.topiclist = topiclist;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public String[] getTopicAuthIds() {
		return topicAuthIds;
	}
	public void setTopicAuthIds(String[] topicAuthIds) {
		this.topicAuthIds = topicAuthIds;
	}
	public TopicAuth getTopicAuth() {
		return topicAuth;
	}
	public void setTopicAuth(TopicAuth topicAuth) {
		this.topicAuth = topicAuth;
	}

	public String[] getTopicIds() {
		return topicIds;
	}

	public void setTopicIds(String[] topicIds) {
		this.topicIds = topicIds;
	}

	public String[] getTopicContentIds() {
		return topicContentIds;
	}

	public void setTopicContentIds(String[] topicContentIds) {
		this.topicContentIds = topicContentIds;
	}

	public File getIcon() {
		return icon;
	}
	public void setIcon(File icon) {
		this.icon = icon;
	}
	public List<TopicAuth> getTopicAuthlist() {
		return topicAuthlist;
	}
	public void setTopicAuthlist(List<TopicAuth> topicAuthlist) {
		this.topicAuthlist = topicAuthlist;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public FreeMarkerService getFreeMarkerService() {
		return freeMarkerService;
	}

	public void setFreeMarkerService(FreeMarkerService freeMarkerService) {
		this.freeMarkerService = freeMarkerService;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Map getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(Map returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getTopicAuthOrg() {
		return topicAuthOrg;
	}

	public void setTopicAuthOrg(String topicAuthOrg) {
		this.topicAuthOrg = topicAuthOrg;
	}
}
