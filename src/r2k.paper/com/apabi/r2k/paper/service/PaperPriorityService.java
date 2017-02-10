package com.apabi.r2k.paper.service;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.paper.model.PaperPriority;

public interface PaperPriorityService {

	/**
	 * 入库
	 */
	public int save(PaperPriority paperPriority) throws Exception;
	
	/**
	 * 更新
	 */
	public int update(PaperPriority paperPriority) throws Exception;

	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception; 
}
