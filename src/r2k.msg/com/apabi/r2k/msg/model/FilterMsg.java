package com.apabi.r2k.msg.model;

/**
 * filter消息体
 * @author l.wen
 *
 */
public class FilterMsg {

	private int id;
	private String filter;
	
	public FilterMsg(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
}
