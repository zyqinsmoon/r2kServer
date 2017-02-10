package com.apabi.r2k.common.utils;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShuyuanUtil {
	final static Logger shuyuanUtilLog = LoggerFactory.getLogger(ShuyuanUtil.class);

	public final static int DEFAULT_VALID_DAYS = 1;		//默认有效签名天数
	
	public final static String APABI_URL="http://www.apabi.com";//获取元数据基地址[元数据、在线阅读]	
	public final static String APABI_SERVICE="http://service.apabi.com";//借阅基地址
	public final static String APABI_CEBXOL = "http://cebxol.apabi.com";	//在线借阅基地址
	public final static String APABI_TYPE_USPSERVICE = "uspservice.mvc";
	public final static String APABI_TYPE_MOBILE = "mobile.mvc";
	
	public final static String LOGIN_TYPE = "1";	//登录类型
	
	public final static String successCode = "0";
	public final static String userRepeatCode = "24";
	
	//图书签名
	public static Map<String, Object> ebookSign(String orgId, String userName, String metaId, String rights) throws Exception{
		if (orgId != null && metaId != null && userName != null && rights != null) {
			String sign = "";
			String rightKey = PropertiesUtil.get("rightKey");
			String time = getTime();
			sign = orgId + "_"+ userName +"_" + metaId + "_"+ rights + "_" + time + "_" + rightKey;
			sign = new MD5().md5s(sign).toUpperCase();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("sign", sign);
			model.put("time", URLEncoder.encode(time, "UTF-8"));
			return model;
		}
		return null;
	}
	
	//图片签名
	public static Map<String, Object> pictureSign(String uid, String metaid) throws Exception{
		if(StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(metaid)){
			String sign = "";
			String rightKey = PropertiesUtil.get("pictureKey");
			String time = getTime();
			String source = uid + "_" + metaid + "_" + time + "_" + rightKey;
			sign = new MD5().md5s(source).toUpperCase();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("sign", sign);
			model.put("time", URLEncoder.encode(time, "UTF-8"));
			return model;
		}
		return null;
	}
	
	public static String makeCfxSign(String orgid,String metaid,String userName){
		String key = "apabi";
		String devicetype = "2";
		//md5(metaid+objected+orgcode+devicetype+usercode+ DateTime.Now.Date.ToString("yyyyMMdd") +秘钥)，
		//秘钥是"apabi",例如：
		//m.20080807-m801-w009-042tiyan2testuser00120140513apabi
		String sign = metaid+orgid+devicetype+userName+DateUtil.currentDate("yyyyMMdd")+key;
      return DigestUtils.md5ToHex(sign);
	}
	
	
	//书苑登录
	public static InputStream shuYuanLogin(String userName, String password,String orgId) throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		params.put("api", "signin");
		params.put("uid", userName);
		params.put("pwd", password);
		params.put("type", LOGIN_TYPE);
		//构造书苑登录url
		String url = APABI_URL+"/" + orgId + "/"+APABI_TYPE_USPSERVICE +"?" + HttpUtils.joinUrlParam(params);
		long startTime = System.currentTimeMillis();
		System.out.println("shuYuanLogin:[url#"+url+"]:书苑登录url");
		HttpEntity entity = HttpUtils.httpGet(url);
		System.out.println("shuYuanLogin:书苑登录完成,耗时:"+(System.currentTimeMillis() - startTime)+"毫秒");
		if(entity != null){
			return entity.getContent();
		}
		return null;
	}
	
	//书苑注销
	public static InputStream shuYuanLogout(String orgId, String token) throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		String url = APABI_URL+"/" + orgId+ "/"+APABI_TYPE_USPSERVICE +"?" + HttpUtils.joinUrlParam(params);
		params.put("api", "signout");
		params.put("token", token);
		System.out.println("shuYuanLogout:[url#"+url+"]:书苑注销url");
		HttpEntity entity = HttpUtils.httpGet(url);
		if(entity != null){
			return entity.getContent();
		}
		return null;
	}
	
	//获取书苑返回结果code
	public static String getShuYuanCode(InputStream in) throws Exception{
		if(in == null){
			return null;
		}
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(in);
		Node node = doc.selectSingleNode("//Return/@code");		//获取登录成功后的token
		if(node == null){
			return null;
		}else{
			return node.getText();
		}
	}
	
	//获取书苑登录返回的token
	public static String getShuYuanToken(InputStream in) throws Exception{
		if(in == null){
			return null;
		}
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(in);
		Node node = doc.selectSingleNode("//token");		//获取登录成功后的token
		if(node == null){
			return null;
		}else{
			return node.getText();
		}
	}
	
	//获取书苑返回结果
	public static ShuYuanResult getShuYuanResult(InputStream in) throws Exception{
		if(in == null){
			return null;
		}
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(in);
		ShuYuanResult syResult = (ShuYuanResult) ClientModelTrandsform.xmlToObject(doc.asXML());
		return syResult;
	}
	
	//书苑登录并返回结果
	public static ShuYuanResult getResultAfterLogin(String userName, String password, String orgId) throws Exception{
		InputStream in = shuYuanLogin(userName, password, orgId);
		ShuYuanResult syResult = getShuYuanResult(in);
		return syResult;
	}
	
	//书苑登录并且获取token
	public static String getShuYuanToken(String userName, String password, String orgId) throws Exception{
		InputStream in = shuYuanLogin(userName, password, orgId);
		ShuYuanResult syResult = getShuYuanResult(in);
		String token = "";
		if(syResult == null){
			return token;
		}
		token = syResult.getToken();
		return token;
	}
	
    //登录获取token
    public String getToken(String orgid,String uid,String pwd){
    	String url = getTokenUrl(orgid, uid,pwd);
    	 String token = "";
    	try {
			Document doc = XmlUtil.getDataFromSolr(url);
			Attribute  attrCode = (Attribute) doc.selectSingleNode("//Return/@Code");
   			if(attrCode!=null&&successCode.equals(attrCode.getText())){
 				 Node TokenNode = doc.selectSingleNode("/Return/token");
 				 token = TokenNode.getText();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
    }
	//书苑注册用户
    public static HttpEntity shuyuanSignup(String orgid,String uid,String pwd,String email,String name) throws Exception {
        //待请求参数数组
        Map<String, String> sPara = new HashMap<String, String>();
        sPara.put("uid", uid);
        sPara.put("pwd",pwd);
        sPara.put("email", email);
        sPara.put("name", name);
        String url = APABI_URL+"/"+orgid+"/"+APABI_TYPE_MOBILE+"?api=signup";
		HttpEntity entity = HttpUtils.httpPost(url, sPara);
		return entity;
    }
    
    public static void main(String[] args) {
    	
    	/*String orgid= "tiyan";
    	String uid="uid1111";
    	String pwd  = "pwd11";
    	 Map<String, String> sPara = new HashMap<String, String>();
        BASE64Encoder base = new BASE64Encoder();
         sPara.put("uid", uid);
         sPara.put("pwd",base.encode(pwd.getBytes()));
         sPara.put("email", "");
         sPara.put("name", "");
         String url = APABI_URL+"/"+orgid+"/"+APABI_TYPE_MOBILE+"?api=signup";
    	try {
			HttpEntity entity = HttpUtils.httpPost(url, sPara);
			String xml = XmlUtil.getXmlFromInputStream(entity.getContent());
			System.out.println(xml);
			Reader reader = new InputStreamReader(entity.getContent());
			char[] buffer = new char[1024];
			int len = 0;
			while((len = reader.read(buffer)) != -1){
				String str = new String(buffer);
				System.out.println(str);
			}
			reader.close();
			
		} catch (Exception e) {
			shuyuanUtilLog.error(e.getMessage(),e);
			
		}*/
	}
    
    public static String randomUid(){
    	String 	baseString = "1234567890abcdefghigklmnopqrstuvwxyz";
    		//以当前系统时间作为随机种子
    		Random random = new Random();
    		StringBuilder randomString = new StringBuilder("r_");
    		int length = baseString.length();
    		for(int i = 0; i < 10; i++){
    			int index = random.nextInt(length);
    			char nextChar = baseString.charAt(index);
    			randomString.append(nextChar);
    		}
    		return randomString.toString();
    }

	//获取书苑tokenurl
	private String getTokenUrl(String orgid,String uid,String pwd){
        String api = "signin";
    	//http://www.apabi.com/docin/uspservice.mvc
    	//BASEURL?api=signin&uid=zhangsan&pwd=MTIzNDU2
    	String url = ShuyuanUtil.APABI_URL+"/"+orgid+"/"+APABI_TYPE_USPSERVICE+"?"+"api="+api+"&uid="+uid+"&pwd="+Base64.encodeBytes(pwd.getBytes());
		shuyuanUtilLog.info("getTokenUrl["+url+"]");
		return url;
    }
	
	private static String getTime() {
		Date date = DateUtil.getDateAfterDays(new Date(), DEFAULT_VALID_DAYS);
		String time= DateUtil.formatDate(date);
		return time;
	}
	
	
}
