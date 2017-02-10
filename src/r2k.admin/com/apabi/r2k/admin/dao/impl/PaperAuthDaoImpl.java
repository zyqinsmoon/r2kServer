package com.apabi.r2k.admin.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.PaperAuthDao;
import com.apabi.r2k.admin.model.PaperAuth;
import com.apabi.r2k.common.base.BaseDaoImpl;

@Repository("paperAuthDao")
public class PaperAuthDaoImpl extends BaseDaoImpl<PaperAuth, Serializable> implements PaperAuthDao{
	private Logger log = LoggerFactory.getLogger(PaperAuthDaoImpl.class);
	@Transactional
	public void addPaperAuth(List<PaperAuth> list) throws Exception{
		for(PaperAuth paperAuth : list){
			baseDao.insert(getStatement("insert"), paperAuth);
		}
	}
	
	@Override
	@Transactional
	public List<PaperAuth> getPaperAuth(Map<String,Object> paramMap) throws Exception{
		return baseDao.selectList(getStatement("selectList"), paramMap);
	}
	

	public List<PaperAuth> getPaperAuthById(Map<String,Object> paramMap) throws Exception{
		return baseDao.selectList(getStatement("selectOne"), paramMap);
	}
	
	public void deletePaperAuthByorderId(Map<String,Object> paramMap) throws Exception{
		baseDao.update(getStatement("updateorderId"), paramMap);
	}

	public void deletePaperAuthById(Map<String,Object> paramMap) throws Exception{
		baseDao.delete(getStatement("deleteById"), paramMap);
	}
	 public Page<?> orderDetailQuery(String statementName, PageRequest<?> pageRequest,String countStatementName) throws Exception{
		 return basePageQuery(getStatement(statementName), pageRequest,getStatement(countStatementName));
	 }
	//分页查询
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest) throws Exception{
		return basePageQuery(getStatement(statementName), pageRequest);
	}

	//更新报纸授权
	public boolean updatePaperAuth(PaperAuth paperAuth) throws Exception {
		boolean flag = false;
		int	num = baseDao.update(getStatement("updateByPaperAuthId"), paperAuth);
		if(num > 0){
			flag = true;
		}
		return flag;
	}
	public List<PaperAuth> getPaperAuthByorderId(String orgId, int orderId) throws Exception{
		Map param = new HashMap<String, String>();
		param.put("orgId", orgId);
		param.put("orderId", orderId);
		return baseDao.selectList(getStatement("getPaperAuthByorderId"), param);


	}

	//查询报纸授权对象
	public PaperAuth getPaperAuthObject(String orgId, String paperId) throws Exception{
		Map param = new HashMap<String, String>();
		param.put("orgId", orgId);
		param.put("paperId", paperId);
		return baseDao.selectOne(getStatement("getPaperAuthObject"),param);
	}
	
	//查询机构下状态为已授权的报纸
	public List<PaperAuth> getByStatus(Map<String,Object> paramMap)throws Exception{
		return baseDao.selectList(getStatement("getByStatus"),paramMap);
	}
	//更新报纸授权状态
	public int updatePaperAuthStatus(Map<String,Object> paramMap)throws Exception{
		return baseDao.update(getStatement("updateorderId"),paramMap);
	}
	
	//通过机构orgId更新报纸授权状态
	public int updateByOrgId(Map<String,Object> paramMap)throws Exception{
		return baseDao.update(getStatement("updateByOrgId"),paramMap);
	}

	@Override
	public List<PaperAuth> batchSavePaperAuth(List<PaperAuth> paperAuths) throws Exception {
		return batchSave(getStatement("insert"), paperAuths);
	}

	@Override
	public List<PaperAuth> batchUpdatePaperAuth(List<PaperAuth> paperAuths) throws Exception {
		return batchUpdate(getStatement("updateByPaperAuthId"), paperAuths);
	}

	@Override
	public List<PaperAuth> getByPaperAndDate(Map<String, Object> params) throws Exception {
		return baseDao.selectList(getStatement("getByPaperAndDate"), params);
	}
}
