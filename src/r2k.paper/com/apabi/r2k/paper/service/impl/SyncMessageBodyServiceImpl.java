package com.apabi.r2k.paper.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.paper.dao.SyncMessageBodyDao;
import com.apabi.r2k.paper.model.SyncMessageBody;
import com.apabi.r2k.paper.service.SyncMessageBodyService;

@Service("syncMessageBodyService")
public class SyncMessageBodyServiceImpl implements SyncMessageBodyService {

	private Logger log = LoggerFactory.getLogger(SyncMessageBodyServiceImpl.class);
	
	public static final String PAGE_QUERY_STATEMENT = ".pageSelect";
	
	@Resource(name="syncMessageBodyDao")
	private SyncMessageBodyDao syncMessageBodyDao;
	
	public void setSyncMessageBodyDao(SyncMessageBodyDao syncMessageBodyDao){
		this.syncMessageBodyDao = syncMessageBodyDao;
	}
	
	public SyncMessageBodyDao getSyncMessageBodyDao(){
		return syncMessageBodyDao;
	}
	
	//入库
	@Transactional(propagation = Propagation.REQUIRED)
	public int save(SyncMessageBody syncMessageBody) throws Exception {
		return syncMessageBodyDao.save(syncMessageBody);
	}
	
	//更新
	@Transactional(propagation = Propagation.REQUIRED)
	public int update(SyncMessageBody syncMessageBody) throws Exception {
		return syncMessageBodyDao.update(syncMessageBody);
	}
	
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception {
		return syncMessageBodyDao.pageQuery(PAGE_QUERY_STATEMENT, pageRequest, countQuery);
	} 
}
