package com.apabi.r2k.security.model;

public class AuthOrgRole {
	
	//alias
	public static final String TABLE_ALIAS = "AuthOrgRole";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_ORG_ID = "orgId";
	public static final String ALIAS_ROLE_ID = "roleId";
	public static final String ALIAS_CRT_DATE = "crtDate";
	public static final String ALIAS_LAST_UPDATE = "lastUpdate";
	
	private java.lang.Integer id;
	private String orgId;
	private java.lang.Long roleId;
	private java.util.Date crtDate;
	private java.util.Date lastUpdate;

	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setOrgId(String value) {
		this.orgId = value;
	}
	
	public String getOrgId() {
		return this.orgId;
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

	public java.lang.Long getRoleId() {
		return roleId;
	}

	public void setRoleId(java.lang.Long roleId) {
		this.roleId = roleId;
	}
}

