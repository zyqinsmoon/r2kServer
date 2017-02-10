package com.apabi.r2k.wx.action;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.apabi.r2k.api.exception.ApiException;
import com.apabi.r2k.common.base.ServerModelTrandsform;
import com.apabi.r2k.common.template.TemplateProcessor;
import com.apabi.r2k.common.utils.ApiUtil;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.HttpUtils;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.ShuyuanUtil;
import com.apabi.r2k.common.utils.XmlUtil;
import com.apabi.r2k.wx.model.Category;
import com.apabi.r2k.wx.model.Ebook;

@Controller("EbookAction")
@Scope("prototype")
public class EbookAction {
	final Logger log = LoggerFactory.getLogger(EbookAction.class);

	private String orgid ;
	//private String bookPageNumBaseUrl="http://cebxol.apabi.com/command/silverlight.ashx";
		//"swhy";
	private String userName = "swhy03";
	private String passWord = "111111";
	private String type;
    private String page;
	private String pagesize;
    private String metaid;
	private String rexml;
	private List<Map<String, String>> catalogRowMapList;
	private List<Ebook> ebooks;
	private String totalCount;
	private Ebook ebook;
    private String successCode = "0";
    private String message;
    private String onlineurl;
    private String pageid;
	private String PageTotalCount;
	private String bookName;
	private String width;
	private String height;
	private String rights;
	private String parent;
	private String code;
	private String key;
	private String digitresgroupid;
	private List<Category> categorys;


	public static String SERVICE_TYPE_IMAGE = "Imagepage";
	public static String SERVICE_TYPE_HTML = "htmlpage";
	public static String SERVICE_TYPE_CSS = "htmlcss";
	public static String CAT_ZTF ="CAT_ZTF";

	//单页抽图
	public String ebook(){
		
		if(StringUtils.isBlank(pageid)||pageid.equals("0")){
			pageid=getFirstPageNum();
		}
    	String flag="wxfail";
        String baseUrlType = "command/imagepage.ashx";
   	    String serviceType =SERVICE_TYPE_IMAGE;
   	    getOrgRights();
   	  
    	//String token = getToken();
    	//String url = getOnlinereadUrl(token);
    	 String url = "";
		try {
			url = makeHtmlUrl(baseUrlType,serviceType);
			if(StringUtils.isNotBlank(url)){
	    		onlineurl=url;
	    		getBookPageNum();
	    		flag = "book";
	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
			
		}
    	
    	return flag;
    }
	//单页抽图
	public String ebook1(){
		if(StringUtils.isBlank(pageid)||pageid.equals("0")){
			pageid=getFirstPageNum();
		}
    	String flag = "booktest";
    	getBookPageNum();
    	return flag;
    }
	
	private String getFirstPageNum() {
		String type= "1";
  	    String url = makeBookdetailUrl(type);
		Document doc;
		String firstPageNum="1";
		String nameKey = PropertiesUtil.get("ebook.weixin.first");
			try {
				doc = XmlUtil.getDataFromSolr(url);
				Attribute  attrCode = (Attribute) doc.selectSingleNode("//Return/@Code");
	   			Attribute  attrMessage = (Attribute) doc.selectSingleNode("//Return/@Message");
	   			if(attrCode!=null&&successCode.equals(attrCode.getText())){
	   				List<Node> catalogRows = doc.selectNodes("//catalogRow");
		   			for (Node node :catalogRows) {
		   				Attribute  chapterNameAttr = (Attribute)node.selectSingleNode("./@chapterName");
						Attribute  ebookPageNumAttr = (Attribute)node.selectSingleNode("./@ebookPageNum");
						 String chapterName = chapterNameAttr.getText();
						 if(StringUtils.isNotBlank(chapterName)){
							 if(nameKey.indexOf(chapterName)==-1){
								 firstPageNum = ebookPageNumAttr.getText();
								 break;
							 }
							
						 }
					}
	          }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(), e);
			}
			return firstPageNum;	
			}
    //获取书的总页数
    private void getBookPageNum(){
    	String command = "command/silverlight.ashx";
    	String url = ShuyuanUtil.APABI_CEBXOL+"/"+command+"?metaID="+metaid;
    	try {
			Document doc = XmlUtil.getDataFromSolr(url);
			Attribute  attrCode = (Attribute) doc.selectSingleNode("//Return/@Code");
   			Attribute  attrMessage = (Attribute) doc.selectSingleNode("//Return/@Message");
   			if(attrCode!=null&&successCode.equals(attrCode.getText())){
 				  Node PageTotalCountNode = doc.selectSingleNode("/Return/PageTotalCount");
 				 Node BookNameNode = doc.selectSingleNode("/Return/BookName");
 				 
 				  if(PageTotalCountNode!=null){
 					 PageTotalCount = PageTotalCountNode.getText();
 				  }else{
 					 PageTotalCount="0"; 
 				  }
 				  if(BookNameNode!=null){
 					 bookName = BookNameNode.getText();
 				  }
 				 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
		}
    }
    
       //获取书目录
       public String getCatalogRow(){
    	   String type= "1";
    	   String url = makeBookdetailUrl(type);
   			Document doc;
   			String flag="";
			try {
				doc = XmlUtil.getDataFromSolr(url);
				Attribute  attrCode = (Attribute) doc.selectSingleNode("//Return/@Code");
	   			Attribute  attrMessage = (Attribute) doc.selectSingleNode("//Return/@Message");
	   			if(attrCode!=null&&successCode.equals(attrCode.getText())){
	   				List<Node> catalogRows = doc.selectNodes("//catalogRow");
		   			catalogRowMapList = new ArrayList<Map<String,String>>();
		   			for (Node node :catalogRows) {
		   				Attribute  chapterName = (Attribute)node.selectSingleNode("./@chapterName");
						Attribute  ebookPageNum = (Attribute)node.selectSingleNode("./@ebookPageNum");
						Map<String, String> catalogRowMap = new HashMap<String, String>();
						catalogRowMap.put( chapterName.getText(),ebookPageNum.getText());
						catalogRowMapList.add(catalogRowMap);
					}
		   			flag="catalogRow";
	   			}else{
	   				flag="wxfail";
	   				message=attrMessage.getText();
	   	    	   log.info("getCatalogRo>wmessage["+message+"]");

	   			}
	   			
	   			//HttpServletResponse response = ServletActionContext.getResponse();
				// ResponseUtils.responseOutXml(response, doc.asXML());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(), e);
			}
			return flag;
       }
       
     
       
       public void getHtml(){
    	   try {
    		   HttpServletResponse response = ServletActionContext.getResponse();
    		   String baseUrlType = "command/htmlpage.ashx";
        	   String serviceType =SERVICE_TYPE_HTML;
    		   String url = makeHtmlUrl(baseUrlType,serviceType);
    		   long a= System.currentTimeMillis();
    		   log.debug("请求开始["+a+"]");
    		   HttpUtils.generateResponse(url, response, GlobalConstant.HTTP_DATE_TYPE_HTML);
    		   long b= System.currentTimeMillis();
    		   log.debug("请求耗时["+(b-a)+"]url["+url+"]");
    	   } catch (Exception e) {
    		   log.error("getHtml:微信获取Html异常");
    		   log.error(e.getMessage(), e);
    	   }
       }
       
       public void getCss(){
    	   try {
    		   HttpServletResponse response = ServletActionContext.getResponse();
    		   String url = makeCssUrl();
    		   HttpUtils.generateResponse(url, response, GlobalConstant.HTTP_DATE_TYPE_HTML);
    	   } catch (Exception e) {
    		   log.error("getCss:微信获取Css异常");
    		   log.error(e.getMessage(), e);
    	   }
       }
       
     //获取书信息
       public String getBookDetail(){
    	   String type= "0";
    	   String url = makeBookdetailUrl(type);
   			Document doc;
   			String flag="";
			try {
				doc = XmlUtil.getDataFromSolr(url);
				Attribute  attrCode = (Attribute) doc.selectSingleNode("//Return/@Code");
	   			Attribute  attrMessage = (Attribute) doc.selectSingleNode("//Return/@Message");
	   			if(attrCode!=null&&successCode.equals(attrCode.getText())){
	   				  Node Record = doc.selectSingleNode("/Return/Record");
					  ebooks = new ArrayList<Ebook>();
					  ebook = (Ebook)ServerModelTrandsform.xmlToObj(Record.asXML(),"Record", Ebook.class);
				      String cfxUrl = makeCfxUrl();
				      ebook.setBorrowUrl(cfxUrl);
		   			//HttpServletResponse response = ServletActionContext.getResponse();
					// ResponseUtils.responseOutXml(response, doc.asXML());
				      flag="bookDetail";
	   			}else{
	   				flag="wxfail";
	   				message=attrMessage.getText();
	   			 log.info("getBookDetail>message["+message+"]");
	   			}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(), e);
			}
			return flag;
       }
       
       
     //获取推荐列表
	public String getCommendlist(){
		String url = makeCommendlistUrl();
		String flag = "";
		try {
			xmlToEbooks(url);
			flag = "commend";
		} catch (Exception e) {
			 flag="wxfail";
			log.info("getCommendlist>message["+message+"]");
			log.error(e.getMessage(), e);
		}
		return flag;
		
	}

	
	
	//获取机构的常用分类法
	public String getCatApabi(){
		String url = MakeCategoryUrl();
		String flag = "";
		Document doc;
		try {
			doc = XmlUtil.getDataFromSolr(url);
			Attribute  attrCode = (Attribute) doc.selectSingleNode("//Return/@Code");
			Attribute  attrMessage = (Attribute) doc.selectSingleNode("//Return/@Message");
			if(attrCode!=null&&successCode.equals(attrCode.getText())){
			   //System.out.println(attrCode.getText());
				Node  totalCountNode = doc.selectSingleNode("/Return/TotalCount");
				List<Node> Records = doc.selectNodes("/Return/Category/CatItem");
				totalCount = totalCountNode.getText();
				categorys = new ArrayList<Category>();
				 for (Node node:Records) {
					 Category category = (Category)ServerModelTrandsform.xmlToObj(node.asXML(),"CatItem", Category.class);
					 categorys.add(category);
					}
				 flag="catapabi";
			 }else{
				 flag="wxfail";
				 message=attrMessage.getText();
				 log.info("getCommendlist>message["+doc.toString()+"]");
			 }
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return flag;
	}

	//获取机构的常用分类法
	public void getCatApabiXml(){
		String url =MakeCategoryUrl();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			HttpUtils.generateResponse(url, response);
		
		} catch (Exception e) {
			TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1000", "书苑处理错误"),response);
			log.error(e.getMessage(), e);
		}
     }
	
	//根据分类进行检索
	public String getCategorySearch(){
		String url =makeCategorySearchUrl();
		String flag = "";
		try {
			xmlToEbooks(url);
			flag = "catsearch";
		} catch (Exception e) {
			flag = "wxfail";
			log.error(e.getMessage(), e);
		}
		return flag;
		
	}
	//根据分类进行检索
	public void getCategorySearchXml(){
		String url =makeCategorySearchUrl();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			HttpUtils.generateResponse(url, response);
		
		} catch (Exception e) {
			TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1000", "书苑处理错误"),response);
			log.error(e.getMessage(), e);
		}
		
	}
	
	//书苑检索返回xml
	public void getMetadatasearchXml(){
		HttpServletResponse response = ServletActionContext.getResponse();
		if(StringUtils.isNotBlank(key)){
			String url = makeMetadatasearchUrl();
			try {
				HttpUtils.generateResponse(url, response);
			}catch (Exception e) {
				TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1000", "书苑处理错误"),response);
				log.error(e.getMessage(), e);
			}
		}else{
			TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-2010", "参数错误，关键词为空"),response);

		}
	
	}
	
	//书苑检索
	public String getMetadatasearch(){
		HttpServletResponse response = ServletActionContext.getResponse();
		String flag = "";
		if(StringUtils.isNotBlank(key)){
			String url = makeMetadatasearchUrl();
			try {
				xmlToEbooks(url);
				flag = "search";
			} catch (Exception e) {
				flag = "wxfail";
				log.error(e.getMessage(), e);
			}
			
		}else{
			flag = "wxfail";
		}
		return flag;
	}
	//==============================private===============================================================
	
	private void validGetPage(){
		if(StringUtils.isBlank(page)||page.equals("0")||StringUtils.isBlank(pagesize)||pagesize.equals("0")){
 		   page = "1";
 		   pagesize = "50"; 
 	   }
	}
	
	private String makeCategorySearchUrl(){
		String baseUrlType = "mobile.mvc";
		String api = "categorysearch";
	    //BASEURL?api=categorysearch&type=CAT_ZTF&page=2&pagesize=20&code=102&digitresgroupid=12
		validGetPage();
		String url =ShuyuanUtil.APABI_URL+"/"+orgid+"/"+baseUrlType+"?api="+api+"&type="+CAT_ZTF+"&page="+page+"&pagesize="+pagesize;
		if(StringUtils.isNotBlank(this.code)){
			url+="&code="+code;
		}
		if(StringUtils.isNotBlank(this.digitresgroupid)){
			url+="&digitresgroupid="+digitresgroupid;
		}
		
		return url;
	}
	
	private String MakeCategoryUrl(){
		String baseUrlType = "mobile.mvc";
		String api = "getcategory";
		//http://www.apabi.com/tiyan/mobile.mvc?api=getcategory&type=CAT_APABI
		String url = ShuyuanUtil.APABI_URL+"/"+orgid+"/"+baseUrlType+"?api="+api+"&type="+CAT_ZTF;
		if(StringUtils.isNotBlank(this.parent)){
			url+="&parent="+parent;
		}
		return url;
	}
	
	//书苑图书列表xml转ebook对象
	private void xmlToEbooks(String url) throws Exception{
		Document doc = XmlUtil.getDataFromSolr(url);
		Attribute  attrCode = (Attribute) doc.selectSingleNode("//Return/@Code");
		if(attrCode!=null&&successCode.equals(attrCode.getText())){
		   //System.out.println(attrCode.getText());
			Node  totalCountNode = doc.selectSingleNode("/Return/TotalCount");
			List<Node> Records = doc.selectNodes("/Return/Records/Record");
			totalCount = totalCountNode.getText();
		    ebooks = new ArrayList<Ebook>();
			for (Node node:Records) {
			   Ebook ebook = (Ebook)ServerModelTrandsform.xmlToObj(node.asXML(),"Record", Ebook.class);
			   ebooks.add(ebook);
				}
		 }else{
			 throw new Exception(doc.toString());
		 }
	}
	
	private String makeCommendlistUrl(){
		validGetPage();
		String baseUrlType = "mobile.mvc";
		String api ="commendlist";
		String url = ShuyuanUtil.APABI_URL+"/"+orgid+"/"+baseUrlType+"?" + "api="+api + "&page=" + page + "&pagesize="+ pagesize;
		//String url = "http://www.apabi.com/tiyan/mobile.mvc?api=commendlist&page=1&pagesize=1";
		log.info("makeCommendlistUrl["+url+"]");
		return url;
	}
    
   private String makeBookdetailUrl(String type){
		   String baseUrlType = "mobile.mvc";
	   String api ="bookdetail";
	   String url = ShuyuanUtil.APABI_URL+"/"+orgid+"/"+baseUrlType+"?" + "api="+api + "&metaid=" + metaid + "&type="+ type;
	   log.info("makeBookdetailUrl["+url+"]");
	   return url;
	   //BASEURL?api=bookdetail&metaid=m.20071101-m004-w011-047&type=0
   }
   
	
	
	private String makeCfxUrl(){
		String method= "api/ebook/circulate/gettrigger";
		///BASEURL_CIRCULATE +/api/ebook/circulate/gettrigger?metaid=m.20080807-m801-w009-042&usercode=testuser001&objectid=&devicetype=2&type=borrow&orgcode=tiyan&sign=D44E62F09DBBF0E45FDFFAAF2A3777EC
		String url = ShuyuanUtil.APABI_SERVICE+"/"+method+"?metaid="+metaid+"&usercode="+userName+"&objectid="+"&devicetype=2&type=borrow&orgcode="+orgid+"&sign="+ShuyuanUtil.makeCfxSign(orgid, metaid, userName);
	   log.info("makeCfxUrl["+url+"]");
     return url;
	}
	  //根据类型组装抽图或抽流式url
    private String makeHtmlUrl(String baseUrlType,String serviceType) throws Exception{
 	   String objId = metaid + ".ft.cebx.1";
// 	   String orgId = "tiyan";
// 	   rights = "1-0_00";			//测试用
 	   this.userName= this.orgid;
 	   getOrgRights();
 	   Map<String, Object> signMap = ApiUtil.ebookSign(orgid, userName, metaid, rights);
 	   String url = "";
 	   if(serviceType.equals(SERVICE_TYPE_IMAGE)){
     	    url = ShuyuanUtil.APABI_CEBXOL+"/"+baseUrlType+"?" + "ServiceType="+serviceType + "&objID=" + objId + "&metaId=" + metaid + "&OrgId="+ orgid + "&username=" + URLEncoder.encode(userName, "UTF-8") + "&rights=" + rights + "&time=" + signMap.get("time") + "&sign=" + signMap.get("sign");

 	   }else if(serviceType.equals(SERVICE_TYPE_HTML)){
     	    url = ShuyuanUtil.APABI_CEBXOL+"/"+baseUrlType+"?" + "ServiceType="+serviceType + "&objID=" + objId + "&metaId=" + metaid + "&OrgId="+ orgid + "&width=" + width + "&height=" + height + "&pageid=" + page + "&username=" + URLEncoder.encode(userName, "UTF-8") + "&rights=" + rights + "&time=" + signMap.get("time") + "&sign=" + signMap.get("sign");

 	   }
 	   log.info("makeHtmlUrl["+url+"]");
 	   return url;
    }
    
    private String makeCssUrl() throws Exception{
 	   String baseUrlType = "command/htmlpage.ashx";
 	   String serviceType =SERVICE_TYPE_CSS;
 	   String objId = metaid + ".ft.cebx.1";
// 	   String orgId = "tiyan";
 	   String url = ShuyuanUtil.APABI_CEBXOL+"/"+baseUrlType+"?" + "ServiceType="+serviceType + "&objID=" + objId + "&metaId=" + metaid + "&OrgId="+ orgid;
 	   log.info("makeCssUrl["+url+"]");
 	   return url;
    }
    
    //书目检索url
    //ASEURL?api=metadatasearch&type=0&key=经济&order=1&ordertype=0&page=2&pagesize=20&digitresgroupid=12
    private String makeMetadatasearchUrl(){
    	validGetPage();
    	String api="metadatasearch";
    	String url =ShuyuanUtil.APABI_URL+"/"+orgid+"/"+ShuyuanUtil.APABI_TYPE_MOBILE+"?api="+api;
    	if(StringUtils.isNotBlank(key)){
    		url +="&&key="+key;;
    	}
    	url+="&&page="+page+"&&pagesize="+pagesize;
    	return url;
    }
	//===========================================================
	
	
	public void getCfx(){
		makeCfxUrl();
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMetaid() {
		return metaid;
	}

	public void setMetaid(String metaid) {
		this.metaid = metaid;
	}

	public String getRexml() {
		return rexml;
	}

	public void setRexml(String rexml) {
		this.rexml = rexml;
	}
	public List<Map<String, String>> getCatalogRowMapList() {
		return catalogRowMapList;
	}

	public void setCatalogRowMapList(List<Map<String, String>> catalogRowMapList) {
		this.catalogRowMapList = catalogRowMapList;
	}

	public List<Ebook> getEbooks() {
		return ebooks;
	}

	public void setEbooks(List<Ebook> ebooks) {
		this.ebooks = ebooks;
	}


	public Ebook getEbook() {
		return ebook;
	}

	public void setEbook(Ebook ebook) {
		this.ebook = ebook;
	}
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPagesize() {
		return pagesize;
	}

	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public String getOnlineurl() {
			return onlineurl;
	}
	public void setOnlineurl(String onlineurl) {
		this.onlineurl = onlineurl;
	}

	public String getPageid() {
		return pageid;
	}
	public void setPageid(String pageid) {
		this.pageid = pageid;
	}
    public String getPageTotalCount() {
			return PageTotalCount;
		}
	public void setPageTotalCount(String pageTotalCount) {
		PageTotalCount = pageTotalCount;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getRights() {
		return rights;
	}
	public void setRights(String rights) {
		this.rights = rights;
	}
	public String getOrgid() {
		return orgid;
	}
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	
	private String getOrgRights(){
		if(orgid.equals("swhy")){
   	    	rights="1-0_00";
   	    }else{
   	    	rights="1-17_00";
   	    }
		return rights;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getDigitresgroupid() {
		return digitresgroupid;
	}
	public void setDigitresgroupid(String digitresgroupid) {
		this.digitresgroupid = digitresgroupid;
	}
	public List<Category> getCategorys() {
		return categorys;
	}
	public void setCategorys(List<Category> categorys) {
		this.categorys = categorys;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
