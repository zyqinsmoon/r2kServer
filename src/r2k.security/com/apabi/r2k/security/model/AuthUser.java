package com.apabi.r2k.security.model;

import java.util.List;


public class AuthUser {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//alias
	public static final String TABLE_ALIAS = "AuthUser";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_USER_NAME = "用户名";
	public static final String ALIAS_PASSWORD = "登陆密码";
	public static final String ALIAS_LOGIN_NAME = "登录名";
	public static final String ALIAS_USER_DESC = "用户描述";
	public static final String ALIAS_ENABLED = "是否被禁用0正常-1禁用";
	public static final String ALIAS_IS_ADMIN = "是否超级管理员0是-1不是";
	public static final String ALIAS_AUTH_ORG_ID = "用户所属机构ID";
	public static final String ALIAS_MOBILE = "移动电话";
	public static final String ALIAS_OFFICE_PHONE = "办公电话";
	public static final String ALIAS_EMAIL = "EMAIL地址";
	public static final String ALIAS_CRT_DATE = "创建时间";
	public static final String ALIAS_LAST_UPDATE = "最后更新时间";
	
	//columns START
	private java.lang.Long id;
	private String userName;
	private String password;
	private String loginName;
	private String userDesc;
	private java.lang.Long enabled;
	private java.lang.Long isAdmin;
	private String authOrgId;
	private String mobile;
	private String officePhone;
	private String email;
	private java.util.Date crtDate;
	private java.util.Date lastUpdate;
	private AuthOrg authOrg;
	private AuthOrg currentOrg;
	private String token;			//用户token 
	private List<AuthUserRole> authUserRoleList;
	private List<AuthRole> authRoleList;
	private String userAgent;
	private String softVersion;
	private String devId;

	public static Long USER_STATUS_ENABLED = 0L;		//用户状态：正常
	public static Long USER_STATUS_DISABLE = -1L;		//用户状态：禁用
	
	public static Long USER_ISADMIN_YES = 0L;			//是否超级管理员：是
	public static Long USER_ISADMIN_NO = -1L;			//是否超级管理员：不是

	
	//columns END
	public List<AuthRole> getAuthRoleList() {
		return authRoleList;
	}

	public void setAuthRoleList(List<AuthRole> authRoleList) {
		this.authRoleList = authRoleList;
	}
	public AuthOrg getAuthOrg() {
		return authOrg;
	}

	public void setAuthOrg(AuthOrg authOrg) {
		this.authOrg = authOrg;
	}

	public List<AuthUserRole> getAuthUserRoleList() {
		return authUserRoleList;
	}

	public void setAuthUserRoleList(List<AuthUserRole> authUserRoleList) {
		this.authUserRoleList = authUserRoleList;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getId() {
		return this.id;
	}
	public void setUserName(String value) {
		this.userName = value;
	}
	
	public String getUserName() {
		return this.userName;
	}
	public void setPassword(String value) {
		this.password = value;
	}
	
	public String getPassword() {
		return this.password;
	}
	public void setLoginName(String value) {
		this.loginName = value;
	}
	
	public String getLoginName() {
		return this.loginName;
	}
	public void setUserDesc(String value) {
		this.userDesc = value;
	}
	
	public String getUserDesc() {
		return this.userDesc;
	}
	public void setEnabled(java.lang.Long value) {
		this.enabled = value;
	}
	
	public java.lang.Long getEnabled() {
		return this.enabled;
	}
	public void setIsAdmin(java.lang.Long value) {
		this.isAdmin = value;
	}
	
	public java.lang.Long getIsAdmin() {
		return this.isAdmin;
	}
	public void setMobile(String value) {
		this.mobile = value;
	}
	
	public String getMobile() {
		return this.mobile;
	}
	public void setOfficePhone(String value) {
		this.officePhone = value;
	}
	
	public String getOfficePhone() {
		return this.officePhone;
	}
	public void setEmail(String value) {
		this.email = value;
	}
	
	public String getEmail() {
		return this.email;
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

	public AuthOrg getCurrentOrg() {
		return currentOrg;
	}

	public void setCurrentOrg(AuthOrg currentOrg) {
		this.currentOrg = currentOrg;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAuthOrgId() {
		return authOrgId;
	}

	public void setAuthOrgId(String authOrgId) {
		this.authOrgId = authOrgId;
	}
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getSoftVersion() {
		return softVersion;
	}

	public void setSoftVersion(String softVersion) {
		this.softVersion = softVersion;
	}

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

}

