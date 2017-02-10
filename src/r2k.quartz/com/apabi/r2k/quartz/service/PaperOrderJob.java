package com.apabi.r2k.quartz.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import com.apabi.r2k.admin.service.PaperAuthService;
import com.apabi.r2k.admin.service.TopicService;

public class PaperOrderJob {

	@Resource(name="paperAuthService")
	private PaperAuthService paperAuthService;
	@Resource(name="topicServiceImpl")
	private TopicService topicService;
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	
	//检查报纸订单定时任务:先运行是否过期定时任务,后运行是否生效定时任务
	public void checkPaperAuth(){
		Date now = new Date();
		this.paperAuthService.updateAuthExpire(sf.format(now));
		this.paperAuthService.updateUnauthOrder(sf.format(now));
	}

	//检查专题过期生效定时任务
	public void checkTopicAuth(){
		Date now = new Date();
		this.topicService.updateTopicExpire(sf.format(now));
		this.topicService.updateUnauthTopic(sf.format(now));
	}
	
	public PaperAuthService getPaperAuthService() {
		return paperAuthService;
	}
	public void setPaperAuthService(PaperAuthService paperAuthService) {
		this.paperAuthService = paperAuthService;
	}
	public TopicService getTopicService() {
		return topicService;
	}
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}
}
