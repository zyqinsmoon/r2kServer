package com.apabi.r2k.paper.dao.impl;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.BaseDaoImpl;
import com.apabi.r2k.paper.dao.SyncMessageBodyDao;
import com.apabi.r2k.paper.model.SyncMessageBody;

@Repository("syncMessageBodyDao")
public class SyncMessageBodyDaoImpl extends BaseDaoImpl<SyncMessageBody, Serializable> implements SyncMessageBodyDao {

	private Logger log = LoggerFactory.getLogger(SyncMessageBodyDaoImpl.class);

	//入库
	public int save(SyncMessageBody syncMessageBody) throws Exception {
		return baseDao.insert(getStatement("insert"), syncMessageBody);
	}
	
	//更新
	public int update(SyncMessageBody syncMessageBody) throws Exception {
		return baseDao.update(getStatement("update"),syncMessageBody);
	}
	
	//分页查询
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception {
		return basePageQuery(getStatement(statementName), pageRequest, countQuery);
	} 
}
