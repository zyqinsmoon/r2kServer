package com.apabi.r2k.paper.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.BaseDaoImpl;
import com.apabi.r2k.paper.dao.SyncMessageDao;
import com.apabi.r2k.paper.model.SyncMessage;

@Repository("syncMessageDao")
public class SyncMessageDaoImpl extends BaseDaoImpl<SyncMessage, Serializable> implements SyncMessageDao {

	private Logger log = LoggerFactory.getLogger(SyncMessageDaoImpl.class);
	
	//入库
	public int save(SyncMessage syncMessage) throws Exception {
		return baseDao.insert(getStatement("insert"),syncMessage);
	}
	
	//更新
	public int update(Map syncMessage) throws Exception {
		return baseDao.update(getStatement("update"),syncMessage);
	}
	
	//分页查询
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception {
		return basePageQuery(getStatement(statementName), pageRequest, countQuery);
	}

	//根据id查询
	@Override
	public SyncMessage getById(int id) throws Exception {
		Map params = new HashMap();
		params.put("id", id);
		return baseDao.selectOne(getStatement("getById"), params);
	}

	//获取机构下的消息
	@Override
	public List<SyncMessage> getCacheMsgs(Map params) throws Exception {
		int limit = (Integer) params.get("limit");
		RowBounds rowBounds = new RowBounds(0, limit);
		return baseDao.selectList(getStatement("getCacheMsgs"), params, rowBounds);
	}

	@Override
	public void batchSaveMsgs(List<SyncMessage> syncMessages) throws Exception {
		batchSave(syncMessages);
	}

	@Override
	public void batchUpdateMsgs(List<SyncMessage> syncMessages) throws Exception {
		batchUpdate(getStatement("update"), syncMessages);
	}

	@Override
	public int updateByType(Map parameters) throws Exception {
		return baseDao.update(getStatement("updateByMsgType"), parameters);
	}

	@Override
	public int deleteMsgs(Map<String, Object> params) throws Exception {
		return baseDao.delete(getStatement("deleteMsg"), params);
	}

	@Override
	public List<SyncMessage> getDeviceMsgs(Map<String, Object> params) throws Exception {
		return baseDao.selectList(getStatement("getDeviceMsgs"), params);
	}
}
