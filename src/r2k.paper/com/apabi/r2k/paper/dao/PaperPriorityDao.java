package com.apabi.r2k.paper.dao;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.paper.model.PaperPriority;

public interface PaperPriorityDao {
	
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
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception;
	
	/**
	 * 根据报纸id查询优先级
	 */
	public PaperPriority getByPaperId(String paperId) throws Exception;
}
