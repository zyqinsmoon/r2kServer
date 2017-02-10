package com.apabi.r2k.paper.dao;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.paper.model.SyncMessageBody;

public interface SyncMessageBodyDao {
	
	/**
	 * 入库
	 */
	public int save(SyncMessageBody syncMessageBody) throws Exception;
	
	/**
	 * 更新
	 */
	public int update(SyncMessageBody syncMessageBody) throws Exception;
	
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception; 
}
