package com.apabi.r2k.msg.service.impl.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apabi.r2k.common.utils.DateUtil;
import com.apabi.r2k.common.utils.FileUtils;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.HttpUtils;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.XmlUtil;
import com.apabi.r2k.msg.log.Log;
import com.apabi.r2k.msg.model.MsgResults;
import com.apabi.r2k.msg.model.PaperMsg;
import com.apabi.r2k.msg.model.ReplyHandleMsg;
import com.apabi.r2k.msg.service.MsgHandler;
import com.apabi.r2k.msg.service.impl.MsgRequest;
import com.apabi.r2k.msg.service.impl.MsgResponse;

public class PaperMsgHandler implements MsgHandler{

	private Logger log = LoggerFactory.getLogger(PaperMsgHandler.class);
	
	private String saveDir;		//数据文件存放目录
	private String fileBaseUrl;
	@Override
	public void handlerMsg(MsgRequest request,MsgResponse response) {
		PaperMsg msg = (PaperMsg) request.getMsg();		//获取报纸消息
		if(msg == null){
			return;
		}
		try {
			String url = msg.getUrl();
			Map<String, String> httpHeaders = getHttpHeaders();
			if(StringUtils.isNotBlank(url)){
				log.debug("获取报纸xml地址:"+url);
				HttpEntity entity = HttpUtils.httpGet(url, httpHeaders);
				Document doc = XmlUtil.getDocumentFromInputStream(entity.getContent());
//				System.out.println(doc.asXML());
				Paper paper = getPaper(doc);
				if(paper == null){
					createReplyMsg(msg, GlobalConstant.SYNC_MESSAGE_STATUS_FAIL);
					return;
				}
				Period period = getPeriod(doc);
				if(period == null){
					createReplyMsg(msg, GlobalConstant.SYNC_MESSAGE_STATUS_FAIL);
					return;
				}
				period.setPaperId(paper.getPaperId());
				String path = getSaveDir(paper, period);
				FileUtils.createDirs(path);		//创建文件存放目录
				fileBaseUrl = PropertiesUtil.get("fileBaseUrl");
				savePeriodFile(path + "/" + period.getPeriodId() + ".period.xml", doc.asXML());
				downloadPaper(path + "/" + period.getPeriodId() + ".paper.xml", paper, httpHeaders);
				downloadPages(path, period);
				HttpEntity resEntity = notifyCache(path);		//通知更新索引
				int status = getStatus(resEntity.getContent());
				createReplyMsg(msg, status);
				log(paper.getPaperId(), period.getPeriodId(), status);
			} else {
				log.info("handlerMsg:[msgid#"+msg.getId()+"]:该消息没有内容");
				throw new Exception("消息id:" + msg.getId() + ",该消息没有内容");
			}
		} catch (Exception e) {
			log.error("handlerMsg:[msgid#"+msg.getId()+"]:该消息处理异常",e);
			createReplyMsg(msg, GlobalConstant.SYNC_MESSAGE_STATUS_FAIL);
			log(String.valueOf(msg.getId()), msg.getUrl(), GlobalConstant.SYNC_MESSAGE_STATUS_FAIL);
			e.printStackTrace();
		}
	}

	private Map<String, String> getHttpHeaders(){
//		String orgid = PropertiesUtil.get("client.orgid");
		String orgid = "tiyan";
		Map<String, String> httpHeaders = new HashMap<String, String>();
		httpHeaders.put(HttpHeaders.USER_AGENT, GlobalConstant.USER_AGENT_SLAVE);
		httpHeaders.put("Cookie", "orgid="+orgid+";deviceid="+orgid);
		return httpHeaders;
	}
	
	//从期次xml中获取报纸信息
	private Paper getPaper(Document doc){
		Paper paper = null;
		Attribute idAttr = (Attribute) XmlUtil.getSingleNodes(doc, "//Paper/@id");	//获取报纸id
		String paperId = idAttr.getStringValue();
		if(StringUtils.isBlank(paperId)){
			return null;
		}
		Node linkNode = (Node) XmlUtil.getSingleNodes(doc, "//Paper/Link");		//获取报纸下载链接
		String link = linkNode.getStringValue();
		if(StringUtils.isBlank(link)){
			return null;
		}
		Node logoNode = (Node) XmlUtil.getSingleNodes(doc, "//Paper/Icon");		//获取报纸logo下载链接
		String logo = logoNode.getStringValue();
		paper = new Paper(paperId, link, logo);
		return paper;
	}
	
	//从期次xml中获取期次信息
	private Period getPeriod(Document doc){
		Period period = null;
		Attribute idAttr = (Attribute) XmlUtil.getSingleNodes(doc, "//Period/@id");		//获取期次id
		String periodId = idAttr.getStringValue();
		if(StringUtils.isBlank(periodId)){
			return null;
		}
		Node dateNode = (Node) XmlUtil.getSingleNodes(doc, "//Period//PublishedDate");		//获取报纸出版日期
		String publishedDate = dateNode.getStringValue();
		if(StringUtils.isBlank(publishedDate)){
			return null;
		}
		List<Element> pages = (List<Element>)XmlUtil.getNodes(doc, "//Period//CebxFile");	//获取cebx链接
		if(CollectionUtils.isEmpty(pages)){
			return null;
		}
		List<String> cebxs = new ArrayList<String>();
		for(Element page : pages){
			String cebx = page.getStringValue();
			if(StringUtils.isNotBlank(cebx)){
				cebxs.add(cebx);
			}
		}
		List<Element> pics = (List<Element>)XmlUtil.getNodes(doc, "//Period//Icon");
		if(CollectionUtils.isEmpty(pics)){
			return null;
		}
		List<String> pictures = new ArrayList<String>();
		for(Element pic : pics){
			String picUrl = pic.getStringValue();
			if(StringUtils.isNotBlank(picUrl)){
				pictures.add(picUrl);
			}
		}
		period = new Period();
		period.setPeriodId(periodId);
		period.setPublishedDate(DateUtil.getDateFromString(publishedDate, DateUtil.SOLR_DATE_FORMAT));
		period.setCebxs(cebxs);
		period.setPictures(pictures);
		return period;
	}
	
	//保存期次文件
	private void savePeriodFile(String fileName, String periodXml) throws Exception{
		OutputStream os = new FileOutputStream(fileName);
		byte[] buffer = periodXml.getBytes("UTF-8");
		int len = buffer.length;
		int offset = 4096;
		int count = len / offset + 1;
		for(int i = 0; i < count; i++){
			int size = offset;
			if((i+1)*offset > len){
				size = len - i * offset;
			}
			os.write(buffer, i*offset, size);
		}
		os.flush();
		os.close();
	}
	
	//获取文件保存目录
	private String getSaveDir(Paper paper, Period period){
		saveDir = PropertiesUtil.get("slave.data.dir");
		String path = null;
		String paperId = paper.getPaperId();
		Date date = period.getPublishedDate();
		//获取路径中期次信息部分
		String month = DateUtil.formatDate(date, "yyyy-MM");			//出版月份	
		String day = DateUtil.formatDate(date, "dd");					//出版日期
		String dirPaper = paperId.substring(paperId.indexOf(".")+1);		//获取路径中报纸id部分
		if(StringUtils.isNotBlank(dirPaper) && StringUtils.isNotBlank(month) && StringUtils.isNotBlank(day)){
			path = saveDir + "/" + dirPaper + "/" + month + "/" + day + "/mpml.files";
		}
		return path;
	}
	
	//下载文件
	private void downloadFile(String fileName, InputStream in) throws Exception {
		OutputStream out = new FileOutputStream(fileName);
		byte[] buffer = new byte[4096];
		int len = 0;
		while((len = in.read(buffer)) != -1){
			out.write(buffer, 0, len);
		}
		out.flush();
		out.close();
		in.close();
	}
	
	//下载报纸文件
	private void downloadPaper(String fileName, Paper paper, Map<String, String> httpHeaders) throws Exception{
		String url = paper.getLink();
		//从r2kserver下载报纸xml
		HttpEntity entity = HttpUtils.httpGet(url, httpHeaders);
		if(entity != null){
			downloadFile(fileName, entity.getContent());
		}
		//从服务器下载报纸logo
		String logoUrl = paper.getLogo();
		HttpEntity logoEntity = HttpUtils.httpGet(logoUrl);
		if(logoEntity != null){
			String logoDir = getLogoDir(logoUrl);		//获取报纸logo存储路径
			downloadFile(logoDir, logoEntity.getContent());
		}
	}
	
	//将报纸url转换为存储路径
	private String getLogoDir(String url){
		String logoDir = null;
		if(url.contains(fileBaseUrl)){
			logoDir = url.substring(fileBaseUrl.length());
		}else{
			String http = "http://";
			if(url.startsWith(http)){
				String dir = url.substring(http.length());
				logoDir = dir.substring(dir.indexOf("/"));
			}
		}
		String path = saveDir + "/" + logoDir;
		File logoFile = new File(path);
		File parent = logoFile.getParentFile();
		if(!parent.exists()){
			parent.mkdirs();
		}
		return path;
	}
	
	//下载单版文件:版面图或cebx
	private void downloadPage(String url, String savePath) throws Exception{
		int endInx = url.indexOf("?");
		String sourceUrl = (endInx == -1 ? url.substring(0) : url.substring(0, endInx));
		String fileName = sourceUrl.substring(sourceUrl.lastIndexOf("/"));		//获取cebx文件名
		HttpEntity entity = HttpUtils.httpGet(url);
		if(entity != null){
			downloadFile(savePath + "/" + fileName, entity.getContent());
		}
	}
	
	//下载所有版面文件
	private void downloadPages(String path, Period period) throws Exception{
		List<String> cebxUrls = period.getCebxs();
		for(String url : cebxUrls){
			downloadPage(url, path);
		}
		List<String> picUrls = period.getPictures();
		for(String url : picUrls){
			downloadPage(url, path);
		}
	}
	
	//创建处理结果消息
	private void createReplyMsg(PaperMsg paperMsg, int status){
		if(paperMsg == null){
			return;
		}
		int msgid = paperMsg.getId();
		ReplyHandleMsg replyMsg = new ReplyHandleMsg();
		replyMsg.setId(msgid);
		replyMsg.setStatus(status);
		MsgResults.addMsg(replyMsg);
	}
	
	//获取消息处理结果
	private int getStatus(InputStream in) throws Exception{
		Document doc = XmlUtil.getDocumentFromInputStream(in);
		System.out.println(doc.asXML());
		Element e = (Element) doc.selectSingleNode("//int[@name='msgstatus']");
		int status = e != null ? Integer.parseInt(e.getStringValue()) : GlobalConstant.SYNC_MESSAGE_STATUS_FAIL;
		log.info("getStatus:[status#"+status+"]:消息处理状态");
		return status;
	}
	
	//通知从solr更新数据
	private HttpEntity notifyCache(String path) throws Exception{
		String url = PropertiesUtil.get("slave.update.url")+"?path="+path;
		log.info("notifyCache:[url#"+url+"]:通知更新缓存");
		return HttpUtils.httpGet(url);
	}
	
	public String getSaveDir() {
		return saveDir;
	}

	public void setSaveDir(String saveDir) {
		this.saveDir = saveDir;
	}
	
	private void log(String paperId, String periodId, int status){
		String log = DateUtil.currentDate(DateUtil.DEFAULT_DATE_FORMAT) + "\t|\t" +paperId + "\t|\t" + periodId + "\t|\t" + status + "\n";
		Log.addLog(log);
	}
	
	//静态内部类：报纸实体
	public static class Paper{
		private String paperId;
		private String link;
		private String logo;
		
		public Paper(){}
		
		public Paper(String paperId, String link, String logo){
			this.paperId = paperId;
			this.link = link;
			this.logo = logo;
		}

		public String getPaperId() {
			return paperId;
		}

		public void setPaperId(String paperId) {
			this.paperId = paperId;
		}

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}
	}
	
	//静态内部类：期次实体
	public static class Period{
		private String periodId;
		private String paperId;
		private Date publishedDate;
		private List<String> cebxs;
		private List<String> pictures;
		
		public Period(){}

		public String getPeriodId() {
			return periodId;
		}

		public void setPeriodId(String periodId) {
			this.periodId = periodId;
		}

		public String getPaperId() {
			return paperId;
		}

		public void setPaperId(String paperId) {
			this.paperId = paperId;
		}

		public Date getPublishedDate() {
			return publishedDate;
		}

		public void setPublishedDate(Date publishedDate) {
			this.publishedDate = publishedDate;
		}

		public List<String> getCebxs() {
			return cebxs;
		}

		public void setCebxs(List<String> cebxs) {
			this.cebxs = cebxs;
		}

		public List<String> getPictures() {
			return pictures;
		}

		public void setPictures(List<String> pictures) {
			this.pictures = pictures;
		}
	}
	
	public void test(){
//		try {
//			String url = "http://r.apabi.com/r2k/api/period/nq.D110000bjwb_20140301"; 
//			Map<String, String> httpHeaders = getHttpHeaders();
//			if(StringUtils.isNotBlank(url)){
//				HttpEntity entity = HttpUtils.httpGet(url, httpHeaders);
//				Document doc = XmlUtil.getDocumentFromInputStream(entity.getContent());
//				System.out.println(doc.asXML());
//				Paper paper = getPaper(doc);
//				if(paper == null){
//					return;
//				}
//				Period period = getPeriod(doc);
//				if(period == null){
//					return;
//				}
//				period.setPaperId(paper.getPaperId());
//				String path = getSaveDir(paper, period);
//				FileUtils.createDirs(path);		//创建文件存放目录
//				fileBaseUrl = PropertiesUtil.get("fileBaseUrl");
//				savePeriodFile(path + "/" + period.getPeriodId() + ".period.xml", doc.asXML());
//				downloadPaper(path + "/" + period.getPeriodId() + ".paper.xml", paper, httpHeaders);
//				downloadPages(path, period);
//				HttpEntity resEntity = notifyCache(path);		//通知更新索引
//				int status = getStatus(resEntity.getContent());
//				log(paper.getPaperId(), period.getPeriodId(), status);
//			} else {
//			}
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			Map<String, String> httpHeaders = getHttpHeaders();
			File xmlFile = new File("G:/temp/rmrb1119.xml");
			InputStream in = new FileInputStream(xmlFile);
			Document doc = XmlUtil.getDocumentFromInputStream(in);
			Paper paper = getPaper(doc);
			if(paper == null){
				return;
			}
			Period period = getPeriod(doc);
			if(period == null){
				return;
			}
			period.setPaperId(paper.getPaperId());
			String path = getSaveDir(paper, period);
			FileUtils.createDirs(path);		//创建文件存放目录
			fileBaseUrl = PropertiesUtil.get("fileBaseUrl");
			savePeriodFile(path + "/" + period.getPeriodId() + ".period.xml", doc.asXML());
			downloadPaper(path + "/" + period.getPeriodId() + ".paper.xml", paper, httpHeaders);
			downloadPages(path, period);
			HttpEntity resEntity = notifyCache(path);		//通知更新索引
			int status = getStatus(resEntity.getContent());
			log(paper.getPaperId(), period.getPeriodId(), status);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		PaperMsgHandler pmh = new PaperMsgHandler();
		pmh.test();
	}
}
