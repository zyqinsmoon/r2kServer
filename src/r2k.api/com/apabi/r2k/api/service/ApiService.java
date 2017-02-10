package com.apabi.r2k.api.service;

import java.util.List;
import java.util.Map;

import org.dom4j.Node;

import com.apabi.r2k.security.model.AuthOrg;

public interface ApiService {

	public int saveOrDelPaperSubs(List<Node> nodes, String orgId, String userId, String method) throws Exception;
	
	public String getPaperSubs(String orgId, String userId) throws Exception;
	
	public int saveSuggest(List<Node> nodes, String orgId, String userId) throws Exception;
	
	public String fuzzySearchOrg(String name_startsWith) throws Exception;
	
	/**
	 * 分发消息前检查源数据是否有cebx
	 */
	public boolean checkHasCebx(String periodId) throws Exception;
	
	public void createPaperMsg(String paperId, String periodId, String publishDate) throws Exception;
	
	public int getEnumId(String deviceType) throws Exception;
	
	
	public String getMenuRoot();
	
	public void sendMail(String emailAdress,Map<String,Object> mailContent) throws Exception;
	
	public  String getFilePath(AuthOrg authOrg,String devicetype,String deviceid,String filetype,String filename) throws Exception;
	
	/**
	 * 登录
	 */
	public boolean login(String orgId, String userName, String password, String userAgent) throws Exception;
}
