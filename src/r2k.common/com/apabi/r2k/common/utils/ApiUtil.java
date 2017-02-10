package com.apabi.r2k.common.utils;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class ApiUtil {

	public final static int DEFAULT_VALID_DAYS = 1;		//默认有效签名天数
	
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
	
	private static String getTime() {
		Date date = DateUtil.getDateAfterDays(new Date(), DEFAULT_VALID_DAYS);
		String time= DateUtil.formatDate(date);
		return time;
	}
	
}
