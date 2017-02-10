package com.apabi.r2k.paper.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.BaseDaoImpl;
import com.apabi.r2k.paper.dao.PaperPriorityDao;
import com.apabi.r2k.paper.model.PaperPriority;

@Repository("paperPriorityDao")
public class PaperPriorityDaoImpl extends BaseDaoImpl<PaperPriority, Serializable> implements PaperPriorityDao {

	private Logger log = LoggerFactory.getLogger(PaperPriorityDaoImpl.class);
	
	//入库
	public int save(PaperPriority paperPriority) throws Exception {
		return baseDao.insert(getStatement("insert"), paperPriority);
	}
	
	//更新
	public int update(PaperPriority paperPriority) throws Exception {
		return baseDao.update(getStatement("update"),paperPriority);
	}
	
	//分页查询
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception {
		return basePageQuery(getStatement(statementName), pageRequest, countQuery);
	}

	@Override
	public PaperPriority getByPaperId(String paperId) throws Exception {
		Map params = new HashMap();
		params.put("paperId", paperId);
		return baseDao.selectOne(getStatement("getByPaperId"), params);
	} 
}
