package com.apabi.r2k.admin.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.PaperSubDao;
import com.apabi.r2k.admin.model.PaperSub;
import com.apabi.r2k.common.base.BaseDaoImpl;

@Repository("paperSubDao")
public class PaperSubDaoImpl extends BaseDaoImpl<PaperSub, Serializable> implements PaperSubDao {

	@Override
	public int batchSavePaperSub(List<PaperSub> paperSubs) throws Exception {
		Map params = new HashMap();
		params.put("paperSubs", paperSubs);
		return baseDao.insert(getStatement("batchInsert"), params);
	}

	@Override
	public int batchDeletePaperSub(Map params) throws Exception {
		return baseDao.delete(getStatement("batchDelete"), params);
	}

	@Override
	public Page<?> pageQueryPaperSub(String statementName, PageRequest<?> pageRequest) throws Exception {
		return basePageQuery(getStatement(statementName), pageRequest);
	}

	@Override
	public List<PaperSub> getPaperSubs(Map params) throws Exception {
		return baseDao.selectList(getStatement("pageSelect"), params);
	}

	@Override
	public List<PaperSub> getByPaperAndDate(Map params) throws Exception {
		return baseDao.selectList(getStatement("getByPaperAndDate"), params);
	}

}
