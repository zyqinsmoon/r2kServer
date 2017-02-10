package com.apabi.r2k.admin.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.OrgPaperOrderDao;
import com.apabi.r2k.admin.model.OrgPaperOrder;
import com.apabi.r2k.admin.service.OrgPaperOrderService;
import com.apabi.r2k.common.utils.GlobalConstant;
@Service("orgPaperOrderService")
public class OrgPaperOrderServiceImpl implements OrgPaperOrderService {
	private Logger log = LoggerFactory.getLogger(OrgPaperOrderServiceImpl.class);
	
	public static final String PAGE_QUERY_STATEMENT = "pageSelect";
	
	@Resource(name="orgPaperOrderDao")
	private OrgPaperOrderDao orgPaperOrderDao;

	public OrgPaperOrderDao getOrgPaperOrderDao() {
		return orgPaperOrderDao;
	}

	public void setOrgPaperOrderDao(OrgPaperOrderDao orgPaperOrderDao) {
		this.orgPaperOrderDao = orgPaperOrderDao;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public boolean saveOrgPaperOrder(OrgPaperOrder orgPaperOrder)
			throws Exception {
		/*Map<String, Object> map = new HashMap<String, Object>();
		map.put("crtDate", orgPaperOrder.getCrtDate());
		map.put("operator", orgPaperOrder.getOperator());
		map.put("startDate", orgPaperOrder.getStartDate());
		map.put("endDate", orgPaperOrder.getEndDate());
		map.put("orgId", orgPaperOrder.getOrgId());
		map.put("status", orgPaperOrder.getStatus());*/
		return this.orgPaperOrderDao.saveOrgPaperOrder(orgPaperOrder);
	}
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean updateStatus(int orderId, String status) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);
		map.put("status", status);
		return this.orgPaperOrderDao.updateStatus(map);
	}
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean deleteOrgPaperOrder(int orderId, String status) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);
		map.put("status", status);
		return this.orgPaperOrderDao.deleteOrgPaperOrder(map);
	}

	public OrgPaperOrder getOrgPaperOrderByOrderID(int orderId)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);
		return this.orgPaperOrderDao.getOrgPaperOrderByOrderId(map);
	}
	public OrgPaperOrder getOrgPaperOrderByOrgIdAndCrtDate(String orgId, Date crtDate) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", orgId);
		map.put("crtDate", crtDate);
		return this.orgPaperOrderDao.getOrgPaperOrderByOrgIdAndCrtDate(map);
	}

	public List<OrgPaperOrder> getOrgPaperOrderByStatus(String orgId, String status)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", orgId);
		map.put("status", status);
		return this.orgPaperOrderDao.getOrgPaperOrderByStatus(map);
	}

	public List<OrgPaperOrder> getAllOrgPaperOrder() throws Exception {
		return this.orgPaperOrderDao.getAllOrgPaperOrder();
	}

	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest) throws Exception {
		return this.orgPaperOrderDao.pageQuery(GlobalConstant.PAGE_QUERY_STATEMENT, pageRequest);
	}
}
