package com.apabi.r2k.api.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.apabi.r2k.api.exception.ApiException;
import com.apabi.r2k.api.service.ApiService;
import com.apabi.r2k.api.utils.ApiUtils;
import com.apabi.r2k.common.security.springsecurity.des.BASE64Encoder;
import com.apabi.r2k.common.template.TemplateProcessor;
import com.apabi.r2k.common.utils.ApiUtil;
import com.apabi.r2k.common.utils.BaseSession;
import com.apabi.r2k.common.utils.DateUtil;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.common.utils.FileUtils;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.HttpParamUtils;
import com.apabi.r2k.common.utils.HttpUtils;
import com.apabi.r2k.common.utils.MD5;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.ShuyuanUtil;
import com.apabi.r2k.common.utils.XmlUtil;
import com.apabi.r2k.msg.service.MsgConnector;
import com.apabi.r2k.security.model.AuthOrg;

@Scope("prototype")
public class ApiAction {
	
	private Logger log = LoggerFactory.getLogger(ApiAction.class);
	
	private ApiService apiService;
	
	private MsgConnector msgConnector;

	private BaseSession baseSession;
	
	//报纸id
	private String paperid;
	//期id
	private String periodid;
	//版id
	private String pageid;
	//文章id
	private String articleid;
	//put类型
	private String putType;
	//分页起始值
	private String from;
	//分页结束值
	private String to;
	//开始时间
	private String startdate;
	//结束时间
	private String enddate;
	//检索条件
	private String q;
	//专题id
	private String topicid;
	//文章内容截取字数
	private int words;
	//机构id
	private String orgId;
	//专题查询id字符串
	private String topicsIds;
	//查询专题状态
	private String published;
	//是否需要cebx
	private String hasCebx;
	//机构查询
	private String name_startsWith;
	//资源标识
	private String metaId;
	//用户名称
	private String userName;
	//权限
	private String rights;
	//报纸地区
	private String place;
	//报纸分类
	private String subjectCode;
	//返回xml
	private InputStream xmlStream;
	//报纸期次出版时间
	private String publishDate;
	//报纸名称
	private String papername;
	
	private String uid;
	private String pwd;
	private String email;

	
	//end
	
	public static final String INX_START = "1";			//solr查询默认起始值
	public static final String INX_END = "10000";		//solr查询默认终值


	/**
	 * 本地系统心跳接口
	 */
	public void slaveHeartbeat(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			HttpServletResponse res = ServletActionContext.getResponse();
			msgConnector.serverHandler(req, res);
		} catch (Exception e) {
			log.info("slaveHeartbeat:服务器端消息处理异常");
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取导航菜单列表
	 */
	
	public void menu(){
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		OutputStream os = null;
		String deviceid = getRequestDeviceId();
		AuthOrg authOrg = getRequestAuthOrg();
		String devicetype = getUserAgent();
		/*测试代码**/
		/*  AuthOrg authOrg = new AuthOrg();
		authOrg.setEbook("1");
		authOrg.setPaper("1");
		authOrg.setPicture("1");
		authOrg.setPublish("1");
		authOrg.setTopic("1");
		authOrg.setOrgId("test1");
		String devicetype = GlobalConstant.USER_AGENT_ANDROID_LARGE;
		String deviceid="001";*/
		String filetype = PropertiesUtil.get("path.menu");;
		String menuname=PropertiesUtil.get("file.menu.pub.name");
		String ifModifiedSince = request.getHeader(HttpHeaders.IF_MODIFIED_SINCE);
		try {
			String rootPath = PropertiesUtil.getRootPath();
			String filePath = apiService.getFilePath(authOrg, devicetype, deviceid, filetype, menuname);
			String path = rootPath+"/"+filePath;
			File menuzip = new File(path);
			log.info("menu:菜单地址:"+path);
			String lastModified = ApiUtils.getFileLastModifiedForGMT(menuzip);
			if(lastModified.equalsIgnoreCase(ifModifiedSince)){
				response.setStatus(HttpStatus.SC_NOT_MODIFIED);
			}else{
				os = response.getOutputStream();
				ApiUtils.setHeader(response,lastModified);
				FileUtils.copyFile(menuzip, os);

			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(os != null){
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void publish(){
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		String deviceid = getRequestDeviceId();
		AuthOrg authOrg = getRequestAuthOrg();
		String devicetype = getUserAgent();
		
		String filetype = PropertiesUtil.get("path.info");
		String infoname= PropertiesUtil.get("file.info.pub.index.name");
		String infourl = "";
		try {
			String filePath = apiService.getFilePath(authOrg, devicetype, deviceid, filetype, infoname);
			infourl = PropertiesUtil.get("server.url")+"/"+filePath;
			response.sendRedirect(infourl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * 获取某屏报纸列表或获取某份报纸信息（带参数paperid）
	 */
	public void paper(){
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			String api = "";
			String cOrgId = getRequestOrgId();
			if(StringUtils.isNotBlank(cOrgId)){
				orgId = cOrgId;
				if(StringUtils.isNotBlank(paperid)){
					api = "getPaper";
				}else{
					api = "getPapers";
				}
				if(StringUtils.isBlank(from)){
					from = INX_START;
				}
				if(StringUtils.isBlank(to)){
					to = INX_END;
				}
				forwordUrl(api,response);
			}else{
				TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1003", "机构id为空"),response);
			}
		} catch (Exception e) {
			TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1004", "操作失败"),response);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取某期报纸期信息
	 */
	public void period(){
		HttpServletResponse response = ServletActionContext.getResponse();
		String api = "getPeriod";
	    if(StringUtils.isNotBlank(periodid)){
	    	if(StringUtils.isBlank(from)){
				from = INX_START;
			}
			if(StringUtils.isBlank(to)){
				to = INX_END;
			}
	    	forwordUrl(api,response);
	    }else{
	    	TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1003", "参数无效"),response);
	    	//返回参数无效失败
	    	//throw new Exception("");
	    }
	}
	
	/**
	 * 获取报纸某版信息
	 */
	public void page(){
		HttpServletResponse response = ServletActionContext.getResponse();
		String api = "getPage";
	    if(StringUtils.isNotBlank(pageid)){
	    	if(StringUtils.isBlank(from)){
				from = INX_START;
			}
			if(StringUtils.isBlank(to)){
				to = INX_END;
			}
	    	forwordUrl(api,response);
	    }else{
	    	TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1003", "参数无效"),response);
	    	//返回参数无效失败
	    	//throw new Exception("");
	    }
	}
	
	/**
	 * 获取报纸某版信息
	 */
	public void article(){
		HttpServletResponse response = ServletActionContext.getResponse();
		String api = "getArticle";
	    if(StringUtils.isNotBlank(articleid)){
	    	forwordUrl(api,response);
	    }else{
	    	TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1003", "参数无效"),response);
	    	//返回参数无效失败
	    	//throw new Exception("");
	    }
	}
	
	/**
	 * 获取报纸地区信息
	 */
	public void place(){
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			orgId = getRequestOrgId();
			String api = "getPlaces";
			forwordUrl(api, response);
		} catch (Exception e) {
			TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1003", "参数无效"),response);
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取报纸分类列表
	 */
	public void subjectCode(){
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			orgId = getRequestOrgId();
			String api = "getSubjectCodes";
			forwordUrl(api, response);
		} catch (Exception e) {
			TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1003", "参数无效"),response);
			e.printStackTrace();
		}
	}
	
	//请求查询连接并将数据返回给response
	private void forwordUrl(String api,HttpServletResponse response){
        try {
        	String url = makeUrl(api);
        	log.info("forwordUrl:[url#"+url+"]:转发url请求");
			HttpUtils.generateResponse(url, response);
		} catch (Exception e) {
			TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1004","返回错误"), response);
			e.printStackTrace();
		}
		
	}
	//拼接请求查询的url
	private String makeUrl(String api) throws Exception{
		StringBuffer url = new StringBuffer();
		if(api.equals("getContents") || api.equals("getForwardTopics")||api.equals("getContent")){
			url.append(GlobalConstant.URL_TOPIC_QUERY);
		}else{
			url.append(GlobalConstant.URL_PAPER_QUERY);
		}
		url.append("?");
		url.append("api="+api);
		if(api.equals("getPaper")){
			if(StringUtils.isNotBlank(paperid)){
				url.append("&id="+paperid);	
			}
			
		}else if(api.equals("getPeriod")){
			if(StringUtils.isNotBlank(periodid)){
				url.append("&id="+periodid);	
			}
			
		}else if(api.equals("getPage")){
			if(StringUtils.isNotBlank(pageid)){
				url.append("&id="+pageid);	
			}
		}else if(api.equals("getArticle")){
			if(StringUtils.isNotBlank(articleid)){
				url.append("&id="+articleid);	
		    }
		}else if(api.equals("getContents")){
			if(StringUtils.isNotBlank(topicid)){
				url.append("&id="+topicid);
			}
		}
		
		//待续
		if(StringUtils.isNotBlank(from)&&StringUtils.isNotBlank(to)){
			url.append("&from="+from+"&to="+to);
		}
		
		if(StringUtils.isNotBlank(startdate)){
			url.append("&startdate="+startdate);
		}
		if(StringUtils.isNotBlank(enddate)){
			url.append("&enddate="+enddate);
		}
		if(StringUtils.isNotBlank(q)){
			url.append("&q="+q);
		}
		if(words > 0){
			url.append("&words="+words);
		}
		if(StringUtils.isNotBlank(topicid)){
			url.append("&topicid="+topicid);
		}
		if(StringUtils.isNotBlank(articleid)){
			url.append("&articleid="+articleid);
		}
		if(StringUtils.isNotBlank(orgId)){
			url.append("&org="+orgId);
		}
		if(StringUtils.isNotBlank(topicsIds)){
			url.append("&id="+topicsIds);
		}
		if(StringUtils.isNotBlank(published)){
			url.append("&published="+published);
		}
		if(StringUtils.isNotBlank(hasCebx)){
			url.append("&cebx="+hasCebx);
		}
		if(StringUtils.isNotBlank(place)){
			url.append("&place="+place);
		}
		if(StringUtils.isNotBlank(subjectCode)){
			url.append("&subjectCode="+subjectCode);
		}
		if(StringUtils.isNotBlank(papername)){
			url.append("&papername="+papername);
		}
		System.out.println("url........["+url+"]");
		return url.toString();
	}
	
	
	
	
	public void put() {
		if(StringUtils.isNotBlank(putType)){
			HttpServletRequest req = ServletActionContext.getRequest();
			HttpServletResponse res = ServletActionContext.getResponse();
			if(putType.equals("email")){
				SAXReader saxReader = new SAXReader();
				try {
					Document xmlDoc = saxReader.read(req.getInputStream());
					log.info(xmlDoc.asXML());
					SAXParser saxParser = XmlUtil.getSAXParser();
					String xsdUrl = ServletActionContext.getServletContext().getRealPath("/")+ PropertiesUtil.get("url.xsd.url.email");
					saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource","file:" + xsdUrl);
					String result = XmlUtil.checkSingleXml(xmlDoc,saxParser);		//校验
					if (result != null) {
						TemplateProcessor.generateResponse("fail.xml",ApiException.makeMode("-1005", "格式错误"), res);
						log.info("格式错误:"+result);
					} else {
						Node toNode = xmlDoc.selectSingleNode("//R2k/PaperCut/Email/To");
						String to = toNode.getText();
						Node bodyNode = xmlDoc.selectSingleNode("//R2k/PaperCut/Email/Body");
						String body = bodyNode.getText();
						Node leftNode = xmlDoc.selectSingleNode("//R2k/PaperCut/Paper/Coordinate/Left");
						String left = leftNode.getText();
						Node rightNode = xmlDoc.selectSingleNode("//R2k/PaperCut/Paper/Coordinate/Right");
						String right = rightNode.getText();
						Node proportionNode = xmlDoc.selectSingleNode("//R2k/PaperCut/Paper/Proportion");
						String proportion = proportionNode.getText();
						List<Node> pageNodes = xmlDoc.selectNodes("//R2k/PaperCut/Paper/Page/Link");
						int size = pageNodes.size();
						for (int i = 0; i < size; i++) {
							Node page = pageNodes.get(i);
						}
							Map<String, Object> mailContent = new HashMap<String, Object>();
							mailContent.put("body", body);
							apiService.sendMail(to, mailContent);
							TemplateProcessor.generateResponse("success.xml", ApiException.makeMode("0", "成功"),res);
						}
				} catch (DocumentException e) {
					TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1005", "格式错误"),res);
					e.printStackTrace();
				}  catch (Exception e) {
					TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1004", "操作失败"),res);
					e.printStackTrace();
				}
			}
		}
	}
	
	public void topic(){
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			orgId = getRequestOrgId();
			if(orgId != null){
				String api = "";
				if(StringUtils.isNotBlank(articleid) && StringUtils.isNotBlank(topicid)){
					api = "getContent";
				}else if(StringUtils.isNotBlank(topicid)){
					api = "getContents";
				}else{
					api = "getForwardTopics";
				}
				forwordUrl(api,response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void subscribe(){
		HttpServletRequest req = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			String method = req.getMethod();
			String userId = getRequestUserId();
			String orgId = getRequestOrgId();
			if(userId != null && orgId != null){
				if(method.toLowerCase().equals(GlobalConstant.HTTP_TYPE_GET)){
					response.setContentType(GlobalConstant.HTTP_DATA_TYPE_XML);
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(apiService.getPaperSubs(orgId, userId));
				}else{
					InputStream in = req.getInputStream();
					List<Node> nodes = XmlUtil.getNodesFromInputStream(in, "//Subscribe");
					if(nodes != null && nodes.size() > 0){
						apiService.saveOrDelPaperSubs(nodes, orgId, userId, method);
						TemplateProcessor.generateResponse("success.xml", ApiException.makeMode("0", "成功"),response);
					}else{
						log.info("subscribe:[userId#"+userId+",orgId#"+orgId+"]:xml无效");
						TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1003", "参数无效"),response);
					}
				}
			}else{
				log.info("subscribe:[userId#"+userId+",orgId#"+orgId+"]:无授权");
				TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "无授权"),response);
			}
		} catch (Exception e) {
			TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1004", "操作失败"),response);
			log.error("subscribe:操作异常,"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void suggest(){
		HttpServletRequest req = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			String userId = getRequestUserId();
			String orgId = getRequestOrgId();
			if(userId != null && orgId != null){
				InputStream in = req.getInputStream();
				List<Node> nodes = XmlUtil.getNodesFromInputStream(in, "//Suggest");
				if(nodes != null && nodes.size() > 0){
					apiService.saveSuggest(nodes, orgId, userId);
					TemplateProcessor.generateResponse("success.xml", ApiException.makeMode("0", "成功"),response);
				}else{
					log.info("suggest:[userId#"+userId+",orgId#"+orgId+"]:xml无效");
					TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1003", "参数无效"),response);
				}
			}else{
				log.info("suggest:[userId#"+userId+",orgId#"+orgId+"]:无授权");
				TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1002", "无授权"),response);
			}
		} catch (Exception e) {
			TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1004", "操作失败"),response);
			log.error("suggest:操作异常,"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void orgSuggest() {
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			log.info("orgSuggest:[name_startsWith#"+name_startsWith+"]:前台机构查询智能提示");
			String xml = apiService.fuzzySearchOrg(name_startsWith);
			response.setContentType(GlobalConstant.HTTP_DATA_TYPE_XML);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(xml);
		} catch (Exception e) {
			log.error("orgSuggest:[name_startsWith#"+name_startsWith+"]:前台机构查询智能提示出错");
			e.printStackTrace();
		}
	}
	
	//图书签名
	public void ebookSign(){
		HttpServletResponse response = ServletActionContext.getResponse();
		// OrgId_UserName_MetaId_Rights_Time_RightsKey
		String sign = null;
		String rightKey = PropertiesUtil.get("rightKey");
		try {
			userName = orgId;
			if (orgId != null && metaId != null && userName != null && rights != null) {
				String time = getTime();
				sign = orgId + "_"+ userName +"_" + metaId + "_"+ rights + "_" + time + "_" + rightKey;
				sign = new MD5().md5s(sign).toUpperCase();
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("sign", sign);
				model.put("time", URLEncoder.encode(time, "UTF-8"));
				model.put("code", "0");
				model.put("username", userName);
				TemplateProcessor.generateResponse("sign.xml", model,response);
			}else{
				log.info("ebookSign:[orgId#"+orgId+",userName#"+userName+",metaId#"+metaId+",rights#"+rights+"]");
				TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1003", "参数无效"),response);
			}
		} catch (UnsupportedEncodingException e) {
			log.error("ebookSign:[orgId#"+orgId+",userName#"+userName+",metaId#"+metaId+",rights#"+rights+"]");
			TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1003", "程序异常"),response);
			e.printStackTrace();
		}
	}
	
	//图片签名
	public void pictureSign(){
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			String userAgent = getUserAgent();
			String uid = "";
			if(userAgent.equals(GlobalConstant.USER_AGENT_ANDROID_LARGE)){
				uid = "yuezhi";
			} else if(userAgent.equals(GlobalConstant.USER_AGENT_IPAD) || userAgent.equals(GlobalConstant.USER_AGENT_IPHONE)){
				uid = getRequestUserId();
			}
			Map<String, Object> signMap = ApiUtil.pictureSign(uid, metaId);
			signMap.put("code", "0");
			signMap.put("username", uid);
			TemplateProcessor.generateResponse("sign.xml", signMap,response);
		} catch (Exception e) {
			log.error("pictureSign:[orgId#"+orgId+",metaId#"+metaId+"]:图片签名异常");
			TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1003", "程序异常"),response);
			e.printStackTrace();
		}
	}
	
	public void msg(){
		try {
			if(StringUtils.isNotBlank(paperid) && StringUtils.isNotBlank(periodid) && StringUtils.isNotBlank(publishDate)){
				apiService.createPaperMsg(paperid, periodid, publishDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//图片分类无权限
	public String category(){
		String result ="success";
		try {
			String orgid = getRequestOrgId();
			String userAgent = getUserAgent();
			if(StringUtils.isBlank(orgid)){
				log.error("category:机构id为空");
				xmlStream = XmlUtil.createXmlStream("fail.xml", ApiException.makeMode("-1002", "机构id为空"));
				return result;
			}
			if(StringUtils.isBlank(userAgent)){
				log.error("category:userAgent为空");
				xmlStream = XmlUtil.createXmlStream("fail.xml", ApiException.makeMode("-1002", "userAgent为空"));
				return result;
			}
			String path = getCategoryPath(userAgent);
			xmlStream = ApiAction.class.getClassLoader().getResourceAsStream(path);
			if(xmlStream == null){
				xmlStream = XmlUtil.createXmlStream("fail.xml", ApiException.makeMode("-1003", "数据文件没找到"));
			}
		} catch (Exception e) {
			log.error("category:程序异常:"+e.getMessage());
			try {
				xmlStream = XmlUtil.createXmlStream("fail.xml", ApiException.makeMode("-1003", "userAgent为空"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return result;
	}
	
	
	//注册书苑用户接口
	public void autoShuyuanSignUp(){
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest req = ServletActionContext.getRequest();
		String userAgent = HttpParamUtils.getUserAgent(req);
		if(DevTypeEnum.isHaveUser(userAgent)&&StringUtils.isNotBlank(orgId)){
		    try{
				Document xmldoc = autoSySignUp(req);
				response.setCharacterEncoding("UTF-8");
				Writer writer = response.getWriter();
				writer.write(xmldoc.asXML());
				writer.close();
		      }catch (Exception e) {
				 TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-101", "系统异常"),response);
				 log.error(e.getMessage(), e);
		     }
		}else{
			TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-2010", "参数异常"),response);
		}
	}
	
	public void getPlaceAndOrg(){
		SAXReader sax= new SAXReader();
		Document xmldoc;
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			//data/" + name" +
				
			String placexmlpath = ApiAction.class.getClassLoader().getResource("data/Place.xml").toString();
				//PropertiesUtil.getRootPath()+"/r2k/resources/data/Place.xml";
			//String placexmlpath= "D://Workspace//r2kServer//src//r2k.resources//data//Place.xml";
			xmldoc = sax.read(placexmlpath);
			response.setCharacterEncoding("UTF-8");
			Writer writer = response.getWriter();
			writer.write(xmldoc.asXML());
			writer.close();
		} catch (Exception e) {
			TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-101", "系统异常"),response);
			log.error(e.getMessage(),e);
		}
		
		
		
		
	}
	
	//
	private Document autoSySignUp(HttpServletRequest req) throws Exception{
		 uid = ShuyuanUtil.randomUid();
		 BASE64Encoder ben = new BASE64Encoder();
		 String base64pwd = ben.encode(uid.substring(0,5).getBytes());
		 pwd =base64pwd;
		 Document xmldoc = null;
		 HttpEntity entity = ShuyuanUtil.shuyuanSignup(orgId, uid, pwd, email, userName);
		 if(null!=entity){
			InputStream in = entity.getContent();
			 xmldoc = XmlUtil.getDocumentFromInputStream(in);
			Attribute  attrCode = (Attribute) xmldoc.selectSingleNode("//Return/@Code");
			if(attrCode!=null&&ShuyuanUtil.successCode.equals(attrCode.getText())){
				//String userAgent = GlobalConstant.USER_AGENT_IPAD;
				String userAgent= HttpParamUtils.getUserAgent(req);
				if(apiService.login(orgId, uid, pwd, userAgent)){
					setCurrentUser();
					Element element = xmldoc.getRootElement();
					element.addElement("Uid").addText(uid);
					element.addElement("Pwd").addText(pwd);
					element.addElement("Orgid").addText(orgId);
					
					element.addElement("Jsessionid").addText(req.getSession().getId());
					String baseurl = PropertiesUtil.get("url.r2k.api");
					String menu = PropertiesUtil.get("path.menu");
					element.addElement("Menuurl").addText(baseurl+"/"+menu);
					element.addElement("Ebookurl").addElement("Baseurl").addText(ShuyuanUtil.APABI_URL);
					
					Node token = (Node) xmldoc.selectSingleNode("//Return/token");
					Node uid = (Node) xmldoc.selectSingleNode("//Return/uid");
					Node orgidentifier = (Node) xmldoc.selectSingleNode("//Return/orgidentifier");
					Node orgname = (Node) xmldoc.selectSingleNode("//Return/orgname");
					element.addElement("Token").addText(token.getText());
					element.remove(token);
					if(uid!=null){
						element.remove(uid);
					}
					if(orgidentifier!=null){
						element.remove(orgidentifier);

					}
					if(orgname!=null){
						element.addElement("Orgname").addText(orgname.getText());
						element.remove(orgname);
					}
				
				}else{
					throw new Exception("r2kserver login error");
				}
				
			}else if(attrCode!=null&&ShuyuanUtil.userRepeatCode.equals(attrCode.getText())){
				autoSySignUp(req);
			}
		 }
			return xmldoc; 
	}
	
	private String getCategoryName(String userAgent){
		String name = "pictures.xml";
//		path = userAgent + "-" + path;
		return name;
	}
	
	private String getCategoryPath(String userAgent){
		String name = getCategoryName(userAgent);
		String path = "data/" + name;
		return path;
	}
	
	private String getTime() {
		String time= DateUtil.formatDate(new Date());
		return time;
	}
	
	private String getRequestUserId(){
		HttpServletRequest req = ServletActionContext.getRequest();
		return baseSession.getUserId(req);
	}
	
	private String getRequestOrgId(){
		HttpServletRequest req = ServletActionContext.getRequest();
		return baseSession.getOrgId(req);
	}
	
	private String getRequestDeviceId(){
		HttpServletRequest req = ServletActionContext.getRequest();
		return baseSession.getDeviceId(req);
	}
	
	private String getUserAgent(){
		HttpServletRequest req = ServletActionContext.getRequest();
		return baseSession.getUserAgent(req);
	}

	private String getRequestPassword(){
		HttpServletRequest req = ServletActionContext.getRequest();
		return baseSession.getDeviceId(req);
	}
	
	private void setCurrentUser(){
		HttpServletRequest req = ServletActionContext.getRequest();
		baseSession.setCurrentUser(req);
	}
	
	private AuthOrg getRequestAuthOrg(){
		HttpServletRequest req = ServletActionContext.getRequest();
		return baseSession.getAuthOrg(req);
	}
	
	public BaseSession getBaseSession() {
		return baseSession;
	}

	public void setBaseSession(BaseSession baseSession) {
		this.baseSession = baseSession;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	
	public ApiService getApiService() {
		return apiService;
	}

	public void setApiService(ApiService apiService) {
		this.apiService = apiService;
	}

	public String getPaperid() {
		return paperid;
	}

	public void setPaperid(String paperid) {
		this.paperid = paperid;
	}

	public String getPeriodid() {
		return periodid;
	}

	public void setPeriodid(String periodid) {
		this.periodid = periodid;
	}

	public String getPageid() {
		return pageid;
	}

	public void setPageid(String pageid) {
		this.pageid = pageid;
	}
	public String getArticleid() {
		return articleid;
	}

	public void setArticleid(String articleid) {
		this.articleid = articleid;
	}

	public String getPutType() {
		return putType;
	}

	public void setPutType(String putType) {
		this.putType = putType;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}
	public String getTopicid() {
		return topicid;
	}

	public void setTopicid(String topicid) {
		this.topicid = topicid;
	}

	public int getWords() {
		return words;
	}

	public void setWords(int words) {
		this.words = words;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getHasCebx() {
		return hasCebx;
	}

	public void setHasCebx(String hasCebx) {
		this.hasCebx = hasCebx;
	}

	public String getName_startsWith() {
		return name_startsWith;
	}

	public void setName_startsWith(String name_startsWith) {
		this.name_startsWith = name_startsWith;
	}

	public String getMetaId() {
		return metaId;
	}

	public void setMetaId(String metaId) {
		this.metaId = metaId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRights() {
		return rights;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}
	
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public MsgConnector getMsgConnector() {
		return msgConnector;
	}

	public void setMsgConnector(MsgConnector msgConnector) {
		this.msgConnector = msgConnector;
	}

	public InputStream getXmlStream() {
		return xmlStream;
	}

	public void setXmlStream(InputStream xmlStream) {
		this.xmlStream = xmlStream;
	}
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPapername() {
		return papername;
	}

	public void setPapername(String papername) {
		this.papername = papername;
	}

}
