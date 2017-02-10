package com.apabi.r2k.paper.model;

public class SyncMessageBody {
	
	//alias
	public static final String TABLE_ALIAS = "SyncMessageBody";
	public static final String ALIAS_ID = "消息体id";
	public static final String ALIAS_PAPER_ID = "报纸id";
	public static final String ALIAS_PERIOD_ID = "期次id";
	public static final String ALIAS_TYPE = "消息类型";
	
	private java.lang.Integer id;
	private java.lang.String paperId;
	private java.lang.String periodId;
	private java.lang.String type;		//消息类型：paper:报纸消息, filter:filter消息
	private String filter;

	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setPaperId(java.lang.String value) {
		this.paperId = value;
	}
	
	public java.lang.String getPaperId() {
		return this.paperId;
	}
	public void setPeriodId(java.lang.String value) {
		this.periodId = value;
	}
	
	public java.lang.String getPeriodId() {
		return this.periodId;
	}
	public void setType(java.lang.String value) {
		this.type = value;
	}
	
	public java.lang.String getType() {
		return this.type;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
}

