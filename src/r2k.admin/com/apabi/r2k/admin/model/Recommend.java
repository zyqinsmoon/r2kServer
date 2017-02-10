package com.apabi.r2k.admin.model;

import java.util.Date;

public class Recommend {
	
	private int id;			//id
	private String orgId;	//机构id
	private Date crtDate;	//创建时间
	private Date lastDate;	//最后更新时间
	private String resId;	//专题id
	private int sort;		//推荐顺序
	private String type;	//推荐类型：1报纸	2专题
	
	public static final String RECOMMEND_TYPE_PAPER = "1";		//报纸推荐类型
	public static final String RECOMMEND_TYPE_TOPIC = "2"; 		//专题推荐类型
	public static final int RECOMMEND_DEFAULT_SORT = 0; 		//推荐默认顺序
	
	
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
	public Date getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(Date crtDate) {
		this.crtDate = crtDate;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
