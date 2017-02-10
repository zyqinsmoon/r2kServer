package com.apabi.r2k.paper.model;

public class PaperPriority {
	
	//alias
	public static final String TABLE_ALIAS = "PaperPriority";
	public static final String ALIAS_ID = "id";
	public static final String ALIAS_PAPER_ID = "报纸id";
	public static final String ALIAS_PRIORITY = "报纸优先级";
	
	
	private java.lang.Integer id;
	private java.lang.String paperId;
	private java.lang.Integer priority;

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
	public void setPriority(java.lang.Integer value) {
		this.priority = value;
	}
	
	public java.lang.Integer getPriority() {
		return this.priority;
	}
}

