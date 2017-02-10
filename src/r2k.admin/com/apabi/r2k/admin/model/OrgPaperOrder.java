package com.apabi.r2k.admin.model;

import java.util.Date;

public class OrgPaperOrder {
	
	private int orderId;		//订单Id
	private Date crtDate;		//创建时间
	private String operator;	//创建人
	private Date startDate;		//授权开始时间
	private Date endDate;		//授权结束时间
	private String orgId;		//机构ID
	private String status;		//订单状态（1未生效，2已生效，3已过期，4已删除）

	public static final String ORDER_STATUS_UNAUTH = "1";	//订单状态：1未生效
	public static final String ORDER_STATUS_AUTH   = "2";	//订单状态：2已生效
	public static final String ORDER_STATUS_EXPIRE = "3";	//订单状态：3已过期
	public static final String ORDER_STATUS_DELETE = "4";	//订单状态：4已删除
	
	
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public Date getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(Date crtDate) {
		this.crtDate = crtDate;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
