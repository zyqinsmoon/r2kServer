package com.apabi.r2k.security.model;





/**
 * @author 
 */


/**
 * @author gao.song01
 *
 */
public class AuthRes {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//alias
	public static final String TABLE_ALIAS = "AuthRes";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_RES_NAME = "资源名称";
	public static final String ALIAS_RES_CODE = "资源编码";
	public static final String ALIAS_RES_URL = "资源地址";
	public static final String ALIAS_MODULE_ID = "所属模块ID";
	public static final String ALIAS_PRIORITY = "资源级别，排序";
	public static final String ALIAS_CRT_DATE = "crtDate";
	public static final String ALIAS_LAST_UPDATE = "lastUpdate";
	public static final String ALIAS_RES_DESC = "描述";
	
	//columns START
	private java.lang.Long id;
	private String resName;
	private String resCode;
	private String resUrl;
	private java.lang.Long type;
	private java.lang.Long entityId;
	private java.lang.Long parentId;
	private String viewOrder;
	private java.util.Date crtDate;
	private java.util.Date lastUpdate;
	private String resDesc;
	private String roleCode;
	//columns END



	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getId() {
		return this.id;
	}
	public void setResName(String value) {
		this.resName = value;
	}
	
	public String getResName() {
		return this.resName;
	}
	public void setResCode(String value) {
		this.resCode = value;
	}
	
	public String getResCode() {
		return this.resCode;
	}
	public void setResUrl(String value) {
		this.resUrl = value;
	}
	
	public String getResUrl() {
		return this.resUrl;
	}
	
	public void setViewOrder(String value) {
		this.viewOrder = value;
	}
	
	public String getViewOrder() {
		return this.viewOrder;
	}
	
	public java.lang.Long getEntityId() {
		return entityId;
	}

	public void setEntityId(java.lang.Long entityId) {
		this.entityId = entityId;
	}

	public void setCrtDate(java.util.Date value) {
		this.crtDate = value;
	}
	
	public java.util.Date getCrtDate() {
		return this.crtDate;
	}
	
	public void setLastUpdate(java.util.Date value) {
		this.lastUpdate = value;
	}
	
	public java.util.Date getLastUpdate() {
		return this.lastUpdate;
	}
	public void setResDesc(String value) {
		this.resDesc = value;
	}
	
	public String getResDesc() {
		return this.resDesc;
	}

	public java.lang.Long getParentId() {
		return parentId;
	}

	public void setParentId(java.lang.Long parentId) {
		this.parentId = parentId;
	}

	public java.lang.Long getType() {
		return type;
	}

	public void setType(java.lang.Long type) {
		this.type = type;
	}
}

