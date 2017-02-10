package com.apabi.r2k.admin.service;

import java.util.List;

import org.dom4j.Node;

import com.apabi.r2k.admin.model.PaperSub;

/**
 * 报纸订阅service
 * @author l.wen
 */
public interface PaperSubService {

	//获取所有报纸订阅
	public String getPaperSubs(String orgId, String userId) throws Exception;
	
	//保存报纸订阅
	public int saveOrDelPaperSubs(List<Node> nodes, String orgId, String userId, String method) throws Exception;
	
	//根据报纸id、期次出版日期获取订阅列表
	public List<PaperSub> findByPaperAndDate(String paperId, String publishDate) throws Exception;
}
