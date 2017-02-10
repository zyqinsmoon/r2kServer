package com.apabi.r2k.admin.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.OrgPaperOrderDao;
import com.apabi.r2k.admin.model.OrgPaperOrder;
import com.apabi.r2k.common.base.BaseDaoImpl;
@Repository("orgPaperOrderDao")
public class OrgPaperOrderDaoImpl extends BaseDaoImpl<OrgPaperOrder, Serializable> implements OrgPaperOrderDao {
	
	private Logger log = LoggerFactory.getLogger(OrgPaperOrderDaoImpl.class);


	public boolean saveOrgPaperOrder(OrgPaperOrder orgPaperOrder) throws Exception {
		boolean flag = false;
		int num = baseDao.insert(getStatement("insert"), orgPaperOrder);
		if(num > 0){
			flag = true;
		}
		return flag;
	}

	public boolean deleteOrgPaperOrder(Map<String, Object> map)
			throws Exception {
		boolean flag = false;
		int num = baseDao.delete(getStatement("update"), map);
		if(num > 0){
			flag = true;
		}
		return flag;
	}
	public boolean updateStatus(Map<String,Object> map) throws Exception{
		boolean flag = false;
		int num = baseDao.update(getStatement("updateStatus"), map);
		if(num > 0){
			flag = true;
		}
		return flag;
	}

	public OrgPaperOrder getOrgPaperOrderByOrderId(Map<String, Object> map)
			throws Exception {
		return baseDao.selectOne(getStatement("getOrderByOrderId"), map);
	}
	
	public List<OrgPaperOrder> getOrgPaperOrderByStatus(Map<String,Object> map) throws Exception{
		return baseDao.selectList(getStatement("getOrgPaperOrderByStatus"), map);
	}
	
	public OrgPaperOrder getOrgPaperOrderByOrgIdAndCrtDate(Map<String,Object> map) throws Exception{
		return baseDao.selectOne(getStatement("getOrderByOrgIdAndCrtDate"), map);
	}
	
	public List<OrgPaperOrder> getAllOrgPaperOrder() throws Exception{
		return baseDao.selectList(getStatement("getAllOrder"));
	}
	
    //分页查询
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest) throws Exception {
		return basePageQuery(getStatement(statementName), pageRequest);
	}

	// 根据订单状态查询所有报纸订单
	public List<OrgPaperOrder> getAllAuthOrder(Map<String, Object> map) throws Exception{
		return baseDao.selectList(getStatement("getAllAuthOrder"), map);	
	}
	
	//通过机构orgId修改订单状态
	public int updateStatusByOrgId(Map<String,Object> map) throws Exception{
		return baseDao.update(getStatement("updateStatusByOrgId"), map);
	}
}
