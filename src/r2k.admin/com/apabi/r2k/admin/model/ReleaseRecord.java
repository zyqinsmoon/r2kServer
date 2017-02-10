package com.apabi.r2k.admin.model;

public class ReleaseRecord {
	
	//alias
	public static final String TABLE_ALIAS = "ReleaseRecord";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_ORG_ID = "机构id";
	public static final String ALIAS_DEVICE_ID = "设备id";
	public static final String ALIAS_STATUS = "发布状态：0未发布、1已发布、2已过期";
	public static final String ALIAS_RELEASE_DATE = "发布时间";
	public static final String ALIAS_SETNO = "模板套号";
	public static final String ALIAS_DEVICE_TYPE = "设备类型";
	public static final String ALIAS_CRT_DATE = "创建时间";
	public static final String ALIAS_LAST_DATE = "最后修改时间";
	
	private java.lang.Integer id;
	private java.lang.String orgId;
	private java.lang.String deviceId;
	private java.lang.String status;
	private java.util.Date releaseDate;
	private java.lang.String deviceType;
	private java.util.Date crtDate;
	private java.util.Date lastDate;
	private Integer columnId;
	private Integer templateId;

	public final static String STATUS_UNPUBLISH = "0";		//未发布
	public final static String STATUS_PUBLISHED = "1";		//已发布
	public final static String STATUS_EXPIRED = "2";		//已过期
	
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setOrgId(java.lang.String value) {
		this.orgId = value;
	}
	
	public java.lang.String getOrgId() {
		return this.orgId;
	}
	public void setDeviceId(java.lang.String value) {
		this.deviceId = value;
	}
	
	public java.lang.String getDeviceId() {
		return this.deviceId;
	}
	public void setStatus(java.lang.String value) {
		this.status = value;
	}
	
	public java.lang.String getStatus() {
		return this.status;
	}
	public void setReleaseDate(java.util.Date value) {
		this.releaseDate = value;
	}
	
	public java.util.Date getReleaseDate() {
		return this.releaseDate;
	}
	public void setDeviceType(java.lang.String value) {
		this.deviceType = value;
	}
	
	public java.lang.String getDeviceType() {
		return this.deviceType;
	}
	public void setCrtDate(java.util.Date value) {
		this.crtDate = value;
	}
	
	public java.util.Date getCrtDate() {
		return this.crtDate;
	}
	public void setLastDate(java.util.Date value) {
		this.lastDate = value;
	}
	public java.util.Date getLastDate() {
		return this.lastDate;
	}

	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
}

