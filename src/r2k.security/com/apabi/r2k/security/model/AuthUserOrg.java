package com.apabi.r2k.security.model;



public class AuthUserOrg {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//alias
	public static final String TABLE_ALIAS = "AuthUserOrg";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_USER_ID = "userId";
	public static final String ALIAS_ORG_ID = "orgId";
	public static final String ALIAS_CRT_DATE = "ctrDate";
	
	//columns START
	private java.lang.Long id;
	private java.lang.Long userId;
	private java.lang.Long orgId;
	private java.util.Date crtDate;
	//columns END

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getId() {
		return this.id;
	}
	public void setUserId(java.lang.Long value) {
		this.userId = value;
	}
	
	public java.lang.Long getUserId() {
		return this.userId;
	}
	public void setOrgId(java.lang.Long value) {
		this.orgId = value;
	}
	
	public java.lang.Long getOrgId() {
		return this.orgId;
	}
	
	public void setCrtDate(java.util.Date value) {
		this.crtDate = value;
	}
	
	public java.util.Date getCrtDate() {
		return this.crtDate;
	}
}

