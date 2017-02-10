package com.apabi.r2k.paper.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.paper.dao.PaperPriorityDao;
import com.apabi.r2k.paper.model.PaperPriority;
import com.apabi.r2k.paper.service.PaperPriorityService;

@Service("paperPriorityService")
public class PaperPriorityServiceImpl implements PaperPriorityService {

	private Logger log = LoggerFactory.getLogger(PaperPriorityServiceImpl.class);
	
	public static final String PAGE_QUERY_STATEMENT = ".pageSelect";
	
	@Resource(name="paperPriorityDao")
	private PaperPriorityDao paperPriorityDao;
	
	public void setPaperPriorityDao(PaperPriorityDao paperPriorityDao){
		this.paperPriorityDao = paperPriorityDao;
	}
	
	public PaperPriorityDao getPaperPriorityDao(){
		return paperPriorityDao;
	}
	
	//入库
	@Transactional(propagation = Propagation.REQUIRED)
	public int save(PaperPriority paperPriority) throws Exception {
		return paperPriorityDao.save(paperPriority);
	}
	
	//更新
	@Transactional(propagation = Propagation.REQUIRED)
	public int update(PaperPriority paperPriority) throws Exception {
		return paperPriorityDao.update(paperPriority);
	}
	
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception {
		return paperPriorityDao.pageQuery(PAGE_QUERY_STATEMENT, pageRequest, countQuery);
	} 
}
