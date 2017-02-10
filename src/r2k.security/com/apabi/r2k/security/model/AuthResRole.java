package com.apabi.r2k.security.model;



public class AuthResRole{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//alias
	public static final String TABLE_ALIAS = "AuthResRole";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_RES_ID = "resId";
	public static final String ALIAS_ROLE_ID = "roleId";
	public static final String ALIAS_CRT_DATE = "crtDate";
	
	//columns START
	private java.lang.Long id;
	private java.lang.Long resId;
	private java.lang.Long roleId;
	private java.util.Date crtDate;
	//columns END

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getId() {
		return this.id;
	}
	public void setResId(java.lang.Long value) {
		this.resId = value;
	}
	
	public java.lang.Long getResId() {
		return this.resId;
	}
	public void setRoleId(java.lang.Long value) {
		this.roleId = value;
	}
	
	public java.lang.Long getRoleId() {
		return this.roleId;
	}
	
	public void setCrtDate(java.util.Date value) {
		this.crtDate = value;
	}
	
	public java.util.Date getCrtDate() {
		return this.crtDate;
	}

	public AuthResRole(Long resId, Long roleId) {
		
		
		this.resId = resId;
		this.roleId = roleId;
		
	}
	public AuthResRole(){
		
	}
}

