package com.apabi.r2k.common.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GlobalConstant {

	public static String SERVERURL;
	public static String BASEURL;
	public static String TOPICURL_UPDATE;
	public static String TOPICURL_QUERY;
	public static String PAPER_QUERY;
	public static String PAPER_UPDATE;
	public static String DEFALUT_USERNAME;
	public static String DEFALUT_USERPWD;
	public static String MENU_ICONS;
	/** 公共模板路径：/template/public/ */
	public static String TEMPLATE_PUBLIC_PATH;
	/** 机构模板路径：/template/org/ */
	public static String TEMPLATE_ORG_PATH;
	/** 模板上传临时路径：/template/temp/ */
	public static String TEMPLATE_TEMP_PATH;
	/** 项目文件根路径：r2kFile */
	public static String PROJECT_FILE_PATH;
	/** r2k文件根路径*/
	public static String R2K_PATH;
	/** 触摸屏版本存放路径 */
	public static String VERSION_ANDROID; 
	
	//普通服务的过期时间
	public static SimpleDateFormat sdf;
	public static Date GENERAL_SERVICE_EXPIRES;
	/** 判断设备是否在线(心跳超时时间单位为分钟) */
	public static String HEARTBEAT_TIMEOUT;
	static{
		try {
			SERVERURL = PropertiesUtil.getValue("server.url");
			BASEURL = PropertiesUtil.getValue("solr.baseurl");
			TOPICURL_UPDATE = PropertiesUtil.getValue("topic.update.url");
			TOPICURL_QUERY = PropertiesUtil.getValue("topic.query.url");
			PAPER_QUERY = PropertiesUtil.getValue("newspaper.query.url");
			PAPER_UPDATE = PropertiesUtil.getValue("newspaper.update.url");
			
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			GENERAL_SERVICE_EXPIRES = sdf.parse("9999-12-31 12:00:00");
			
			DEFALUT_USERNAME = PropertiesUtil.getValue("default_username");
			DEFALUT_USERPWD = PropertiesUtil.getValue("default_userpwd");
			MENU_ICONS = PropertiesUtil.getValue("r2k.menu.icon");

			TEMPLATE_PUBLIC_PATH = PropertiesUtil.getValue("path.info.tpl.public");
			TEMPLATE_ORG_PATH = PropertiesUtil.getValue("path.info.tpl.org");
			TEMPLATE_TEMP_PATH = PropertiesUtil.getValue("path.info.tpl.upload");
			PROJECT_FILE_PATH = PropertiesUtil.getValue("base.r2kfile");
			R2K_PATH = PropertiesUtil.get("base.r2k");
			HEARTBEAT_TIMEOUT = PropertiesUtil.get("heartbeat.timeout.interval");
			VERSION_ANDROID = PropertiesUtil.get("path.soft.version.android");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final String URL_TOPIC_QUERY 			 = BASEURL + TOPICURL_QUERY;
	public static final String URL_TOPIC_SAVE			 = BASEURL + TOPICURL_UPDATE + "?api=putTopic";					//创建专题
	public static final String URL_TOPIC_GET 			 = URL_TOPIC_QUERY  + "?api=getTopics";				//查看专题
	public static final String URL_TOPIC_UPDATE 		 = BASEURL + TOPICURL_UPDATE + "?api=postTopics";				//修改专题
	public static final String URL_TOPIC_DELETE 		 = BASEURL + TOPICURL_UPDATE + "?api=deleteTopics";				//删除专题
	public static final String URL_TOPIC_CONTENT_GET 	 = URL_TOPIC_QUERY  + "?api=getTopicContent";			//生成专题内容
	public static final String URL_TOPIC_CONTENT_SAVE 	 = BASEURL + TOPICURL_UPDATE + "?api=putTopicContent";			//获取专题内容
	public static final String URL_TOPIC_CONTENT_UPDATE	 = BASEURL + TOPICURL_UPDATE + "?api=postTopicContent";			//修改专题内容
	public static final String URL_TOPIC_CONTENT_DELETE  = BASEURL + TOPICURL_UPDATE + "?api=deleteTopicContent";		//删除专题内容
	public static final String URL_TOPIC_AUTO 			 = BASEURL + TOPICURL_UPDATE + "?api=autoCreate";				//自动生成专题
	public static final String URL_TOPIC_CHECK			 = URL_TOPIC_QUERY  + "?api=checkBeforeCreate";
	public static final String URL_ARTICLES_GET		 	 = URL_TOPIC_QUERY 	 + "?api=getArticles";
	
	
	public static final String FILTER_CREATE = "?api=putFilter";		//新建filter后缀
	public static final String FILTER_UPDATE = "?api=postFilter";		//更新filter后缀
	public static final String FILTER_DELETE = "?api=deleteFilter";		//删除filter后缀
	public static final String FILTER_ALLDELETE = "?api=deleteAllFilter";//删除全部filter后缀
	public static final String FILTER_QUERY = "?api=getFilter";//查询filter后缀
	
	public static final String URL_FILTER_TOPIC_CREATE = BASEURL + TOPICURL_UPDATE + FILTER_CREATE;		//新建专题filter
	public static final String URL_FILTER_TOPIC_UPDATE = BASEURL + TOPICURL_UPDATE + FILTER_UPDATE;		//更新专题filter
	public static final String URL_FILTER_TOPIC_DELETE = BASEURL + TOPICURL_UPDATE + FILTER_DELETE;		//删除专题filter
	public static final String URL_FILTER_TOPIC_ALLDELETE = BASEURL + TOPICURL_UPDATE + FILTER_ALLDELETE;		//删除专题filter
	
	public static final String URL_FILTER_PAPER_CREATE = BASEURL + PAPER_UPDATE + FILTER_CREATE;		//新建报纸filter
	public static final String URL_FILTER_PAPER_UPDATE = BASEURL + PAPER_UPDATE + FILTER_UPDATE;		//更新报纸filter
	public static final String URL_FILTER_PAPER_DELETE = BASEURL + PAPER_UPDATE + FILTER_DELETE;		//删除报纸filter
	public static final String URL_FILTER_PAPER_QUERY = BASEURL + PAPER_QUERY + FILTER_QUERY;		//查询报纸filter
	
	public static final String URL_PAPER_QUERY = BASEURL + PAPER_QUERY;
	public static final String URL_PAPER_GET = URL_PAPER_QUERY  + "?api=getPapers";				//查看资源
	public static final String URL_PERIOD_GET = URL_PAPER_QUERY + "?api=getPeriod";				//获取期次url

	
	public static final String HTTP_DATA_TYPE_XML = "text/xml";
	public static final String HTTP_DATE_TYPE_HTML = "text/html;charset=utf-8";
	
	public static final String RESULT_FROM = "from";
	public static final String RESULT_TO = "to";
	public static final String RESULT_TOTAL = "total";
	
	public static final String RESULT_LIST = "result";
	public static final String RESULT_STATUS_SUCCESS = "1000";
	public static final String RESULT_STATUS_FAILURE = "1001";
	
	public static final String QUERY_TOPIC_NOT_PUBLISHED = "0";
	public static final String QUERY_TOPIC_PUBLISHED = "1";
	public static final String QUERY_TOPIC_DEL = "2";
	public static final String QUERY_TOPIC_PUB_NOTPUB = "3";
	
	public static final String URL_PARAM_ORG = "org";
	public static final String URL_PARAM_PUBLISHED = "published";
	public static final String URL_PARAM_ID = "id";
	public static final String URL_PARAM_TOPICTYPE = "topictype";
	public static final String URL_PARAM_QTYPE = "qtype";
	
	public static final String HTTP_TYPE_GET = "get";
	public static final String HTTP_TYPE_POST = "post";
	public static final String HTTP_TYPE_PUT = "put";
	public static final String HTTP_TYPE_DELETE = "delete";
	
	public static final String XPATH_TOPIC = "//Topic";
	public static final String XPATH_ARTICLE = "//Article";
	public static final String XPATH_RESULT = "//Error";
	
	public static final String KEY_ORGID = "orgId";
	public static final int DEFUALT_TOPIC_RESULT = 1000;
	public static final String PAGE_QUERY_STATEMENT = "pageSelect";
	
	
	public static final String STATUS_SUCCESS="1";//验证成功
	public static final String STATUS_FAILURE="0";//验证失败
	public static final String STATUS_ERROR ="-1";//验证错误
	
	public static final String SUBSCRIBE_TYPE_PAPER = "paper";
	public static final String USER_AGENT_ANDROID_PHONE = "Android-Phone";
	public static final String USER_AGENT_ANDROID_LARGE = "Android-Large";
	public static final String USER_AGENT_ANDROID_PAD = "Android-Pad";
	public static final String USER_AGENT_ANDROID_PORTRAIT = "Android-Portrait";
	public static final String USER_AGENT_IPAD = "iPad";
	public static final String USER_AGENT_IPHONE = "iPhone";
	public static final String USER_AGENT_SLAVE = "slave";
	public static final String USER_AGENT_WEIXIN = "weixin";
	public static final String USER_AGENT_ADMIN = "admin";
	public static final String WX_USER_AGENT = "MicroMessenger";
	
	
	public static final String ORGID_WEIXIN = "swhy";
	
	public static final int UPLOAD_FILE_TYPE_SUCCESS = 0;
	public static final int UPLOAD_FILE_TYPE_NOTFOUND = 1;
	public static final int UPLOAD_FILE_TYPE_TOOLARGE = 2;
	public static final int UPLOAD_FILE_TYPE_FORMATERROR = 3;
	public static final int UPLOAD_FILE_TYPE_DATAERROR = 4;
	
	public static final String[] UPLOAD_FILE_MESSAGE = new String[]{"文件检查成功","未上传任何文件","文件太大，请重新选择上传文件","文件格式不正确"};
	
	//solr专题类型：1:普通,2:高级专题请求类型
	public static final String QTYPE_GENERAL = "1";
	public static final String QTYPE_ADVANCED = "2";
	
	//是否为管理员
	public static final int IS_ADMIN = 1;
	
	//同步消息状态
	public static final int SYNC_MESSAGE_STATUS_UNSEND = 0;		//未发送
	public static final int SYNC_MESSAGE_STATUS_DEALING = 1;		//处理中
	public static final int SYNC_MESSAGE_STATUS_SUCCESS = 2;	//成功
	public static final int SYNC_MESSAGE_STATUS_FAIL = 3;		//失败
	public static final int SYNC_MESSAGE_STATUS_INVALID = 4;		//失效
	
	//消息类型
	public static final String MESSAGE_TYPE_SOLRPAPER = "solrpaper";	//solr报纸消息
	public static final String MESSAGE_TYPE_SOLRFILTER = "solrfilter";	//solr报纸消息
	public static final String MESSAGE_TYPE_PAPERREPLY = "paperreplay";	//报纸消息处理回复
	public static final String MESSAGE_TYPE_HEARTBEAT = "heartbeat";	//心跳消息
	
	//消息体类型
	public static final String MESSAGE_BODY_TYPE_PAPER = "paper";		//报纸消息
	public static final String MESSAGE_BODY_TYPE_FILTER = "filter";		//报纸消息
	
	//权限相关参数
	public static final String SECURITY_PARAM_USERID="userid";
	public static final String SECURITY_PARAM_DEVICEID="deviceid";
	public static final String SECURITY_PARAM_ORGID="orgid";
	public static final String SECURITY_PARAM_NAME="name";
	public static final String SECURITY_PARAM_TOKEN="token";
	public static final String SECURITY_PARAM_JSESSIONID="JSESSIONID";
	
	//登录url前缀
	public static final String PREFIX_LOGIN_ADMIN = "/admin/";
	public static final String PREFIX_LOGIN_APP = "/app/";
	public static final String CURRENT_ORG = "currentOrg";
	//模板类型
	public final static String TEMPLATE_HOME = "home";
	public final static String TEMPLATE_COLUMN = "column";
	public final static String TEMPLATE_ARTICLE = "article";
	public final static String TEMPLATE_PICTURE = "picture";
	public final static String TEMPLATE_PICTURE_LIST = "picturelist";
	public final static String TEMPLATE_PICTURE_GROUP = "picturegroup";
	public final static String TEMPLATE_VIDEO = "video";
	public final static String TEMPLATE_LIST = "list";
	public final static String TEMPLATE_VIDEO_LIST = "videolist";
	public final static String TEMPLATE_ARTICLE_LIST = "articlelist";
	
	//内容类型
	public final static String TYPE_WELCOME = "0";
	public final static String TYPE_ARTICLE = "1";
	public final static String TYPE_PICTURE = "2";
	public final static String TYPE_VIDEO = "3";
	public final static String TYPE_PICTURES = "4";
	public final static String TYPE_ARTICLE_COLUMNS = "11";
	public final static String TYPE_PICTURE_COLUMNS = "14";
	public final static String TYPE_VIDEO_COLUMNS = "13";
	
	/** 文件路径分隔符：'/' */
	public static String SLASH = "/";
	
	public final static String WIFI_OPEN = "0";	//wifi开启
	public final static String WIFI_CLOSE = "1";//wifi关闭
}
