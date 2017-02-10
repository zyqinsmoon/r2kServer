package com.apabi.r2k.admin.model;


public class InfoTemplate {
	
	/** 模板id */
	private java.lang.Integer id;	
	/** 模板名称 */
	private java.lang.String name;
	/** 模板类型 */
	private java.lang.String type;
	/** 模板路径 */
	private java.lang.String path;
	/** 机构id */
	private java.lang.String orgId;
	/** 模板套号 */
	private java.lang.String setNo;
	/** 创建时间 */
	private java.util.Date crtDate;
	/** 最后修改时间 */
	private java.util.Date lastDate;
	/** 模板描述 */
	private java.lang.String description;
	/** 模板套信息 */
	private InfoTemplateSet templateSet;
	

	/** 模板类型：0首页 */
	public static final String TEMPLATE_TYPE_HOME = "0";
	/** 模板类型：1栏目 */
	public static final String TEMPLATE_TYPE_COLUMN = "1";
	/** 模板类型：2文章 */
	public static final String TEMPLATE_TYPE_ARTICLE = "2";
	/** 模板类型：3列表页 */
	public static final String TEMPLATE_TYPE_LIST = "3";
	/** 模板类型：4图片列表 */
	public static final String TEMPLATE_TYPE_PICTURELIST = "4";
	/** 模板类型：5视频列表 */
	public static final String TEMPLATE_TYPE_VIDEOLIST = "5";
	/** 模板类型：6文章列表 */
	public static final String TEMPLATE_TYPE_ARTICLELIST = "6";
	
	/** 同种模板类型默认模板：0非默认 */
	public static final int Defalut_MODEL_FALSE = 0;
	/** 同种模板类型默认模板：1默认 */
	public static final int Defalut_MODEL_TRUE = 1;
	
	
	// getter and setter
	
	public java.lang.Integer getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	public java.lang.String getType() {
		return type;
	}
	public void setType(java.lang.String type) {
		this.type = type;
	}
	public java.lang.String getPath() {
		return path;
	}
	public void setPath(java.lang.String path) {
		this.path = path;
	}
	public java.lang.String getSetNo() {
		return setNo;
	}
	public void setSetNo(java.lang.String setNo) {
		this.setNo = setNo;
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
	public java.lang.String getName() {
		return name;
	}
	public void setName(java.lang.String name) {
		this.name = name;
	}
	public java.lang.String getOrgId() {
		return orgId;
	}
	public void setOrgId(java.lang.String orgId) {
		this.orgId = orgId;
	}
	public InfoTemplateSet getTemplateSet() {
		return templateSet;
	}
	public void setTemplateSet(InfoTemplateSet templateSet) {
		this.templateSet = templateSet;
	}
}

