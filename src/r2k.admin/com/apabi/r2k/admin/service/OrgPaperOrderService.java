package com.apabi.r2k.admin.service;

import java.util.Date;
import java.util.List;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.OrgPaperOrder;

public interface OrgPaperOrderService {
	public boolean saveOrgPaperOrder(OrgPaperOrder orgPaperOrder) throws Exception;
	
	public boolean updateStatus(int orderId, String status) throws Exception;
	
	public boolean deleteOrgPaperOrder(int orderId, String status) throws Exception;
	
	public OrgPaperOrder getOrgPaperOrderByOrderID(int orderId) throws Exception;
	
	public OrgPaperOrder getOrgPaperOrderByOrgIdAndCrtDate(String orgId, Date crtDate) throws Exception;
	
	public List<OrgPaperOrder> getOrgPaperOrderByStatus(String orgId, String status) throws Exception;
	
	public List<OrgPaperOrder> getAllOrgPaperOrder() throws Exception;
	
	public Page<?> pageQuery(PageRequest<?> pageRequest) throws Exception;
}
