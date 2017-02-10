package com.apabi.r2k.security.model;

import java.util.ArrayList;
import java.util.List;

import com.apabi.r2k.admin.model.PrjEnum;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;


@XStreamAlias("Org")
public class AuthOrg {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//alias
	public static final String TABLE_ALIAS = "AuthOrg";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_ORG_NAME = "orgName";
	public static final String ALIAS_ORG_DESC = "orgDesc";
	public static final String ALIAS_PARENT_ID = "上级机构ID";
	public static final String ALIAS_ORG_CODE = "机构编码";
	public static final String ALIAS_CRT_DATE = "创建时间";
	public static final String ALIAS_LAST_UPDATE = "最后更新时间";
	public static final String ALIAS_MOBLE = "mobile";
	public static final String ALIAS_OFFICE_PHONE = "officePhone";
	public static final String ALIAS_ORG_TYPE = "机构类型";
	public static final String ALIAS_EMAIL = "email";
	public static final String ALIAS_PAPER = "报纸（是否授权:0未授权，1已授权）";
	public static final String ALIAS_PICTURE = "图片（是否授权:0未授权，1已授权）";

	public static final String ALIAS_EBOOK = "书（是否授权:0未授权，1已授权）";
	public static final String ALIAS_MAKER_ID = "模板id";
	public static final String ALIAS_DEVICE_NUM = "屏数量";
	
	//columns START
	@XStreamOmitField
	private int id;
	@XStreamAlias("OrgName")
	private String orgName;
	@XStreamAlias("OrgId")
	private String orgId;
	@XStreamOmitField
	private String orgDesc;
	@XStreamOmitField
	private java.lang.Long parentId;
	@XStreamOmitField
	private java.util.Date crtDate;
	@XStreamOmitField
	private java.util.Date lastUpdate;
	@XStreamOmitField
	private String mobile;
	@XStreamOmitField
	private String officePhone;
	@XStreamOmitField
	private String orgType;
	@XStreamOmitField
	private String email;
	@XStreamOmitField
	private int makerId;		//模板id
	@XStreamOmitField
	private int deviceNum;		//屏数量
	
	@XStreamOmitField
	private String ebook;			//读书授权（是否授权:0未授权，1已授权）
	@XStreamOmitField
	private String paper;			//报纸授权（是否授权:0未授权，1已授权）
	@XStreamOmitField
	private String topic;			//专题授权（是否授权:0未授权，1已授权）
	@XStreamOmitField
	private String publish;			//资讯授权（是否授权:0未授权，1已授权）
	@XStreamOmitField
	private String picture;			//图片授权（是否授权:0未授权，1已授权）
	@XStreamOmitField
	private java.lang.Long orgRoleId;
	@XStreamOmitField
	private int isAdmin;
	
	private String areaCode;
	@XStreamOmitField
	private List<AuthUser> authUserList;
	private List<AuthRole> authRoleList;
	private List<String> enumAuthList;
	
	public static final String AUTH_STATUS_NO = "0";
	public static final String AUTH_STATUS_YES = "1";
	
	//设置enumAuthList初值
	public void setEnumAuthList(List<AuthRole> authRoleList) {
		enumAuthList = new ArrayList<String>();
		for (AuthRole authRole : authRoleList) {
			if(authRole != null){
				String menuType = authRole.getMenuType();
				boolean flag = false;
				if(menuType != null){
					for (String enumAuth : enumAuthList) {
						if(menuType.equals(enumAuth)){
							flag = true;
							break;
						}
					}
					if(!flag){
						enumAuthList.add(menuType);
					}
				}
			}
		}
	}
	
	//columns END

	public List<AuthUser> getAuthUserList() {
		return authUserList;
	}

	public void setAuthUserList(List<AuthUser> authUserList) {
		this.authUserList = authUserList;
	}


	public void setOrgName(String value) {
		this.orgName = value;
	}
	
	public String getOrgName() {
		return this.orgName;
	}
	public void setOrgDesc(String value) {
		this.orgDesc = value;
	}
	
	public String getOrgDesc() {
		return this.orgDesc;
	}
	public void setParentId(java.lang.Long value) {
		this.parentId = value;
	}
	
	public java.lang.Long getParentId() {
		return this.parentId;
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
	public void setOrgType(String value) {
		this.orgType = value;
	}
	
	public String getOrgType() {
		return this.orgType;
	}
	public void setEmail(String value) {
		this.email = value;
	}
	
	public String getEmail() {
		return this.email;
	}

	public void setOrgRoleId(java.lang.Long orgRoleId) {
		this.orgRoleId = orgRoleId;
	}

	public java.lang.Long getOrgRoleId() {
		return orgRoleId;
	}

	public int getMakerId() {
		return makerId;
	}

	public void setMakerId(int makerId) {
		this.makerId = makerId;
	}

	public int getDeviceNum() {
		return deviceNum;
	}

	public void setDeviceNum(int deviceNum) {
		this.deviceNum = deviceNum;
	}

	public List<AuthRole> getAuthRoleList() {
		return authRoleList;
	}

	public void setAuthRoleList(List<AuthRole> authRoleList) {
		this.authRoleList = authRoleList;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public List<String> getEnumAuthList() {
		return enumAuthList;
	}

	public String getEbook() {
		return ebook;
	}

	public void setEbook(String ebook) {
		this.ebook = ebook;
	}

	public String getPaper() {
		return paper;
	}

	public void setPaper(String paper) {
		this.paper = paper;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getPublish() {
		return publish;
	}

	public void setPublish(String publish) {
		this.publish = publish;
	}

	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public List<String> getAllAuthRes(){
		List<String> authResList = new ArrayList<String>();
		if(this.getEbook().equals(authresstatus)){
			authResList.add(PrjEnum.ENUM_VALUE_BOOKS);
		}
		if(getPaper().equals(authresstatus)){
			authResList.add(PrjEnum.ENUM_VALUE_PAPER);
		}
		if(getPicture().equals(authresstatus)){
			authResList.add(PrjEnum.ENUM_VALUE_PICTURE);
		}
		if(getPublish().equals(authresstatus)){
			authResList.add(PrjEnum.ENUM_VALUE_PUBLISH);
		}
		if(getTopic().equals(authresstatus)){
			authResList.add(PrjEnum.ENUM_VALUE_TOPIC);
		}
		return authResList;
	}
	
	
	private String authresstatus="1";

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	
}

