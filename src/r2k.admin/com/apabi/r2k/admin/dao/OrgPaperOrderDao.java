package com.apabi.r2k.admin.dao;

import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.OrgPaperOrder;

public interface OrgPaperOrderDao {
	public boolean saveOrgPaperOrder(OrgPaperOrder orgPaperOrder) throws Exception;
	
	public boolean updateStatus(Map<String,Object> map) throws Exception;
	
	public boolean deleteOrgPaperOrder(Map<String,Object> map) throws Exception;
	
	public OrgPaperOrder getOrgPaperOrderByOrderId(Map<String,Object> map) throws Exception;
	
	public List<OrgPaperOrder> getOrgPaperOrderByStatus(Map<String,Object> map) throws Exception;
	
	public OrgPaperOrder getOrgPaperOrderByOrgIdAndCrtDate(Map<String,Object> map) throws Exception;
	
	public List<OrgPaperOrder> getAllOrgPaperOrder() throws Exception;
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest) throws Exception;
	/**
	 * 根据订单状态查询所有报纸订单
	 */
	public List<OrgPaperOrder> getAllAuthOrder(Map<String, Object> map) throws Exception;
	//通过机构orgId修改订单状态
	public int updateStatusByOrgId(Map<String,Object> map) throws Exception;
}
