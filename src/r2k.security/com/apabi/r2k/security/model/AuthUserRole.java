package com.apabi.r2k.security.model;



public class AuthUserRole {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "AuthUserRole";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_ROLE_ID = "roleId";
	public static final String ALIAS_USER_ID = "userId";
	public static final String ALIAS_CRT_DATE = "crtDate";
	
	//columns START
	private java.lang.Long id;
	private java.lang.Long roleId;
	private java.lang.Long userId;
	private java.util.Date crtDate;
	//columns END
	public AuthUserRole(){
		
	}
	public AuthUserRole(Long roleId, Long userId) {
		
		this.roleId = roleId;
		this.userId = userId;
	}
	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getId() {
		return this.id;
	}
	public void setRoleId(java.lang.Long value) {
		this.roleId = value;
	}
	
	public java.lang.Long getRoleId() {
		return this.roleId;
	}
	public void setUserId(java.lang.Long value) {
		this.userId = value;
	}
	
	public java.lang.Long getUserId() {
		return this.userId;
	}
	
	public void setCrtDate(java.util.Date value) {
		this.crtDate = value;
	}
	
	public java.util.Date getCrtDate() {
		return this.crtDate;
	}
	public String getRoleCode(){
		return AuthRole.ROLE_PREFIX+this.roleId;
	}
}

