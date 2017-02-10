package com.apabi.r2k.security.model;

public class AuthRole {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//alias
	public static final String ROLE_PREFIX = "ROLE_";
	public static final String TABLE_ALIAS = "AuthRole";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_ROLE_NAME = "角色名称";
	public static final String ALIAS_ROLE_CODE = "角色编码";
	public static final String ALIAS_ROLE_DESC = "角色描述";
	public static final String ALIAS_ROLE_CRT_USER = "创建者";
	public static final String ALIAS_CRT_DATE = "创建时间";
	public static final String ALIAS_LAST_UPDATE = "最后更新时间";
	public static final String ALIAS_ENABLED = "是否禁用0否-1是";
	//columns START
	private java.lang.Long id;
	private String roleName;
	private String roleCode;
	private String roleDesc;
	private java.lang.Long roleCrtUser;		//角色创建者id
	private java.util.Date crtDate;
	private java.util.Date lastUpdate;
	private java.lang.Long enabled;			//是否禁用0否-1是
	private java.lang.Long orgId;			//机构id
	private String deviceType;					//设备类型
	private java.lang.Long type;			//角色类型(0后台，1前台)
	private java.lang.String interfaceUrl;	//菜单接口地址
	private java.lang.String menuType;		//ENUM类型
	
	public static final int TYPE_FRONT = 1;
	public static final int TYPE_BACK = 0;
	
	public static final int TYPE_ANDROID_LARGE = 1;
	public static final int TYPE_ANDROID_PAD = 2;
	public static final int TYPE_ANDROID = 3;
	public static final int TYPE_IPAD = 4;
	public static final int TYPE_IPHONE = 5;
	public static final int TYPE_SLAVE = 6;
	public static final int TYPE_WEIXIN = 7;
	
	public static final Long TYPE_MENU_NORMAL = 1L;
	//columns END

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Long getId() {
		return this.id;
	}
	public void setRoleName(String value) {
		this.roleName = value;
	}
	
	public String getRoleName() {
		return this.roleName;
	}
	public void setRoleCode(String value) {
		this.roleCode = ROLE_PREFIX+this.id;
	}
	
	public String getRoleCode() {
		return this.roleCode;
	}
	public void setRoleDesc(String value) {
		this.roleDesc = value;
	}
	
	public String getRoleDesc() {
		return this.roleDesc;
	}
	public void setRoleCrtUser(java.lang.Long value) {
		this.roleCrtUser = value;
	}
	
	public java.lang.Long getRoleCrtUser() {
		return this.roleCrtUser;
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
	public void setEnabled(java.lang.Long value) {
		this.enabled = value;
	}
	
	public java.lang.Long getEnabled() {
		return this.enabled;
	}

	public void setOrgId(java.lang.Long orgId) {
		this.orgId = orgId;
	}

	public java.lang.Long getOrgId() {
		return orgId;
	}
	public java.lang.Long getType() {
		return type;
	}

	public void setType(java.lang.Long type) {
		this.type = type;
	}

	public java.lang.String getInterfaceUrl() {
		return interfaceUrl;
	}

	public void setInterfaceUrl(java.lang.String interfaceUrl) {
		this.interfaceUrl = interfaceUrl;
	}

	public java.lang.String getMenuType() {
		return menuType;
	}

	public void setMenuType(java.lang.String menuType) {
		this.menuType = menuType;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
}

