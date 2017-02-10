package com.apabi.r2k.admin.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class TopicCondition {

	@XStreamAlias("Query")
	private String query;
	@XStreamAlias("Sort")
	private String Sort;
	
	public TopicCondition(){}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getSort() {
		return Sort;
	}

	public void setSort(String sort) {
		Sort = sort;
	}
}
