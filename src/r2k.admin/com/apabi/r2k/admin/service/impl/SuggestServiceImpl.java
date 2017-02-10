package com.apabi.r2k.admin.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.apabi.r2k.admin.dao.SuggestDao;
import com.apabi.r2k.admin.model.Suggest;
import com.apabi.r2k.admin.service.SuggestService;
import com.apabi.r2k.common.base.ServerModelTrandsform;

@Service("suggestService")
public class SuggestServiceImpl implements SuggestService {

	private Logger log = LoggerFactory.getLogger(SuggestServiceImpl.class);
	@Resource(name="suggestDao")
	private SuggestDao suggestDao;
	
	@Override
	public int saveSuggest(List<Node> nodes, String orgId, String userId) throws Exception {
		Suggest suggest = (Suggest) ServerModelTrandsform.xmlToObject(nodes.get(0));
		suggest = createSuggest(suggest,orgId,userId);
		log.info("suggest:[userId#"+userId+",orgId#"+orgId+",type#"+suggest.getType()+"]:保存用户反馈");
		int result = suggestDao.saveSuggest(suggest);
		log.info("suggest操作成功执行"+result+"条");
		return result;
	}

	private Suggest createSuggest(Suggest suggest, String orgId, String userId) throws Exception{
		Date now = new Date();
		suggest.setStatus(0);
		suggest.setUserId(userId);
		suggest.setOrgId(orgId);
		suggest.setCrtDate(now);
		suggest.setLastUpdate(now);
		return suggest;
	}
}
