package com.apabi.r2k.admin.dao;

import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.PaperSub;

public interface PaperSubDao {

	//批量保存报纸订阅信息
	public int batchSavePaperSub(List<PaperSub> paperSubs) throws Exception;
	
	//批量删除报纸订阅信息
	public int batchDeletePaperSub(Map params) throws Exception;
	
	//分页查询订阅信息
	public Page<?> pageQueryPaperSub(String statementName, PageRequest<?> pageRequest) throws Exception;
	
	//查询订阅信息
	public List<PaperSub> getPaperSubs(Map params) throws Exception;
	
	//根据报纸id及期次出版时间查询
	public List<PaperSub> getByPaperAndDate(Map params) throws Exception;
}
