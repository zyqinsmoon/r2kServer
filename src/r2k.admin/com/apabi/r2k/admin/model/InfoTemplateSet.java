package com.apabi.r2k.admin.model;

import java.util.List;

public class InfoTemplateSet {
	/** 模板套信息主键id */
	private java.lang.Integer id;	
	/** 模板套号 */
	private java.lang.String setNo;
	/** 模板套号 */
	private java.lang.String setName;
	/** 设备类型 */
	private java.lang.String deviceType;
	/** 设备类型默认模版 */
	private java.lang.String defaultType;
	/** 适用范围：0全部、1个性化 */
	private java.lang.Integer scope;
	/** 机构id */
	private java.lang.String orgId;
	/** 创建时间 */
	private java.util.Date crtDate;
	/** 最后修改时间 */
	private java.util.Date lastDate;
	/** 模板描述 */
	private java.lang.String description;
	/** 模板信息列表 */
	private List<InfoTemplate> templates;
	
	/** 适用范围：0全部 */
	public static final int SCOPE_TYPE_ALL = 0;
	/** 适用范围：1个性化 */
	public static final int SCOPE_TYPE_ORG = 1;
	
	
	public java.lang.Integer getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	public java.lang.String getSetNo() {
		return setNo;
	}
	public void setSetNo(java.lang.String setNo) {
		this.setNo = setNo;
	}
	public java.lang.String getSetName() {
		return setName;
	}
	public void setSetName(java.lang.String setName) {
		this.setName = setName;
	}
	public java.lang.String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(java.lang.String deviceType) {
		this.deviceType = deviceType;
	}
	public java.lang.String getDefaultType() {
		return defaultType;
	}
	public void setDefaultType(java.lang.String defaultType) {
		this.defaultType = defaultType;
	}
	public java.lang.Integer getScope() {
		return scope;
	}
	public void setScope(java.lang.Integer scope) {
		this.scope = scope;
	}
	public java.lang.String getOrgId() {
		return orgId;
	}
	public void setOrgId(java.lang.String orgId) {
		this.orgId = orgId;
	}
	public java.util.Date getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(java.util.Date crtDate) {
		this.crtDate = crtDate;
	}
	public java.util.Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(java.util.Date lastDate) {
		this.lastDate = lastDate;
	}
	public java.lang.String getDescription() {
		return description;
	}
	public void setDescription(java.lang.String description) {
		this.description = description;
	}
	public List<InfoTemplate> getTemplates() {
		return templates;
	}
	public void setTemplates(List<InfoTemplate> templates) {
		this.templates = templates;
	}
}
